import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;


public class AddInfoGUI extends JPanel implements ActionListener {

    private final int GUI_WIDTH = 300;
    private final int GUI_HEIGHT = 400;

    private Connection connection;
    private JFrame gui;
    private JLabel invoiceNo, stockCode, description, quantity, invoiceDate, unitPrice, customerID, country;
    private JTextField newInvoiceNo, newStockCode, newDescription, newQuantity, newInvoiceDate, newUnitPrice, newCustomerID, newCountry;
    private JButton addData;

    public AddInfoGUI(Connection connection) {
        //create and show the ADDInfo GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGUI(connection);
            }
        });
    }

    //constructor which constructs the various aspects of the gui
    private AddInfoGUI(Connection connection, Object object) {
        this.connection = connection;

        GridLayout guiGrid = new GridLayout(0,2);
        
        //add all new fields and labels
        invoiceNo = new JLabel("Invoice No:");
        newInvoiceNo = new JTextField();

        stockCode = new JLabel("Stock Code:");
        newStockCode = new JTextField();

        description = new JLabel("Description:");
        newDescription = new JTextField();

        quantity = new JLabel("Quantity:");
        newQuantity = new JTextField();

        invoiceDate = new JLabel("Invoice Date:");
        newInvoiceDate = new JTextField();

        unitPrice = new JLabel("Unit Price:");
        newUnitPrice = new JTextField();

        customerID = new JLabel("Customer ID:");
        newCustomerID = new JTextField();

        country = new JLabel("Country:");
        newCountry = new JTextField();

        //create button which will launch sql query
        addData = new JButton("Add");
        addData.setActionCommand("ADD");
        addData.addActionListener(this);

        //create Jpanel for the buton
        JPanel panel = new JPanel();
        panel.setLayout(guiGrid);

        //add components to grid
        panel.add(invoiceNo);
        panel.add(newInvoiceNo);
        panel.add(stockCode);
        panel.add(newStockCode);
        panel.add(description);
        panel.add(newDescription);
        panel.add(quantity);
        panel.add(newQuantity);
        panel.add(invoiceDate);
        panel.add(newInvoiceDate);
        panel.add(unitPrice);
        panel.add(newUnitPrice);
        panel.add(customerID);
        panel.add(newCustomerID);
        panel.add(country);
        panel.add(newCountry);

        //create JPanel for button
        JPanel panel2 = new JPanel();
        panel2.add(addData);

        //add panel to gui
        add(panel, BorderLayout.PAGE_START);
        //add button panel to gui
        add(panel2, BorderLayout.LINE_END);
        //create borders around panel
        setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
    }

    //this method creates and displays the GUI
    private void createGUI(Connection connection) {
        Object object = null;
        gui = new JFrame("Add Info");
        gui.setSize(GUI_WIDTH, GUI_HEIGHT);

        JComponent contentPane = new AddInfoGUI(connection, object);
        contentPane.setOpaque(true);
        gui.setContentPane(contentPane);

        gui.setTitle("Add Info");
        gui.pack();
        gui.setVisible(true);
    }

    //this method adds the user input data into the database
    public void actionPerformed(ActionEvent event) {
        //when the user clicks the add button
        if (event.getSource() == addData) {
            String insertValuesSQL = "INSERT INTO data"
                + "(InvoiceNo, StockCode, Description, Quantity, InvoiceDate, UnitPrice, CustomerID, Country)"
                + " VALUES (?,?,?,?,?,?,?,?);";
            String[] newInformation = {newInvoiceNo.getText(), newStockCode.getText(), newDescription.getText(), newQuantity.getText(),
                                        newInvoiceDate.getText(), newUnitPrice.getText(), newCustomerID.getText(), newCountry.getText()};

            //code similar to CSVLoader
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(insertValuesSQL);
                for (int i = 1; i < 9; i++) {
                    //check for null input
                    if (!newInformation[i - 1].equals("")) {
                        //check for invalid quantities
                        if (i == 4) {
                            int quantity = convertToInt(newInformation[i - 1]);
                            if (quantity == -1) {
                                throw new NumberFormatException();
                            } else {
                                preparedStatement.setString(i, newInformation[i - 1]);
                            }

                        //check for invalid Price
                        } else if (i == 6) {
                            double unitPrice = convertToDouble(newInformation[i - 1]);
                            if (unitPrice == - 1) {
                                throw new NumberFormatException();
                            } else {
                                preparedStatement.setString(i, newInformation[i - 1]);
                            }

                        } else {
                            preparedStatement.setString(i, newInformation[i - 1]);
                        }
                       
                    } else {
                        throw new NullPointerException();
                    }
                    
                }
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Information added. Please press the refresh button.", "Success", JOptionPane.PLAIN_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Invalid SQL, please try again.", "SQL Error", JOptionPane.WARNING_MESSAGE);
                e.printStackTrace();
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Empty values not allowed, please try again", "Empty Value Error", JOptionPane.WARNING_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Price or Quantity cannot be letters and characters, please try again", "Number Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    //method which converts string to an int
    private int convertToInt(String s) {
        int number = 0;

        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
        return number;
    }

    //method which converts string to a double
    private double convertToDouble(String s) {
        double number = 0.0;

        try {
            number = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return -1;
        }
        return number;
    }
}
