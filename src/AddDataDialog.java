import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddDataDialog extends JDialog {
    JPanel mainPanel;
    JButton exitButton;
    JButton addStudentButton;
    JTextArea idArea;
    JTextArea punctArea;
    JTextArea otprArea;
    JTextArea pribArea;
    JTextArea timeDoArea;
    JTextArea timePoArea;
    public static String time1;
    public static String time;


    AddDataDialog(WindowApp owner){
        super(owner,"Add trains", true);
        //main panel
        mainPanel=new JPanel();
        mainPanel.setLayout(new GridLayout(14,1));
        //text areas
        mainPanel.add(new JLabel("id"));
        idArea =new JTextArea();
        mainPanel.add(idArea);
        mainPanel.add(new JLabel("punct"));
        punctArea =new JTextArea();
        mainPanel.add(punctArea);
        mainPanel.add(new JLabel("otpr"));
        otprArea =new JTextArea();
        mainPanel.add(otprArea);
        mainPanel.add(new JLabel("prib"));
        pribArea =new JTextArea();
        mainPanel.add(pribArea);
        mainPanel.add(new JLabel("timePo"));
        timePoArea =new JTextArea();
        mainPanel.add(timePoArea);
        mainPanel.add(new JLabel("timeDo"));
        timeDoArea =new JTextArea();
        mainPanel.add(timeDoArea);
        //buttons
        addStudentButton=new JButton("Add train");
        mainPanel.add(addStudentButton);
        exitButton=new JButton("Close");
        mainPanel.add(exitButton);
        //window
        setResizable(false);
        setContentPane(mainPanel);
        pack();
        //listeners
        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idArea.getText());
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-M HH:mm:ss", Locale.ENGLISH);
                    formatter.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

                    String punct = punctArea.getText();
                    Date otpr = formatter.parse(otprArea.getText());
                    Date prib = formatter.parse(pribArea.getText());

//                    if (group < 1 || group > 4) {
//                        JOptionPane.showMessageDialog(AddDataDialog.this,"Check input format.", "", JOptionPane.WARNING_MESSAGE);
//                        return;
//                    }
//                    if (surname.length() < 1 || surname.length() > 15) {
//                        JOptionPane.showMessageDialog(AddDataDialog.this, "Check input format.", "", JOptionPane.WARNING_MESSAGE);
//                        return;
//                    }
//                    if (progMark < 1 || progMark > 10 || algMark < 1 || algMark > 10 || mathMark < 1 || mathMark > 10) {
//                        JOptionPane.showMessageDialog(AddDataDialog.this, "Check input format.", "", JOptionPane.WARNING_MESSAGE);
//                        return;
//                    }
                    owner.getListOfTrains().add(new Train(id, punct, otpr, prib));
                    owner.updateTableModel();

                } catch (IllegalArgumentException | ParseException exception) {
                    JOptionPane.showMessageDialog(AddDataDialog.this,"Check input format.", "", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }
}