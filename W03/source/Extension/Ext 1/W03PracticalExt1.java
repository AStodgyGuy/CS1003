import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.util.ArrayList;

public class W03PracticalExt1 {

    private static final int GUI_WIDTH = 300;
    private static final int GUI_HEIGHT = 400;

    public static void main(String[] args) {

        final JFrame gui = new JFrame(GUI_WIDTH, GUI_HEIGHT);
        final JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files" , "csv");
        JButton openFileButton = new JButton("Open File");
        JPanel panel = new JPanel(new BorderLayout());

        gui.setSize();
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fileChooser.setFileFilter(filter);

        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(gui);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        java.io.File file = fileChooser.getSelectedFile();
                        String path = file.getAbsolutePath();
                        String exportDestination = args[0];
                        CSVHandler handler = new CSVHandler(path);
                        ArrayList<Record> al = handler.getRecordArrayList();   
                        TextWriter tw = new TextWriter(al, exportDestination); 
                        JOptionPane.showMessageDialog(null, "File successfully exported to \n" + exportDestination);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(null, "Not a valid CSV, please choose another file", "Invalid file", JOptionPane.WARNING_MESSAGE);
                    }
        
                }
            }
        });

        panel.add(openFileButton, BorderLayout.CENTER);
        gui.add(panel);
        gui.setTitle("W03 Practical Extension");
        gui.setVisible(true);
    }
}
