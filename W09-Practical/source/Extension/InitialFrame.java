import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.json.JsonObject;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.json.stream.JsonParsingException;

public class InitialFrame extends JFrame implements ActionListener {

    private final int GUI_WIDTH = 800;
    private final int GUI_HEIGHT = 800;

    private final int TITLE_FONT_SIZE = 90;
    private final int USER_FONT_SIZE = 20;
    private final int USER_INPUBOX_WIDTH = 500;
    private final int USER_INPUBOX_HEIGHT = 30;

    private JPanel panel;
    private JLabel titleLabel;
    private JTextField userText;
    private JButton searchButton, inputFile;

    //constructor for InitialFrame
    public InitialFrame() {
        createAndShowGUI();
    }

    //method which adds all the components to the frame and shows the frame
    public void createAndShowGUI() {

        //frame settings
        setTitle("W09PracticalExt");
        setResizable(false);
        setSize(GUI_WIDTH, GUI_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        //create box
        Box box = Box.createVerticalBox();
        box.setOpaque(true);
        box.setBackground(Color.WHITE);

        //create panel
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        //Title - W09 Practical
        String htmlTitle = "<html><font color=#4885ed>W</font><font color=#db3236>0</font><font color=#f4c20d>9</font><font color=#4885ed>E</font><font color=#3cba54>x</font><font color=#db3236>t</font>";
        titleLabel = new JLabel(htmlTitle, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Futura", Font.PLAIN, TITLE_FONT_SIZE));
        panel.add(titleLabel);
        box.add(panel);

        //TextField - UserInput
        userText = new JTextField("");
        userText.setFont(new Font("Comic Sans MS", Font.PLAIN, USER_FONT_SIZE));
        userText.setMaximumSize( new Dimension(USER_INPUBOX_WIDTH, USER_INPUBOX_HEIGHT));
        userText.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(userText);

        //create new panel for side by side buttons
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);

        //Button - Search
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(searchButton);

        //Button - InputFile
        inputFile = new JButton("Add File");
        inputFile.addActionListener(this);
        inputFile.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(inputFile);

        //add panel to box
        box.add(panel);
        
        //add panel to frame and display it
        add(box, BorderLayout.NORTH);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {

        //user clicks input file button
        if (event.getSource() == inputFile) {
            try {
                //jfile file chooser
                JFileChooser fileChooser = new JFileChooser();
                //set filters for the jfile chooser
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files" , "json");
                fileChooser.setFileFilter(filter);
                //display the dialog for JFileChooser
                int returnValue = fileChooser.showOpenDialog(this);

                //user selects a valid file
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        java.io.File file = fileChooser.getSelectedFile();
                        JsonFileReader jfr = new JsonFileReader(file.getAbsolutePath(), true);
                        JsonObject returnedJsonObject = jfr.getJsonObject();
                        displayResultFrame(returnedJsonObject);
                    } catch (NullPointerException e) {
                        JOptionPane.showMessageDialog(null, "JSON file contents not supported, please try another file", "JSON Content Error", JOptionPane.WARNING_MESSAGE);
                    }

                //user does not select a valid file
                } else if (returnValue == JFileChooser.CANCEL_OPTION) {
                    //do nothing
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid file, please try another file", "Invalid File Error", JOptionPane.WARNING_MESSAGE);
                }               
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Invalid file, please try another file", "Invalid File Error", JOptionPane.WARNING_MESSAGE);
            } catch (JsonParsingException e) {
                JOptionPane.showMessageDialog(null, "Not valid .json file, please try again. ", "Unsupported json file" , JOptionPane.WARNING_MESSAGE);
            }

        //user clicks search button
        } else if (event.getSource() == searchButton) {
            try {
                //display error message when the user tries to search for nothing
                if (userText.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Invalid input, please try again.", "Null Value Error", JOptionPane.WARNING_MESSAGE);

                //use api to search for the user search term
                } else {
                    String query = "https://api.duckduckgo.com/?q= " + userText.getText() + "&format=json";
                    RESTClient rc = new RESTClient();
                    String returnedJson = rc.makeRESTCall(query);
                    JsonFileReader jfr = new JsonFileReader(returnedJson, false);
                    JsonObject returnedJsonObject = jfr.getJsonObject();
                    displayResultFrame(returnedJsonObject);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Invalid file, please try another file", "Invalid File Error", JOptionPane.WARNING_MESSAGE);
            } catch (JsonParsingException e) {
                JOptionPane.showMessageDialog(null, "Not valid .json file please try again. ", "Unsupported json file" , JOptionPane.WARNING_MESSAGE);
            }
            

        }
    }

    //displays panel once the user clicks search
    private void displayResultFrame(JsonObject json) throws NullPointerException {

        //removes all current components on the frame
        getContentPane().removeAll();

        //create new panel to contain two panels
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);

        //create new panel for side by side buttons
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        //Title - W09 Practical
        String htmlTitle = "<html><font color=#4885ed>W</font><font color=#db3236>0</font><font color=#f4c20d>9</font><font color=#4885ed>E</font><font color=#3cba54>x</font><font color=#db3236>t</font>";
        titleLabel = new JLabel(htmlTitle);
        titleLabel.setFont(new Font("Futura", Font.PLAIN, 35));
        //when user clicks on the label, this will return back to the initial state
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getContentPane().removeAll();
                createAndShowGUI();
                repaint();
                revalidate();
                
            }
        });
        panel.add(titleLabel);

        //TextField - UserInput
        userText = new JTextField("");
        userText.setFont(new Font("Comic Sans MS", Font.PLAIN, USER_FONT_SIZE));
        userText.setPreferredSize( new Dimension(250, 25));
        panel.add(userText);

        //Button - Search
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        panel.add(searchButton);

        //Button - InputFile
        inputFile = new JButton("Add File");
        inputFile.addActionListener(this);
        panel.add(inputFile);

        //make panel visible
        panel.setVisible(true);

        //add panel to container
        container.add(panel);

        //add the html pane to the container
        HTMLPane hp = new HTMLPane(json);
        container.add(hp);

        //make container visible
        container.setVisible(true);

        //add container to the frame
        add(container);

        //set frame to visible
        setVisible(true);
        
    }
}
