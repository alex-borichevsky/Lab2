import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class WindowApp extends JFrame{
    private final static int WINDOW_X = 300;
    private final static int WINDOW_Y = 700;
    public static final Object[] tableHeader = new String[]{"id","punct","otpr","prib"};

    JMenu menu;
    JMenu menuTrains;
    JMenuBar menuBar;
    JMenuItem menuItemAddTrain;
    JMenuItem menuItemSaveAsBinary;
    JMenuItem menuItemOpenBinary;
    JMenuItem menuItemCheckTrains;

    JScrollPane scrollPane;


    JPanel mainPanel;
    JPanel northPanel;
    JButton addDataButton;
    JButton deleteDataButton;

    private JTable table;

    ArrayList<Train> trainArrayList;

    ArrayList<Train> debtTrains = new ArrayList<>();

    public ArrayList<Train> getListOfTrains() {

        return trainArrayList;
    }

    WindowApp(){
        //panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        //buttons
        addDataButton = new JButton("Add new trains...");
        deleteDataButton = new JButton("Delete selected train");
        //
        northPanel = new JPanel(new GridLayout(1,2));
        northPanel.add(addDataButton);
        northPanel.add(deleteDataButton);
        mainPanel.add(northPanel, BorderLayout.NORTH);

        //model
        trainArrayList = new ArrayList<>();
        //table
        table = new JTable();
        scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true); //таблица заполняет всю высоту обзора
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        //add header
        DefaultTableModel model = new DefaultTableModel(tableHeader,0);
        table.setModel(model);
        //menu bar
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menuTrains = new JMenu(("Trains"));
        menuItemAddTrain = new JMenuItem("Add train...");
        menuItemCheckTrains = new JMenuItem("Check Trains...");
        menuItemOpenBinary = new JMenuItem("Open file...");
        menuItemSaveAsBinary = new JMenuItem("Save as...");
        menuTrains.add(menuItemAddTrain);
        menuTrains.add(menuItemCheckTrains);
        menu.add(menuItemOpenBinary);
        menu.addSeparator();
        menu.add(menuItemSaveAsBinary);
        menuBar.add(menu);
        menuBar.add(menuTrains);

        //window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(mainPanel);
        setJMenuBar(menuBar);
        setPreferredSize(new Dimension(2*WINDOW_X,WINDOW_Y));
        pack();
        //listeners
        addDataButton.addActionListener(new AddDataListener());
        menuItemAddTrain.addActionListener(new AddDataListener());

        //сериализация - перевод в послед байтов
        //для восстановления объекта: упаковываем InputStream в ObjectInputStream и вызываем readObject();
        menuItemOpenBinary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //parse binary
                JFileChooser fileOpen = new JFileChooser();
                int ret = fileOpen.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileOpen.getSelectedFile();
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        ObjectInputStream oin = new ObjectInputStream(fis);
                        trainArrayList = (ArrayList<Train>) oin.readObject();
                        updateTableModel();
                    }
                    catch (ClassNotFoundException exception){
                        JOptionPane.showMessageDialog(WindowApp.this,
                                "Cannot open the file. Class not found.", "", JOptionPane.WARNING_MESSAGE);
                    }
                    catch (IOException exception){
                        JOptionPane.showMessageDialog(WindowApp.this, "Cannot open the file.", "", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        //FileOutputStream  - для записи байтов в файл
        //создаем выходной поток OutputStream, упаковываем в  ObjectOutputStream и вызываем метод writeObject
        menuItemSaveAsBinary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);//выходной поток для объектов
                        oos.writeObject(trainArrayList);
                        oos.flush();
                        oos.close();
                        fos.close();
                    }
                    catch (IOException exception){
                        System.out.println(exception.getMessage());
                    }
                }
            }
        });

        deleteDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int[] rows = table.getSelectedRows();
                for(int i=0;i<rows.length;i++){
                    //потому что после удаления индексы в модели смещаются
                    model.removeRow(rows[i]-i);
                    trainArrayList.remove(rows[i]-i);
                }
            }
        });

        menuItemCheckTrains.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (Train student : trainArrayList){
                    System.out.println(student);
                    try {
                        System.out.println(student.isDebt());
                        if (student.isDebt()){
                            debtTrains.add(student);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                showDebtTrains();

            }
        });




    }

    class AddDataListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddDataDialog dialog = new AddDataDialog(WindowApp.this);
            dialog.setVisible(true);
        }
    }

    public void updateTableModel() {
        DefaultTableModel model = new DefaultTableModel(tableHeader,0);
        Collections.sort(trainArrayList, new MyComparator());
        Iterator<Train> iterator = trainArrayList.iterator();
        Train train;
        while (iterator.hasNext()) {
            train=iterator.next();
            model.addRow(new Object[]{train.getId(),train.getPunct(),train.getOtpr(),train.getPrib()});
        }
        table.setModel(model);
    }

    public void showDebtTrains(){
        DefaultTableModel model = new DefaultTableModel(tableHeader,0);
        Collections.sort(debtTrains, new MyComparator());
        Iterator<Train> iterator = debtTrains.iterator();
        Train train;
        while (iterator.hasNext()) {
            train=iterator.next();
            model.addRow(new Object[]{train.getId(),train.getPunct(),train.getOtpr(),train.getPrib()});
        }
        table.setModel(model);
    }
}
