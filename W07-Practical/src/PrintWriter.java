import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class PrintWriter {

    private Connection connection;

    public PrintWriter(Connection connection) {
        this.connection = connection;
    }


	//method which prints all the records in the database
	private static void printAllRecords() throws SQLException {
		if (tableExists(connection)) {
			Statement statement = null;
    		String query = "SELECT * FROM data";
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				String invoiceNo = rs.getString("InvoiceNo");
				String stockCode = rs.getString("StockCode");
				String description = rs.getString("Description");
				String quantity = rs.getString("Quantity");
				String invoiceDate = rs.getString("InvoiceDate");
				String unitPrice = rs.getString("UnitPrice");
				String customerID = rs.getString("CustomerID");
				String country = rs.getString("Country");

				String output = invoiceNo + ", " + stockCode + ", " + description + ", " +
								quantity + ", " + invoiceDate + ", " + unitPrice + ", " +
								customerID + ", " + country;

				System.out.println(output);
			}
		} else {
			throw new SQLException();
		}
	}

	//method which prints out the total number of invoices
	private static void printNoOfInvoices() throws SQLException {
		if (tableExists(connection)) {
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

			System.out.println("Number Of Invoices\n" + counter);
		} else {
			throw new SQLException();
		}
	} 

	//method which prints out the invoice number and total price
	private static void printInvoiceNoAndTotalPrice() throws SQLException {
		if (tableExists(connection)) {
			Statement statement = null;
    		String query = "SELECT InvoiceNo, Quantity, UnitPrice FROM data";
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {

			}

		} else {
			throw new SQLException();
		}
	}

	//method which prints out the highest invocie
	private static void printOutHighestInvoice() throws SQLException {
		if (tableExists(connection)) {
			//sql query goes here
		} else {
			throw new SQLException();
		}
	}

	//method which checks if the 'data' table exists, code taken from stackoverflow
	private static boolean tableExists() throws SQLException{
		DatabaseMetaData dmd = connection.getMetaData();

		ResultSet tables = dmd.getTables(null, null, "data", null);
		
		if (tables.next()) {
			return true;
		} else {
			return false;
		}
	}

    private int convertToint(String s) {
        int number = 0;

        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return number;
    }

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