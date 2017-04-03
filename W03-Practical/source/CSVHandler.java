import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVHandler {

    private ArrayList<Record> al = new ArrayList<Record>();
    private String[] informationArray;

    /*
        This constructor obtains the specified file path and reads every line of that file. It then splits
        line by commas and puts them into a string array. The string array is then passed into the Record class
        where the comma-split words is turned into a record. The record is then added to an arraylist of records.
    */
    public CSVHandler(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                    informationArray = line.split(","); //split the line by commas to obtain a string array
                    Record aRecord = new Record(informationArray); //pass the array to the Record class
                    al.add(aRecord); //add the Record to the array
                    line = br.readLine(); //read the next line
            }

        } catch (IOException e) {
            System.out.println("File not found error!");
        }
    }

    //method which returns the ArrayList of records
    public ArrayList<Record> getRecordArrayList() {
        return al;
    }
}
