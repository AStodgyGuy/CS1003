import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class PrintWriter {

    private static Connection connection;

    public PrintWriter(Connection connection) {
        this.connection = connection;
    }

    //method which prints all the records in the database
    public void printAllRecords() throws SQLException {
        Statement statement = null;
        String query = "SELECT * FROM data";
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        boolean flag = true;
        while (rs.next()) {
            if (flag) {
                String invoiceNo = rs.getString("InvoiceNo");
                String stockCode = rs.getString("StockCode");
                String description = rs.getString("Description");
                String quantity = rs.getString("Quantity");
                String invoiceDate = rs.getString("InvoiceDate");
                String unitPrice = rs.getString("UnitPrice");
                String customerID = rs.getString("CustomerID");
                String country = rs.getString("Country");
                String output = invoiceNo + ", " + stockCode + ", " + description + ", "
                                + quantity + ", " + invoiceDate + ", " + unitPrice + ", "
                                + customerID + ", " + country;
                System.out.println(output);
                flag = false;
             } else {
                int invoiceNo = convertToInt(rs.getString("InvoiceNo"));
                String stockCode = rs.getString("StockCode");
                String description = rs.getString("Description");
                int quantity = convertToInt(rs.getString("Quantity"));
                String invoiceDate = rs.getString("InvoiceDate");
                double unitPrice = convertToDouble(rs.getString("UnitPrice"));
                String customerID = rs.getString("CustomerID");
                String country = rs.getString("Country");

                String output = invoiceNo + ", " + stockCode + ", " + description + ", "
                                + quantity + ", " + invoiceDate + ", " + unitPrice + ", "
                                + customerID + ", " + country;
                System.out.println(output);
             }
        }
    }
    //method which prints out the total number of invoices
    public void printNoOfInvoices() throws SQLException {
        Statement statement = null;
        String query = "SELECT InvoiceNo FROM data";
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

		int counter = 0;
		String previousInvoiceNo = "";
		while (rs.next()) {
			String invoiceNo = rs.getString("InvoiceNo");
			if (previousInvoiceNo.equals("")) {
				previousInvoiceNo = invoiceNo;
			}

			if (!previousInvoiceNo.equals(invoiceNo)) {
				counter++;
				previousInvoiceNo = invoiceNo;
			}
		}

		System.out.println("Number of Invoices\n" + counter);
	}

	//method which prints out the invoice number and total price
	public void printInvoiceNoAndTotalPrice() throws SQLException {
		Statement statement = null;
    	String query = "SELECT InvoiceNo, Quantity, UnitPrice FROM data";
		statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);

        String previousInvoiceNo = "";
        boolean flag = true;
        double total = 0;
		while (rs.next()) {
            String invoiceNo = rs.getString("InvoiceNo");
            if (flag) {
                System.out.println(invoiceNo + ", " + "Total Price");
                flag = false;
            } else {
                if (previousInvoiceNo.equals("")) {
                    previousInvoiceNo = invoiceNo;
                }
                if (!previousInvoiceNo.equals(invoiceNo)) {
                    System.out.println(previousInvoiceNo + ", " + total);
                    previousInvoiceNo = invoiceNo;
                    total = 0;
                    int quantity = convertToInt(rs.getString("Quantity"));
                    double unitPrice = convertToDouble(rs.getString("UnitPrice"));
                    total = total + quantity * unitPrice;
                } else {
                    int quantity = convertToInt(rs.getString("Quantity"));
                    double unitPrice = convertToDouble(rs.getString("UnitPrice"));
                    total = total + quantity * unitPrice;
                }
            }
		}
        //print out last invoice number
        System.out.println(previousInvoiceNo + ", " + total);
	}

	//method which prints out the highest invocie
	public void printOutHighestInvoice() throws SQLException {
		Statement statement = null;
    	String query = "SELECT InvoiceNo, Quantity, UnitPrice FROM data";
		statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        String previousInvoiceNo = "";
        String highestInvoice = "";
        double total = 0.0;
        double highestPrice = 0;
        boolean flag = true;
		while (rs.next()) {
            String invoiceNo = rs.getString("InvoiceNo");
            if (flag) {
                System.out.println(invoiceNo + ", " + "Maximum Total Price");
                flag = false;
            } else {
                if (previousInvoiceNo.equals("")) {
                    previousInvoiceNo = invoiceNo;
                }
                if (!previousInvoiceNo.equals(invoiceNo)) {
                    if (total > highestPrice) {
                        highestInvoice = previousInvoiceNo;
                        highestPrice = total;
                    }
                    previousInvoiceNo = invoiceNo;
                    total = 0;
                    int quantity = convertToInt(rs.getString("Quantity"));
                    double unitPrice = convertToDouble(rs.getString("UnitPrice"));
                    total = total + quantity * unitPrice;
                } else {
                    int quantity = convertToInt(rs.getString("Quantity"));
                    double unitPrice = convertToDouble(rs.getString("UnitPrice"));
                    total = total + quantity * unitPrice;
                }
            }
        }

        if (total > highestPrice) {
            highestInvoice = previousInvoiceNo;
            highestPrice = total;
        }
        System.out.println(highestInvoice + ", " + highestPrice);
	}

    //method which converts string to an integer
    private int convertToInt(String s) {
        int number = 0;

        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }

    //method which converts string to a double
    private double convertToDouble(String s) {
        double number = 0.0;

        try {
            number = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }
}
