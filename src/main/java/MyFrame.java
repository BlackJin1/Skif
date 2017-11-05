import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by knyazev.v on 26.10.2017.
 */
public class MyFrame extends JFrame {
    private JPanel mainPanel = new JPanel();
    private JPanel leftPanel = new JPanel();
    private JPanel rightPanel = new JPanel();
    public MyFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(getFrameSize());
        setLocationRelativeTo(null);
        renderMainPanel();
        setContentPane(mainPanel);
    }
    private Dimension getFrameSize(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.7);
        int height = (int) (screenSize.getHeight() * 0.5);
        return new Dimension(width, height);
    }

    private void renderMainPanel() {
        final int leftWidth = 250;
        Dimension frameSize = getFrameSize();
        Border b = new LineBorder(new Color(71, 164, 255), 1);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
        leftPanel.setPreferredSize(new Dimension(leftWidth, frameSize.height));
        rightPanel.setPreferredSize(new Dimension(frameSize.width - leftWidth, frameSize.height));
        leftPanel.setBorder(b);
        rightPanel.setBorder(b);
        Box mainBox = Box.createHorizontalBox();

        ScrollPane scrollPane = new ScrollPane();
        JTextArea textFieldRight;
        //textFieldRight
        rightPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        textFieldRight = writeRightPanel();
        writeLeftPanel(textFieldRight);
        mainBox.add(leftPanel);
        mainBox.add(rightPanel);
        mainPanel.add(mainBox);
    }

    private void writeLeftPanel(final JTextArea textFieldRight) {
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        JButton jButton1 = new JButton("Выбрать");
        JButton jButton2 = new JButton("Выбрать");
        JButton jButton3 = new JButton("Запустить");
        final JTextField resourcesTextField   = new JFormattedTextField();
        final JTextField resultTextField      = new JFormattedTextField();
        JLabel labelResources           = new JLabel("Ресурсы: ");
        JLabel labelResult              = new JLabel("Результаты: ");

        resourcesTextField.setPreferredSize(new Dimension(240, 20));
        panel1.add(labelResources);
        panel1.add(resourcesTextField);
        panel1.add(jButton1);
        panel1.setLayout(new FlowLayout(FlowLayout.LEADING));

        resultTextField.setPreferredSize(new Dimension(240, 20));
        panel2.add(labelResult);
        panel2.add(resultTextField);
        panel2.add(jButton2);
        panel2.setLayout(new FlowLayout(FlowLayout.LEADING));

        leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));

        //leftPanel.add(labelResources);
        leftPanel.add(panel1);
//        leftPanel.add(jButton1);

       // leftPanel.add(labelResult);
        leftPanel.add(panel2);
        //leftPanel.add(jButton2);

        leftPanel.add(jButton3);

        jButton1.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                //Код, который нужно выполнить при нажатии
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File(".txt"));
                chooser.setDialogTitle("Выбрать файл или директорию");
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    //System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
                    resourcesTextField.setText(""+chooser.getSelectedFile()); // отлавливаем "путь в нашу глобальную переменную
                    // MyData.path = ""+chooser.getSelectedFile(); // при желании -отловим файл
                } else {
                    System.out.println("No Selection ");
                }

            }
        });

        jButton2.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Код, который нужно выполнить при нажатии


                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Выбрать директорию");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//                    System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
                    resultTextField.setText(""+chooser.getSelectedFile()); // отлавливаем "путь в нашу глобальную переменную
//                    // MyData.path = ""+chooser.getSelectedFile(); // при желании -отловим файл
                } else {
                    System.out.println("No Selection ");
                }

            }
        });

        jButton3.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath     = resourcesTextField.getText();
                String dirResult    = resultTextField.getText();
                if (dirResult.equals("")||filePath.equals("")){
                    JOptionPane.showMessageDialog(null,"Выбери файлы и директории блиать!");
                }else {
                    Manager manager = new Manager();
                    manager.setFilePath(filePath);
                    manager.setDirResult(dirResult);
                    manager.processData(filePath);
                    textFieldRight.setText("Обработано "+manager.getCount()+" файлов");
                }

            }
        });

    }

    private JTextArea writeRightPanel() {
        JTextArea jTextArea = new JTextArea( "",29 ,81 );
        JScrollPane scrollPane = new JScrollPane(jTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        rightPanel.add(scrollPane);
        return jTextArea;
    }

}
