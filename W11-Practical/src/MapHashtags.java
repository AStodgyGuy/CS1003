/**
 * This class takes the input file from the user and creates a json object from it
 * The class then proceeds to map all the hastags it finds to a key:value of hashtag:1
 */

import java.io.IOException;
import java.io.StringReader;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.Json;
import javax.json.stream.JsonParsingException;

public class MapHashtags extends Mapper<LongWritable, Text, Text, LongWritable> {
	

	// The output of the mapper is a map from words (including duplicates) to the value 1.

	public void map(LongWritable key, Text value, Context output) throws IOException, InterruptedException, JsonParsingException {

		//read the json object
		String line = value.toString();
		JsonReader reader = Json.createReader(new StringReader(line));
		JsonObject anObject = reader.readObject();

		//check if the object contains the 'entities' field where the hashtags are stored
		if (anObject.containsKey("entities")) {
			anObject = anObject.getJsonObject("entities");
			//obtain the hashtag array
			JsonArray hashtags = anObject.getJsonArray("hashtags");
			//loop through the hashtag array
			for (int i = 0; i < hashtags.size(); i++) {
				//map the hashtag text to 1
				JsonObject aResult = hashtags.getJsonObject(i);
				String hashtagText = aResult.get("text").toString();
				//replace the # symbol
				hashtagText = hashtagText.replace("\"", "");
				//output the file
				output.write(new Text(hashtagText), new LongWritable(1));
			}
		}
	}
}
