import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CSVLoader {

    private Connection connection;
    private String filePath;


    //constructor for csv loader
    public CSVLoader(Connection connection, String filePath) throws IOException, SQLException {
        this.connection = connection;
        this.filePath = filePath;
    }

    //method to load csv into data table
    public void loadCSVData() throws IOException, SQLException {
        PreparedStatement preparedStatement = null;
        //SQL statement to load csv information into data table
        String insertValuesSQL = "INSERT INTO data"
        + "(InvoiceNo, StockCode, Description, Quantity, InvoiceDate, UnitPrice, CustomerID, Country)"
        + " VALUES (?,?,?,?,?,?,?,?)";

        //code to read csv file taken from W03 practical
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line = br.readLine();
        String[] informationArray;
        while (line != null) {
            informationArray = line.split(",");
            preparedStatement = connection.prepareStatement(insertValuesSQL);
            //the first '?' corresponds to the 0th term in the information array
            //the second '?' corresponds to the 1st term in the information array
            for (int i = 1; i < 9; i++) {
                preparedStatement.setString(i, informationArray[i - 1]);
            }
            preparedStatement.executeUpdate();
            line = br.readLine();
        }
    }
}
