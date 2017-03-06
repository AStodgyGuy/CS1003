import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class W07PracticalExt extends JPanel implements ActionListener {

    private static Connection connection;
    private static final int GUI_WIDTH = 1100;
    private static final int GUI_HEIGHT = 600;
    private static final int PROPERTIES_PATH = 0;
	private static final int CSV_PATH = 1;
    private static final int NUMBER_OF_ARGS = 2;

    private JLabel invoiceNoText;
    private JTextField invoiceNoSearch;
    private JButton launchSearch;
    private JButton launchAdd;
    private JButton launchUpdate;
    private JTable table;
    private static JFrame gui;

    public static void main(String[] args) throws SQLException {

        try {
            PropertyLoader pl = new PropertyLoader(args[PROPERTIES_PATH]);
            connection = DriverManager.getConnection(pl.getDBUrl(), pl.getUserName(), pl.getPassword());
            //if the args contains only the properties file, do not create new table
            if (args.length == 1) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        createAndShowGUI();
                    }
                });
            //if the args contains both properties file and csv, create a new table
            } else if (args.length == NUMBER_OF_ARGS) {
                createTable(connection);
                CSVLoader csvLoader = new CSVLoader(connection, args[CSV_PATH]);
                csvLoader.loadCSVData();
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        createAndShowGUI();
                    }
                });  
            } else {
                System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file>");
            }
        } catch (IOException e) {
            System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file>");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Usage: java -cp <mariadb-client.jar>:. W07Practical <DB_properties_file> <input_file>");
            e.printStackTrace();
        }
    }

    //this method creates and displays the GUI
    private static void createAndShowGUI() {

        gui = new JFrame("W07PracticalExt");
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gui.setSize(GUI_WIDTH, GUI_HEIGHT);

        JComponent contentPane = new W07PracticalExt();
        contentPane.setOpaque(true);
        gui.setContentPane(contentPane);

        gui.setTitle("W07 Practical Extension");
        gui.setVisible(true);
    }

    //constructor which constructs the various aspects of the gui
    public W07PracticalExt() {
        super(new BorderLayout());

        //create Jbutton to launch search
        launchSearch = new JButton("Search");
        launchSearch.setActionCommand("SEARCH");
        launchSearch.addActionListener(this);

        //create jbutton to launch add
        launchAdd = new JButton("Add");
        launchAdd.setActionCommand("ADD");
        launchAdd.addActionListener(this);

        //create update button
        launchUpdate = new JButton("Update");
        launchUpdate.setActionCommand("UPDATE");
        launchUpdate.addActionListener(this);

        //add table object to the gui
        try {
            Statement statement = null;
            String query = "SELECT * FROM data";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            DefaultTableModel model = buildTableModel(rs);
            table = new JTable(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //create jpanel to put everything on
        JPanel panel = new JPanel();
        panel.add(launchAdd);
        panel.add(launchSearch);
        panel.add(launchUpdate);

        //add the panel to the gui
        add(panel, BorderLayout.PAGE_START);
        //add the scroll panel to the gui
        add(new JScrollPane(table), BorderLayout.CENTER);
        //create borders around the panel
        setBorder(BorderFactory.createEmptyBorder(35,35,35,35));
    }

    //this method detects the action event the user performs and launches the relevant task
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == launchAdd) {
            AddInfoGUI aig = new AddInfoGUI(connection);
        } else if (event.getSource() == launchSearch) {
            String[] possibilities = {"InvoiceNo" , "StockCode", "CustomerID", "Country"};
            String column = (String)JOptionPane.showInputDialog(null, "Choose column to filter by:", "Choose column", JOptionPane.PLAIN_MESSAGE, null, possibilities, "InvoiceNo");
            String value = (String)JOptionPane.showInputDialog(null, "Enter a(n) " + column + " value:", "Choose value", JOptionPane.PLAIN_MESSAGE);
            String query = "SELECT * FROM data WHERE " + column + " LIKE " + value + ";";
            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                table.setModel(buildTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Invalid SQL please try again.", "SQL Error", JOptionPane.WARNING_MESSAGE);
                e.printStackTrace();
            }
        } else if (event.getSource() == launchUpdate) {
            try {
                String query = "SELECT * FROM data";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                table.setModel(buildTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Invalid SQL please try again.", "SQL Error", JOptionPane.WARNING_MESSAGE);
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
}
