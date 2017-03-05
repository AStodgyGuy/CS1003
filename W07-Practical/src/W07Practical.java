import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class W07Practical {

	private static Connection JDBC_CONNECTION = null;
	private static final int PROPERTIES_PATH = 0;
	private static final int CSV_PATH = 1;
	private static final int ACTION_TAKEN = 2;
	private static final int NUMBER_OF_ARGS = 3;

	public static void main(String[] args) throws IOException, SQLException {

		//check that the length of args is correct
		if (args.length != NUMBER_OF_ARGS) {
			System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file> <action>");
			System.exit(0);
		}
		//switch action to be taken
		try {
			W07Practical myDB = new W07Practical();
			myDB.tryToAccessDB(args[PROPERTIES_PATH]);
			PrintWriter pw = new PrintWriter(JDBC_CONNECTION);
			switch (args[ACTION_TAKEN]) {
				case "create":
					//create the table
					createTable(JDBC_CONNECTION);
					CSVLoader loader = new CSVLoader(JDBC_CONNECTION, args[CSV_PATH]);
					loader.loadCSVData();
					System.out.println("OK");
					break;
				case "query1":
					//list all the records in the database
					pw.printAllRecords();
					break;
				case "query2":
					//print out the total number of invoices in the database
					pw.printNoOfInvoices();
					break;
				case "query3":
					//list the invoice number and total price for each invoice
					pw.printInvoiceNoAndTotalPrice();
					break;
				case "query4":
					//print the invoice number and total price for the invoice with the highest total price
					pw.printOutHighestInvoice();
					break;
				default:
					System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file> <action>");
			}

		} catch (IOException e) {
			System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file> <action>");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file> <action>");
			e.printStackTrace();
		}

	}

	//method which tries to connect to a database, code adapted from studres
	private static void tryToAccessDB(String propertiesLocation) throws IOException, SQLException {
		Connection connection = null;
		//load the properties file
		PropertyLoader pl = new PropertyLoader(propertiesLocation);
		connection = DriverManager.getConnection(pl.getDBUrl(), pl.getUserName(), pl.getPassword());
		//set the connection variable to the current connection
		JDBC_CONNECTION = connection;
	}

	//method which creates the table to store the csv, code adapted from studres
	private static void createTable(Connection connection) throws SQLException {
		//delete the table if it already exists
		Statement statement = connection.createStatement();
		statement.executeUpdate("DROP TABLE IF EXISTS data");
		//SQL statement to create the new table
		String tableCreation = "CREATE TABLE data (InvoiceNo varchar(10), StockCode varchar(10), Description varchar(50),"
								+ "Quantity varchar(10), InvoiceDate varchar(25), UnitPrice varchar(10), "
								+ "CustomerID varchar(10), Country varchar(50))";

		statement.executeUpdate(tableCreation);
		statement.close();
	}

}
