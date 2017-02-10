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

    class Task extends SwingWorker<Void, Void> {
        int progress = 0;
        @Override
        public Void doInBackground() {
            Random random = new Random();
            setProgress(0);
            while (progress < 100) {
                try {
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
                    if (progress > 100 ) {
                        JOptionPane.showMessageDialog(null, "File successfully exported to \n" + exportDestination);
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(null, "Not a valid CSV, please choose another file", "Invalid file", JOptionPane.WARNING_MESSAGE);
                    break;
                }
            }
            return null;
        }

        @Override
        public void done() {
            if (progress > 100) {
                setCursor(null);
                openFileButton.setEnabled(true);
                taskOutput.append("Process Complete\n");
            } else {
                setCursor(null);
                openFileButton.setEnabled(true);
                taskOutput.append("Invalid file type\n");
            }
        }
    }

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

        JPanel panel = new JPanel();
        panel.add(progressBarText);
        panel.add(progressBar);
        panel.add(openFileButton);
        
        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

    }

    public void actionPerformed(ActionEvent event) {        
        if (event.getSource() == openFileButton) {
            openFileButton.setEnabled(false);
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                task = new Task();
                task.addPropertyChangeListener(this);
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                task.execute();
            }
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            progressBar.setValue((Integer)evt.getNewValue());
            taskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
        }
    }
}
