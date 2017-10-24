/**
 * This class starts the map reduce process for a given input path and outputs the mapreduce
 * data to the user's specified output path
 * This code was adapted from studres: https://studres.cs.st-andrews.ac.uk/CS1003/Examples/W10-1-Map-Reduce/W10-1-MapReduce/src/WordCount.java
 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.json.stream.JsonParsingException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.LocalJobRunner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;

public class W11Practical {

	public static void main(String[] args) throws IOException, JsonParsingException {

		// This produces an output file in which each line contains a separate word followed by
		// the total number of occurrences of that word in all the input files.
		
		if(args.length < 2) {
			System.out.println("Usage: java -cp " + "\"lib/*:bin\"" + " W11Practical <input_path> <output_path>");
			System.exit(1);
		}

		String input_path = args[0];
		String output_path = args[1];
		
		// Setup new Job and Configuration 
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "W11Practical");
		
		// Specify input and output paths
		FileInputFormat.setInputPaths(job, new Path(input_path));
		FileOutputFormat.setOutputPath(job, new Path(output_path));

		// Set our own MapHashtags as the mapper
		job.setMapperClass(MapHashtags.class);

		// Specify output types produced by mapper (words with count of 1)
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);

		// The output of the reducer is a map from unique words to their total counts.
		job.setReducerClass(ReduceHashtags.class);

		// Specify the output types produced by reducer (words with total counts)
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		try {
			job.waitForCompletion(true);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} catch (JsonParsingException e) {
			System.out.println(e.getMessage());
		}
	}
}
