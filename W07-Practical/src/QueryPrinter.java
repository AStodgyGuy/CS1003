import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryPrinter {

    private static Connection connection;

    //constructor for QueryPrinter
    public QueryPrinter(Connection connection) {
        this.connection = connection;
    }

    //method which prints all the records in the database
    public void printAllRecords() throws SQLException {
        Statement statement = null;
        String query = "SELECT * FROM data";
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        //boolean flag used to print out the first line
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
                //set the flag to false as first line has been printed
                flag = false;
            //format the data so that it is in the correct data format
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

        //counter to store the number of invoices
		int counter = 0;
		String previousInvoiceNo = "";

		while (rs.next()) {
			String invoiceNo = rs.getString("InvoiceNo");
            //stores first previous inoivce to the current invoice
			if (previousInvoiceNo.equals("")) {
				previousInvoiceNo = invoiceNo;
			}
            //increment the counter if the current invoice number does not match the previous invoice number
			if (!invoiceNo.equals(previousInvoiceNo)) {
				counter++;
				previousInvoiceNo = invoiceNo;
			}
		}

        //print out the number of invoices
		System.out.println("Number of Invoices\n" + counter);
	}

	//method which prints out the invoice number and total price
	public void printInvoiceNoAndTotalPrice() throws SQLException {
		Statement statement = null;
    	String query = "SELECT InvoiceNo, Quantity, UnitPrice FROM data";
		statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);

        String previousInvoiceNo = "";
        double total = 0;

        //boolean flag used to print out the first line
        boolean flag = true;
		while (rs.next()) {
            String invoiceNo = rs.getString("InvoiceNo");
            //printing out the first line
            if (flag) {
                System.out.println(invoiceNo + ", " + "Total Price");
                flag = false;
            } else {
                //set the previous invoice number to the current invoice number
                if (previousInvoiceNo.equals("")) {
                    previousInvoiceNo = invoiceNo;
                }
                //calculate the total price for the current invoice
                if (invoiceNo.equals(previousInvoiceNo)) {
                    int quantity = convertToInt(rs.getString("Quantity"));
                    double unitPrice = convertToDouble(rs.getString("UnitPrice"));
                    total = total + quantity * unitPrice;
                //print out the previous invoice number and total if the current invoice number and previous invoice number do not match 
                } else {
                    System.out.println(previousInvoiceNo + ", " + total);
                    previousInvoiceNo = invoiceNo;
                    //reset the total
                    total = 0;
                    //calculate the new invoice total
                    int quantity = convertToInt(rs.getString("Quantity"));
                    double unitPrice = convertToDouble(rs.getString("UnitPrice"));
                    total = total + quantity * unitPrice;
                }
            }
		}
        //print out last invoice number
        System.out.println(previousInvoiceNo + ", " + total);
	}

	//method which prints out the highest invoice
	public void printOutHighestInvoice() throws SQLException {
		Statement statement = null;
    	String query = "SELECT InvoiceNo, Quantity, UnitPrice FROM data";
		statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        String previousInvoiceNo = "";
        String highestInvoice = "";
        double total = 0;
        double highestPrice = 0;
        boolean flag = true;


		while (rs.next()) {
            String invoiceNo = rs.getString("InvoiceNo");
            //boolean flag to print out the first line
            if (flag) {
                System.out.println(invoiceNo + ", " + "Maximum Total Price");
                //set flag to false as the first line has been printed
                flag = false;
            } else {
                //set the previous invoice number to the current invoice number
                if (previousInvoiceNo.equals("")) {
                    previousInvoiceNo = invoiceNo;
                }
                //if the current invoice and previous invoice match, calculate total
                if (invoiceNo.equals(previousInvoiceNo)) {
                    int quantity = convertToInt(rs.getString("Quantity"));
                    double unitPrice = convertToDouble(rs.getString("UnitPrice"));
                    total = total + quantity * unitPrice;
                    //current invoice number and previous invoice number do not match
                } else {
                    //check if the previous total is larger than the highest price
                    if (total > highestPrice) {
                        highestInvoice = previousInvoiceNo;
                        highestPrice = total;
                    }
                    //set the previous invoice number to the current invoice number
                    previousInvoiceNo = invoiceNo;
                    //reset the total
                    total = 0;
                    //calculate the total for this invoice
                    int quantity = convertToInt(rs.getString("Quantity"));
                    double unitPrice = convertToDouble(rs.getString("UnitPrice"));
                    total = total + quantity * unitPrice;
                }
            }
        }

        //last comparison
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
        double number = 0;

        try {
            number = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }
}
