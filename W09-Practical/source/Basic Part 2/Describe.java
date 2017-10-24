import java.io.IOException;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;

public class Describe {
    
    public static void main(String[] args) {

        //check for misuse
        if (args.length != 1) {
            System.out.println("Usage: java -cp .:javax.json-1.0.jar Describe file.json/query");
        } else {
            try {
                String input = args[0];
                //if the user enters a json file print out the summary
                if (input.length() > 5 && input.substring(input.length() - 5).equals(".json")) {
                    JsonFileReader jfr = new JsonFileReader(args[0], true);
                    jfr.printSummary();

                //if the user does not enter a json file, query the duckduckgo the api and then print out the summary
                } else {
                    String query = "https://api.duckduckgo.com/?q= " + input + "&format=json";
                    RESTClient rc = new RESTClient();
                    String returnedJson = rc.makeRESTCall(query);
                    JsonFileReader jfr = new JsonFileReader(returnedJson, false);
                    jfr.printSummary();
                }

            } catch (IOException e) {
                System.out.println("404 Error! File not found");
            } catch (JsonParsingException e) {
                System.out.println("Not a valid JSON string!");
            }
        }
    }
}
