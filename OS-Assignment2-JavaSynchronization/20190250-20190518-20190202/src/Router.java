import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class Router {
    public boolean[] routerArray;
    public int numberOfConnections;
    public Semaphore connection;
    public int numberOccupied;
    JPanel panel = new JPanel(new GridLayout(0, 1, 10, 5));
    WriteToFile writeToFile = new WriteToFile();
    JFrame frame = new JFrame("Connections");
    Font fieldFont = new Font("Arial", Font.PLAIN, 25);
    public JTextField[] buttons;
    public GridBagConstraints frameConstraints = new GridBagConstraints();

    Router(int n) throws IOException {
        panel.setBorder(LineBorder.createBlackLineBorder());
        numberOfConnections = n;
        connection = new Semaphore(numberOfConnections);
        routerArray = new boolean[numberOfConnections];
        buttons = new JTextField[numberOfConnections];
        this.createButtons();
    }

    public void createButtons() {
        frame.setSize(620, 600);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        for (int i = 0; i < numberOfConnections; i++) {
            buttons[i] = new JTextField();
            buttons[i].setColumns(25);
            buttons[i].setFont(fieldFont);
        }
        int i;
        for (i = 0; i < numberOfConnections; i++) {
            buttons[i].setText("Connection:" + (i + 1));
            buttons[i].setEditable(false);
            buttons[i].setBackground(Color.yellow);
            panel.add(buttons[i]);
            System.out.println("create" + i);
        }
        JScrollPane scrollPane = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        frameConstraints.gridx = 0;
        frameConstraints.gridy = 1;
        frameConstraints.weighty = 1;
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(scrollPane, frameConstraints);
    }

    public synchronized int occupy(Device device) throws InterruptedException {
        for (int i = 0; i < numberOfConnections; i++) {
            if (!routerArray[i]) {
                numberOccupied++;
                device.assignedToConnection = i + 1;
                System.out.println("Connection " + device.assignedToConnection + ": " + device.getName() + " occupied");

                try {
                    buttons[i].setBackground(Color.yellow);
                    buttons[i].setText("Connection " + device.assignedToConnection + ": " + device.getName() + " occupied");
                    writeToFile.print("Connection " + device.assignedToConnection + ": " + device.getName() + " occupied");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                routerArray[i] = true;
                sleep(800);
                break;
            }
        }
        return device.assignedToConnection;
    }

    public void login(int n, Device d) throws InterruptedException {
        System.out.println("Connection " + n + ": " + d.getName() + " login");
        try {
            buttons[n - 1].setBackground(Color.green);
            buttons[n - 1].setText("Connection " + n + ": " + d.getName() + " login");
            writeToFile.print("Connection " + n + ": " + d.getName() + " login");
        } catch (IOException e) {
            e.printStackTrace();
        }

        sleep(800);
    }

    public void activity(int n, Device d) throws InterruptedException {
        System.out.println("Connection " + n + ": " + d.getName() + " performs online activity");
        try {
            buttons[d.assignedToConnection - 1].setBackground(Color.lightGray);
            buttons[n - 1].setText("Connection " + n + ": " + d.getName() + " performs online activity");
            writeToFile.print("Connection " + n + ": " + d.getName() + " performs online activity");
        } catch (IOException e) {
            e.printStackTrace();
        }

        sleep(800);
    }

    public synchronized void logout(Device device) throws InterruptedException {
        numberOccupied--;
        routerArray[device.assignedToConnection - 1] = false;
        notify();
        System.out.println("Connection " + device.assignedToConnection + ": " + device.getName() + " Logged out");

        try {
            buttons[device.assignedToConnection - 1].setBackground(Color.red);
            buttons[device.assignedToConnection - 1].setText("Connection " + device.assignedToConnection + ": " + device.getName() + " Logged out");
            writeToFile.print("Connection " + device.assignedToConnection + ": " + device.getName() + " Logged out");
        } catch (IOException e) {
            e.printStackTrace();
        }
         sleep(800);
    }
}