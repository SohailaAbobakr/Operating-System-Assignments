import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI extends Component implements ActionListener {
    private JTextArea nConnections;
    private JTextArea input;
    private JTextArea nDevices;
    JFrame mainFrame = new JFrame("Router");
    JPanel nConnectionsPanel = new JPanel();
    JPanel inputPanel = new JPanel();
    JPanel nDevicesPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();


    public void runProgram() {
        mainFrame.setSize(620, 600);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);
        mainFrame.setLayout(new FlowLayout());
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        nConnections = new JTextArea(2, 2);
        nConnections.setEditable(true);
        nConnectionsPanel.setLayout(new FlowLayout());
        nConnectionsPanel.add(new JLabel("What is the number of WI-FI Connections?"));
        nConnectionsPanel.add(nConnections);

        nDevices = new JTextArea(2, 2);
        nDevices.setEditable(true);
        nDevicesPanel.setLayout(new FlowLayout());
        nDevicesPanel.add(new JLabel("What is the number of devices Clients want to connect?"));
        nDevicesPanel.add(nDevices);

        input = new JTextArea(10, 40);
        input.setEditable(true);
        JScrollPane scrollableInput = new JScrollPane(input);
        scrollableInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollableInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Input"));
        inputPanel.add(scrollableInput);

        buttonsPanel.setLayout(new FlowLayout());
        JButton start = new JButton("Start");
        start.addActionListener(this);
        buttonsPanel.add(start);
        mainFrame.add(nConnectionsPanel, BorderLayout.WEST);
        mainFrame.add(nDevicesPanel, BorderLayout.WEST);
        mainFrame.add(inputPanel, BorderLayout.WEST);
        mainFrame.add(buttonsPanel, BorderLayout.WEST);
        mainFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Start")) {
            try {
                Network network = new Network();
                network.setInput(Integer.parseInt(nConnections.getText()), Integer.parseInt(nDevices.getText()));
                network.setDevices(input.getText());
                network.start();
            } catch (InterruptedException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}