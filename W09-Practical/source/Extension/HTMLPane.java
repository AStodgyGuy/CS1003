import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.Document;
import javax.json.JsonObject;
import javax.json.JsonArray;
import java.net.URISyntaxException;

public class HTMLPane extends JPanel {

    private final int PANEL_WIDTH = 750;
    private final int PANEL_HEIGHT = 680;

    public HTMLPane(JsonObject returnedObject) {

        //set panel size and colour
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setBackground(Color.WHITE);

        //code taken from : http://alvinalexander.com/blog/post/jfc-swing/how-create-simple-swing-html-viewer-browser-java 

        //add a HTMLEditorKit to the editor pane
        HTMLEditorKit kit = new HTMLEditorKit();
        
        //generate the html string
        String htmlString = generateHTML(returnedObject);

        //add the style sheet for css support
        StyleSheet ss = new StyleSheet();
        ss.importStyleSheet(HTMLPane.class.getResource("style.css"));
        kit.setStyleSheet(ss);

        // create a document, set it on the jeditorpane, then add the html
        Document doc = kit.createDefaultDocument();
        
        //create a JEditorPane
        JEditorPane jEditorPane = new JEditorPane();
        jEditorPane.setEditable(false);
        jEditorPane.setEditorKit(kit);
        jEditorPane.setDocument(doc);
        jEditorPane.setText(htmlString);
        jEditorPane.setCaretPosition(0);

        //JScrollPane
        JScrollPane scrollPane = new JScrollPane(jEditorPane);
        scrollPane.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        //output the html file to show the html file in the folder
        output(htmlString);

        //when the user clicks on a url
        jEditorPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                try {
                    if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        //use desktop application if supported
                        if(Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        //else display error message
                        } else {
                            JOptionPane.showMessageDialog(null, "Your computer does not support this operation.", "Desktop not supported", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } catch (URISyntaxException err) {
                    JOptionPane.showMessageDialog(null, "Whoops! Something went wrong with the website you were trying to access.", "Network Error", JOptionPane.WARNING_MESSAGE);
                } catch (IOException err) {
                    JOptionPane.showMessageDialog(null, "Cannot find a default application to load the website. Please set a default in system settings", "No application found", JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        //add scroll panel to the panel
        add(scrollPane);
        setVisible(true);
    }

    private String generateHTML(JsonObject json) {
        
        String html = "";

        //adding html to the string
        html += "<!DOCTYPE html>";
        html += "\n<html>";

        //link css sheet to the html file
        html += "\n<head>";
        html += "\n<link rel = " + "\"" + "stylesheet" + "\"" + " href=" + "\"" + "style.css" + "\"" + ">";
        html += "\n</head>";
        
        //main body
        html += "\n<body>";

        //main heading
        html += "\n<h1>" + "Search Results for " + json.get("Heading").toString() + "</h1>";
        html += "\n<br>";
        
        //loop all the topics in the jsonarray
        JsonArray relatedTopics = json.getJsonArray("RelatedTopics");
        for (int i = 0; i < relatedTopics.size(); i++) {
            JsonObject aResult = relatedTopics.getJsonObject(i);

            //Result
            if (aResult.containsKey("Result")) {

                //html for the result
                String url = aResult.get("FirstURL").toString();
                String information = aResult.get("Text").toString();
                String topicName = findTopicName(url);

                html += "\n<p id = " + "\"" + "p1" + "\"" + ">" + "<a href=" + "\"" + url + "\"" + ">" + topicName + "</a></p>";
                html += "\n<p id =" + "\"" + "p2" + "\"" + ">" + url + "</p>";
                html += "\n<p id =" + "\"" + "p3" + "\"" + ">" + information + "</p>";
                html += "\n<br>";

            //Topics
            } else if (aResult.containsKey("Topics")) {
                JsonArray topics = aResult.getJsonArray("Topics");
                    for (int j = 0; j < topics.size(); j++) {
                        aResult = topics.getJsonObject(j);

                        //html for the result
                        String url = aResult.get("FirstURL").toString();
                        String information = aResult.get("Text").toString();
                        String topicName = findTopicName(url);

                        html += "\n<p id = " + "\"" + "p1" + "\"" + ">" + "<a href=" + "\"" + url + "\"" + ">" + topicName + "</a></p>";
                        html += "\n<p id =" + "\"" + "p2" + "\"" + ">" + url + "</p>";
                        html += "\n<p id =" + "\"" + "p3" + "\"" + ">" + information + "</p>";
                        html += "\n<br>";  
                    }
            }
        }


        //end the html
        html += "\n</body>";
        html += "\n</html>";


        return html;
    }
    
    //method to find the topic of the url
    private String findTopicName(String url) {
        //url is always 'https://duckduckgo.com' which is 2 characters long
        //the topic follows the last slash

        //url expression to be ommitted
        int urlExpression = 22;

        //obtain the topic name
        String topicName = url.substring(urlExpression);
        String relatedTopics = "/" + "d";

        if (topicName.startsWith(relatedTopics)) {
            topicName = topicName.substring(3);
        }

        //topic name can contain underscores, replace all underscores with spaces
        topicName = topicName.replaceAll("\\W", " ");
        topicName = topicName.replaceAll("_", " ");

        //returm the topic name
        return topicName;
    }

    //method to output html
    private void output(String html) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("output.html"));
            out.write(html);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
