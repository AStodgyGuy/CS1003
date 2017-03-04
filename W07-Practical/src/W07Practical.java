import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class W07Practical {
	
	private static Connection JDBC_CONNECTION = null;
	private static final int PROPERTIES_PATH = 0;
	private static final int CSV_PATH = 1;
	private static final int ACTION_TAKEN = 2;
	private static final int NUMBER_OF_ARGS = 3;

	public static void main(String[] args) throws SQLException {

		//check that the length of args is correct
		if (args.length != NUMBER_OF_ARGS) {
			System.out.println("Usage: java W07Practical <properties_file_name> <data_file> <sql_query>");
			System.exit(0);
		}
		
		W07Practical myDB = new W07Practical();
		myDB.tryToAccessDB(args[PROPERTIES_PATH]);

		//switch action to be taken
		try {
			switch (args[ACTION_TAKEN]) {
				case "create":
					createTable(JDBC_CONNECTION);
					CSVLoader loader = new CSVLoader(JDBC_CONNECTION, args[CSV_PATH]);
					loader.loadCSVData();
					System.out.println("OK");
					break;
				case "query1":
					//list all the records in the database
					PrintWriter pw = new PrintWriter(JDBC_CONNECTION);
					pw.printAllRecords();
					break;
				case "query2":
					//print out the total number of invoices in the database
					PrintWriter pw = new PrintWriter(JDBC_CONNECTION);
					pw.printNoOfInvoices();
					break;
				case "query3":
					//list the invoice number and total price for each invoice
					PrintWriter pw = new PrintWriter(JDBC_CONNECTION);
					pw.printInvoiceNoAndTotalPrice();
					break;
				case "query4":
					//print the invoice number and total price for the invoice with the highest total price
					PrintWriter pw = new PrintWriter(JDBC_CONNECTION);
					pw.printOutHighestInvoice();
					break;
				default:
					System.out.println("Usage: java W07Practical <properties_file_name> <data_file> <sql_query>");
			}

		} catch (IOException e) {
			System.out.println("Usage: java W07Practical <properties_file_name> <data_file> <sql_query>");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Usage: java W07Practical <properties_file_name> <data_file> <sql_query>");
			e.printStackTrace();
		}
		
	}

	//method which tries to connect to a database, code adapted from studres
	private static void tryToAccessDB(String propertiesLocation) throws SQLException {
		Connection connection = null;
		try {
			PropertyLoader pl = new PropertyLoader(propertiesLocation);
			connection = DriverManager.getConnection(pl.getDBUrl(), pl.getUserName(), pl.getPassword());
			JDBC_CONNECTION = connection;

		} catch (IOException e) {
			System.out.println("Usage: java W07Practical <properties_file_name> <data_file> <sql_query>");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Usage: java W07Practical <properties_file_name> <data_file> <sql_query>");
			e.printStackTrace();
		}
	}

	//method which creates the table to store the csv, code adapted from studres
	private static void createTable(Connection connection) throws SQLException {

		Statement statement = connection.createStatement();
		statement.executeUpdate("DROP TABLE IF EXISTS data");

		String tableCreation = "CREATE TABLE data (InvoiceNo varchar(10), StockCode varchar(10), Description varchar(50)," +
								"Quantity varchar(10), InvoiceDate varchar(25), UnitPrice varchar(10), CustomerID varchar(10), Country varchar(50))";

		statement.executeUpdate(tableCreation);
		statement.close();
	}

}
