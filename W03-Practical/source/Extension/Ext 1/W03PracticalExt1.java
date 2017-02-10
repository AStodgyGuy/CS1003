import javax.swing.*;
import java.awt.*;
import java.beans.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.Random;

public class W03PracticalExt1 extends JPanel implements ActionListener, PropertyChangeListener {

    private static final int GUI_WIDTH = 300;
    private static final int GUI_HEIGHT = 400;

    private JButton openFileButton;
    private JButton cancelButton;
    private JProgressBar progressBar;
    private JTextArea taskOutput;
    private JLabel progressBarText;
    private JFileChooser fileChooser; 
    private Task task;

    /*
     * A class that handles the functionality of the program
     */
    class Task extends SwingWorker<Void, Void> {
        int progress = 0;
        @Override
        public Void doInBackground() {
            Random random = new Random();
            setProgress(0);
            while (progress < 100) {
                try {
                    //whilst each part of the program is being executed a different integer variable from
                    //0 to 20 is generated to increase the progress bar
                    java.io.File file = fileChooser.getSelectedFile();
                    progress += random.nextInt(20);

                    String path = file.getAbsolutePath();
                    progress += random.nextInt(20);

                    String exportDestination = "output.txt";
                    CSVHandler handler = new CSVHandler(path);
                    progress += random.nextInt(20);

                    ArrayList<Record> al = handler.getRecordArrayList();
                    progress += random.nextInt(20);

                    TextWriter tw = new TextWriter(al, exportDestination);
                    setProgress(Math.min(progress, 100));
                    
                    if (progress >= 100 ) {
                        //once progress has reached 100 or above, display message to the user
                        JOptionPane.showMessageDialog(null, "File successfully exported to \n" + exportDestination);
                    }
                //if the user inputs an invalid file an exception is generated and a message is displayed to the user
                } catch (ArrayIndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(null, "Not a valid CSV, please choose another file", "Invalid file", JOptionPane.WARNING_MESSAGE);
                    break;
                }
            }
            return null;
        }

        /*
         * Once the background process is finished, this method executes which provides information
         * on the JTextArea.
         */
        @Override
        public void done() {
            if (progress > 100) {
                //change the cursor from loading to normal
                setCursor(null);
                //re-enabling open file button
                openFileButton.setEnabled(true);
                //output feedback to user
                taskOutput.append("Process Complete\n");
            } else {
                setCursor(null);
                openFileButton.setEnabled(true);
                taskOutput.append("Invalid file type\n");
            }
        }
    }

    /*
     * Create a runnable object that intializes the GUI
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /*
     * This method creates and displays the GUI
    */
    private static void createAndShowGUI() {

        JFrame gui = new JFrame("W03PracticalExt1");
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gui.setSize(GUI_WIDTH, GUI_HEIGHT);

        JComponent contentPane = new W03PracticalExt1();
        contentPane.setOpaque(true);
        gui.setContentPane(contentPane);

        gui.setTitle("W03 Practical Extension");
        gui.pack();
        gui.setVisible(true);
    }

    /*
     * This constructor constructs the various aspects of the gui
    */
    public W03PracticalExt1() {
        super(new BorderLayout());

        //create the open file button
        openFileButton = new JButton("Open File");
        openFileButton.setActionCommand("OPEN");
        openFileButton.addActionListener(this);

        //create JFileChooser
        fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files" , "csv");
        fileChooser.setFileFilter(filter);

        //create progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        //create task output panel
        taskOutput = new JTextArea(10, 40);
        taskOutput.setMargin(new Insets(10,10,10,10));
        taskOutput.setEditable(false);

        //create jlbael for progress bar
        progressBarText = new JLabel("Progress Bar");

        //create jpanel to put everything on
        JPanel panel = new JPanel();
        panel.add(progressBarText);
        panel.add(progressBar);
        panel.add(openFileButton);

        //add the panel to the gui
        add(panel, BorderLayout.PAGE_START);
        //add the scroll panel to the gui
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        //create borders around the panel
        setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

    }

    /*
     * This method detects the action event the user performs and launches the task
     */
    public void actionPerformed(ActionEvent event) {        
        if (event.getSource() == openFileButton) {
            //disable the open file button so that no more files can be input into the program
            openFileButton.setEnabled(false);
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                task = new Task();
                task.addPropertyChangeListener(this);
                //set the cursor to the loading Cursor
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //launch the task
                task.execute();
            }
        }
    }

    /*
     * This method displays the status in the JText area and provides status feedback to the user
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            progressBar.setValue( (Integer) evt.getNewValue());
            //formatted output to show percentage
            taskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
        }
    }
}
