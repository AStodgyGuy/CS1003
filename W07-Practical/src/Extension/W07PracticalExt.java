import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class W07PracticalExt extends JPanel implements ActionListener {

    private static final int GUI_WIDTH = 1100;
    private static final int GUI_HEIGHT = 600;
    private static final int PROPERTIES_PATH = 0;
	private static final int CSV_PATH = 1;
    private static final int NUMBER_OF_ARGS = 2;

    private JLabel invoiceNoText;
    private JTextField invoiceNoSearch;
    private JButton launchSearch, launchRefresh, launchAdd, launchTotalPrice;
    private JTable table;
    private static JFrame gui;
    private static Connection connection;

    public static void main(String[] args) {

        try {
            //check the args length
            if (args.length < 1 || args.length > NUMBER_OF_ARGS) {
                System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file>");
                System.exit(0);
            }

            //Load the property files
            PropertyLoader pl = new PropertyLoader(args[PROPERTIES_PATH]);
            connection = DriverManager.getConnection(pl.getDBUrl(), pl.getUserName(), pl.getPassword());

            //if there are 2 arguments, load the CSV file into the database
            if (args.length == NUMBER_OF_ARGS) {
                createTable(connection);
                CSVLoader csvLoader = new CSVLoader(connection, args[CSV_PATH]);
                csvLoader.loadCSVData(); 
            }

            //run and show the GUI
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });

        } catch (IOException e) {
            System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file>");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file>");
            e.printStackTrace();
        }
    }

    //this method creates and displays the GUI
    public static void createAndShowGUI() {
        try {
            gui = new JFrame("W07PracticalExt");
            gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            gui.setSize(GUI_WIDTH, GUI_HEIGHT);

            JComponent contentPane = new W07PracticalExt();
            contentPane.setOpaque(true);
            gui.setContentPane(contentPane);

            gui.setTitle("W07 Practical Extension");
            gui.setVisible(true);
        } catch (SQLException e) {
            System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file>");
            e.printStackTrace();       
        }

    }

    //constructor which constructs the various aspects of the gui
    public W07PracticalExt() throws SQLException{
        super(new BorderLayout());

        //create Jbutton to launch search
        launchSearch = new JButton("Search");
        launchSearch.addActionListener(this);

        //create jbutton to launch add
        launchAdd = new JButton("Add");
        launchAdd.addActionListener(this);

        //create Refresh button
        launchRefresh = new JButton("Refresh");
        launchRefresh.addActionListener(this);

        //create compress view button
        launchTotalPrice = new JButton("Total Price");
        launchTotalPrice.addActionListener(this);

        //add table object to the gui
        Statement statement = null;
        String query = "SELECT * FROM data";
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        DefaultTableModel model = buildTableModel(rs);
        table = new JTable(model);


        //create jpanel to put everything on
        JPanel panel = new JPanel();
        panel.add(launchAdd);
        panel.add(launchSearch);
        panel.add(launchRefresh);
        panel.add(launchTotalPrice);

        //add the panel to the gui
        add(panel, BorderLayout.PAGE_START);
        //add the scroll panel to the gui
        add(new JScrollPane(table), BorderLayout.CENTER);
        //create borders around the panel
        setBorder(BorderFactory.createEmptyBorder(35,35,35,35));
    }

    //this method detects the action event the user performs and launches the relevant task
    public void actionPerformed(ActionEvent event) {

        //if the add button is pressed
        if (event.getSource() == launchAdd) {
            //create new instance of the AddInfoGUI
            AddInfoGUI aig = new AddInfoGUI(connection);

        //if the search button is pressed
        } else if (event.getSource() == launchSearch) {
            String column = "";
            String value = "";
            String[] possibilities = {"InvoiceNo" , "StockCode", "CustomerID", "Country"};
            column = (String)JOptionPane.showInputDialog(null, "Choose column to filter by:", "Choose column", JOptionPane.PLAIN_MESSAGE, null, possibilities, "InvoiceNo");

            //check what the user inputs
            try {
                //if the user presses cancel or exit, return
                if (column != null) {
                    value = (String)JOptionPane.showInputDialog(null, "Enter a(n) " + column + " value:", "Choose value", JOptionPane.PLAIN_MESSAGE);
                    //user presses cancel or exit
                    if (value == null) {
                        return;
                    //user enters nothing throw new NullPointerException
                    } else if (value.equals("")) {
                        throw new NullPointerException();
                    }
                } else {
                    return;
                }

                //query to search for specific terms
                String query = "";
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM data WHERE " + column + " = ?");
                statement.setString(1, value);
                ResultSet rs = statement.executeQuery();
                table.setModel(buildTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Invalid SQL, please try again.", "SQL Error", JOptionPane.WARNING_MESSAGE);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Invalid input, please try again.", "Null Value Error", JOptionPane.WARNING_MESSAGE);
            }

        //if the compress button is pressed
        } else if (event.getSource() == launchTotalPrice) {
             try {
                //query which displays a JTable sorted by InvoiceNo
                String query = "SELECT InvoiceNo, CustomerID, Country, Quantity, UnitPrice FROM data ORDER BY InvoiceNo";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                //create and display the new table
                table.setModel(customTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Invalid SQL, please try again.", "SQL Error", JOptionPane.WARNING_MESSAGE);
            }

        //if the Refresh button is pressed
        } else if (event.getSource() == launchRefresh) {
            try {
                //query which displays a JTable sorted by InvoiceNo
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM data ORDER BY InvoiceNo");
                //create and display the new table
                table.setModel(buildTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Invalid SQL, please try again.", "SQL Error", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    //method which populates a JTable, code taken from StackOverflow
    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

    //method which populates the JTable to show only the invoice, customer id, country and total invoice price
    private static DefaultTableModel customTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        
        // names of columns
        Vector<String> columnNames = new Vector<String>();
        for (int column = 1; column <= 3; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        columnNames.add("Total Price");
        
        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        String previousInvoice = "";
        String previousCountry = "";
        String previousCustomerID = "";
        double total = 0;
        boolean flag = true;
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            //flag for the initial previous invoice
            if (flag) {
                previousInvoice = rs.getString("InvoiceNo");
                previousCustomerID = rs.getString("CustomerID");
                previousCountry = rs.getString("Country");
                flag = false;
            } else {
                if (rs.getString("InvoiceNo").equals(previousInvoice)) {
                    total = total + calculateCost(convertToInt(rs.getString("Quantity")), convertToDouble(rs.getString("UnitPrice")));
                    //check if the current position in the result set is at the bottom
                    if (rs.isLast()) {
                        vector.add(rs.getString("InvoiceNo"));
                        vector.add(rs.getString("CustomerID"));
                        vector.add(rs.getString("Country"));
                        vector.add(total);
                        data.add(vector);
                    }
                } else {
                    vector.add(previousInvoice);
                    vector.add(previousCustomerID);
                    vector.add(previousCountry);
                    vector.add(total);
                    total = 0;
                    data.add(vector);
                    //check if the current position in the result set is at the bottom
                    if (rs.isLast()) {
                        vector = new Vector<Object>();
                        vector.add(rs.getString("InvoiceNo"));
                        vector.add(rs.getString("CustomerID"));
                        vector.add(rs.getString("Country"));
                        total = total + calculateCost(convertToInt(rs.getString("Quantity")), convertToDouble(rs.getString("UnitPrice")));
                        vector.add(total);
                        data.add(vector);
                    } else {
                        previousInvoice = rs.getString("InvoiceNo");
                        previousCustomerID = rs.getString("CustomerID");
                        previousCountry = rs.getString("Country");
                        total = total + calculateCost(convertToInt(rs.getString("Quantity")), convertToDouble(rs.getString("UnitPrice")));
                    }
                }
            }
        }

        return new DefaultTableModel(data, columnNames);

    }

    //method which creates the table to store the csv, code adapted from studres
    private static void createTable(Connection connection) throws SQLException {
		//delete the table if it already exists
		Statement statement = connection.createStatement();
		statement.executeUpdate("DROP TABLE IF EXISTS data;");
		//SQL statement to create the new table
		String tableCreation = "CREATE TABLE data (InvoiceNo varchar(10), StockCode varchar(10), Description varchar(50),"
								+ "Quantity varchar(10), InvoiceDate varchar(25), UnitPrice varchar(10), "
								+ "CustomerID varchar(10), Country varchar(50));";

		statement.executeUpdate(tableCreation);
		statement.close();
	}

    //method which converts string into int
    private static int convertToInt(String s) {
        int number = 0;

        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }

    //method which converts string to a double
    private static double convertToDouble(String s) {
        double number = 0.0;

        try {
            number = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }

    //method which calculates the price
    private static double calculateCost(int quantity, double unitPrice) throws SQLException{
        double total = quantity * unitPrice;
        return total;
    }
}
