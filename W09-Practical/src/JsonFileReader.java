import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.Json;
import javax.json.stream.JsonParsingException;
import java.io.IOException;
import java.io.FileReader;
import java.io.StringReader;

public class JsonFileReader {

    private String jsonLocation;

    public JsonFileReader(String fileLocation) {
        this.jsonLocation = fileLocation;
    }

    public void printSummary() throws IOException, JsonParsingException {
        JsonReader reader = Json.createReader(new FileReader(jsonLocation));
        JsonObject mainObject = reader.readObject();

        if(mainObject.containsKey("Heading")) {
            System.out.println(mainObject.get("Heading").toString() + " can refer to:");
            JsonArray relatedTopics = mainObject.getJsonArray("RelatedTopics");
            for (int i = 0; i < relatedTopics.size(); i++) {
                JsonObject aResult = relatedTopics.getJsonObject(i);
                if ((String) aResult.getText("Text").toString().equals(null)) {
                    continue;
                } else {
                    System.out.println(" - " + aResult.get("Text").toString());
                }
            }
        }
    }
}