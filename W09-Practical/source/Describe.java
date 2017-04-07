import java.io.IOException;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;

public class Describe {
    public static void main(String[] args) {

        //check for misuse
        if (args.length != 1) {
            System.out.println("Usage: java -cp .:javax.json-1.0.jar Describe file.json");
        } else {
            try {
                String input = args[0];
                JsonFileReader jfr = new JsonFileReader(args[0]);
                jfr.printSummary();
                
            } catch (IOException e) {
                System.out.println("404 Error! File not found");
            } catch (JsonParsingException e) {
                System.out.println("Not a valid JSON string!");
            }
        }
    }
}
