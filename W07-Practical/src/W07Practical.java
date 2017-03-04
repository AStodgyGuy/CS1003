import java.sql.*;

public class W07Practical {

	private static Connection JDBC_CONNECTION = null;
	private static int PROPERTIES_PATH = 0;
	private static int CSV_PATH = 1;
	private static int ACTION_TAKEN = 2;
	
	public static void main(String[] args) throws SQLException{

		//args[0] should represent the path of the database.properties file.
		//args[1] should represent the path to the data file where your program will read the data
		//args[2] should specify the action taken by your program
		
		//read properties file args[0]
		//load data into database args[1]
		//specify action taken by your program args[2]

		//query 1 = list all the records in the database
		//query 2 = print out the total number of invoices in the database
		//query 3 = list the invoice number and total price for each invoice
		//query 4 = print the invoice number and total price for the invoice with the highest total price

		/*
		if (args.length != 3) {
			System.out.println("Usage: java W07Practical <properties_file_name> <data_file> <sql_query>");
			System.exit(0);
		}
		*/

		W07Practical myDB = new W07Practical();
		myDB.tryToAccessDB(args[PROPERTIES_PATH]);

		//CSVLoader loader = CSVLoader(JDBC_CONNECTION);

	}

	private void tryToAccessDB(String propertiesLocation) throws SQLException {
		Connection connection = null;
		try {
			PropertyLoader pl = new PropertyLoader(propertiesLocation);
			connection = DriverManager.getConnection(pl.getDBUrl(), pl.getUserName(), pl.getPassword());

			createTable(connection);
			System.out.println("success");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				setConnection(connection);
				System.out.println("ayo");
			}
		}
	}

	private void setConnection(Connection connection) throws SQLException {
		this.JDBC_CONNECTION = connection;
	}

	private void createTable(Connection connection) throws SQLException {

		Statement statement = connection.createStatement();
		statement.executeUpdate("DROP TABLE IF EXISTS data");

		String tableCreation = "CREATE TABLE data (InvoiceNo int, StockCode varchar(10), Description varchar(50)," +
								"Quantity int, InvoiceDate varchar(10), UnitPrice decimal, CustomerID int, Country varchar(50)";

		statement.executeUpdate(tableCreation);
		statement.close();
	}

}
