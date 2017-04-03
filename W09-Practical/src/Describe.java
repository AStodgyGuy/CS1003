import java.io.IOException;
import javax.json.stream.JsonParsingException;

public class Describe {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java -cp .:javax.json-1.0.jar Describe file.json");
            System.exit(0);
        } else {
            try {
                JsonFileReader jfr = new JsonFileReader(args[0]);
                jfr.printSummary();
            } catch (IOException e) {
                System.out.println("404 Error, File not found");
            } catch (JsonParsingException e) {
                System.out.println("Not a valid JSON string!");
            }
        }


    }
}