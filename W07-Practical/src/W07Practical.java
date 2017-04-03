import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class W07Practical {

	private static final int PROPERTIES_PATH = 0;
	private static final int CSV_PATH = 1;
	private static final int ACTION_TAKEN = 2;
	private static final int NUMBER_OF_ARGS = 3;

	public static void main(String[] args) throws IOException, SQLException {

		Connection jdbcConnection;

		//check that the length of args is correct
		if (args.length != NUMBER_OF_ARGS) {
			System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file> <action>");
			System.exit(0);
		}
		//switch action to be taken
		try {
			W07Practical myDB = new W07Practical();
			jdbcConnection = myDB.tryToAccessDB(args[PROPERTIES_PATH]);
			QueryPrinter qp = new QueryPrinter(jdbcConnection);
			switch (args[ACTION_TAKEN]) {
				case "create":
					//create the table
					createTable(jdbcConnection);
					CSVLoader loader = new CSVLoader(jdbcConnection, args[CSV_PATH]);
					loader.loadCSVData();
					System.out.println("OK");
					break;
				case "query1":
					//list all the records in the database
					qp.printAllRecords();
					break;
				case "query2":
					//print out the total number of invoices in the database
					qp.printNoOfInvoices();
					break;
				case "query3":
					//list the invoice number and total price for each invoice
					qp.printInvoiceNoAndTotalPrice();
					break;
				case "query4":
					//print the invoice number and total price for the invoice with the highest total price
					qp.printOutHighestInvoice();
					break;
				default:
					System.out.println("Command does not exist! Commands are: create, query1, query2, query3 and query4");
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
	private static Connection tryToAccessDB(String propertiesLocation) throws IOException, SQLException {
		Connection connection = null;
		//load the properties file
		PropertyLoader pl = new PropertyLoader(propertiesLocation);
		connection = DriverManager.getConnection(pl.getDBUrl(), pl.getUserName(), pl.getPassword());
		//return the connection
		return connection;
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
