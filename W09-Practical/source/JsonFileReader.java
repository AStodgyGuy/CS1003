import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.Json;
import javax.json.stream.JsonParsingException;
import java.io.IOException;
import java.io.FileReader;
import java.io.StringReader;

public class JsonFileReader {

    private JsonObject mainObject;

    //constructor for JsonFileReader for file location
    public JsonFileReader(String userInput) throws IOException {
        JsonReader reader = Json.createReader(new FileReader(userInput));
        this.mainObject = reader.readObject();
        reader.close();
    }

    //method that prints out the JSon
    public void printSummary() throws JsonParsingException {
        //check for heading
        if (mainObject.containsKey("Heading")) {
            //print heading
            System.out.println(mainObject.get("Heading").toString() + " can refer to:");
                             
            //obtain related topics array
            try {
                 JsonArray relatedTopics = mainObject.getJsonArray("RelatedTopics");
                             //loop through all returned objects in json array
                    for (int i = 0; i < relatedTopics.size(); i++) {
                        JsonObject aResult = relatedTopics.getJsonObject(i);
                        //Result
                        if (aResult.containsKey("Result")) {
                            aResult = relatedTopics.getJsonObject(i);
                            //obtain the text and print it out
                            try {
                                aResult.get("Text");
                                System.out.println("  - " + aResult.get("Text").toString());
                            //continue to next iteration in loop if there is nothing in Text
                            } catch (NullPointerException e) {
                                continue;
                            }

                        //Topics array
                        } else if (aResult.containsKey("Topics")) {
                            JsonArray topics = aResult.getJsonArray("Topics");
                            //check that the array is not empty
                            if (topics.size() > 0) {
                                //obtain the category name and print it out
                                System.out.println("  * Category: " + aResult.get("Name"));
                                //print out each result in the array
                                for (int j = 0; j < topics.size(); j++) {
                                    aResult = topics.getJsonObject(j);
                                    try {
                                        aResult.get("Text");
                                        System.out.println("    - " + aResult.get("Text").toString());
                                    //if the text is empty continue to next result in the Topic array
                                    } catch (NullPointerException e) {
                                        continue;
                                    }
                                }
                            }

                        //if key does not contain Topics or Result, continue to next object
                        } else {
                            continue;
                        }
                    }
            } catch (NullPointerException e) {
                System.out.println("Not a JSON in valid format!");
                return;
            }
        //if heading does not exist print error message
        } else {
            System.out.println("No heading found in .json");
        }
    }
}
