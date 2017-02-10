import java.io.IOException;

import java.util.ArrayList;

public class W03Practical {
    public static void main(String[] args) {
        try {

            //input path
            String path = args[0];

            //export destination
            String exportDestination = args[1];

            //pass the path of the file to the csv handler
            CSVHandler handler = new CSVHandler(path);

            //obtain ArrayList of Records
            ArrayList<Record> al = handler.getRecordArrayList();

            //pass the arraylist into a textformatter
            TextWriter tw = new TextWriter(al, exportDestination);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java W03Practical <input_file> <output_file>");
        } //catch (IndexOutOfBoundsException e) {
           // System.out.println("Usage: java W03Practical <input_file> <output_file>");
       // }
    }
}
