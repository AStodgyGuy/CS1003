import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class CSVLoader {

    private Connection connection;
    private String filePath;


    //constructor for csv loader
    public CSVLoader(Connection connection, String filePath) throws IOException, SQLException {
        this.connection = connection;
        this.filePath = filePath;
    }

    public void loadCSVData() throws IOException, SQLException {
        String insertvaluesSQL = "INSERT INTO data"
		+ "(InvoiceNo, StockCode, Description, Quantity, InvoiceDate, UnitPrice, CustomerID, Country)"
		+ " VALUES (?,?,?,?,?,?,?,?)";

        //code to read csv file taken from W03 practical
         BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            String[] informationArray;
            while (line != null) {
                informationArray = line.split(",");
                PreparedStatement preparedStatement = connection.prepareStatement(insertvaluesSQL);
                preparedStatement.setString(1, informationArray[0]);
                preparedStatement.setString(2, informationArray[1]);
                preparedStatement.setString(3, informationArray[2]);
                preparedStatement.setString(4, informationArray[3]);
                preparedStatement.setString(5, informationArray[4]);
                preparedStatement.setString(6, informationArray[5]);
                preparedStatement.setString(7, informationArray[6]);
                preparedStatement.setString(8, informationArray[7]);
                preparedStatement.executeUpdate();
                line = br.readLine();
            }
    }
}
