/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatui;

// IMPORTS
import chatBack.MySocket;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;


/**
 *
 * @author lsadusr11
 */
public class ChatRoom {
    
    static String      appName     = "SwingRoom v0.1";
    ChatRoom           chatRoom;
    static JFrame      newFrame    = new JFrame();
    static JButton     sendMessageButton;
    static JTextField  messageBox;
    static JTextArea   chatBox;
    static JTextField  portChooser;
    static JTextField  hostChooser;
    static JTextField  usernameChooser;
    static JFrame      preFrame;
    
    HashMap<String, Color> clientColors = new HashMap<String, Color>();
    
    static MySocket socket;
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            UIManager.setLookAndFeel(UIManager
                                    .getSystemLookAndFeelClassName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ChatRoom mainGUI = new ChatRoom();
                        mainGUI.preDisplay();
                    }
                });
            }
        });
    }

    public void preDisplay() {
        newFrame.setVisible(false);
        preFrame = new JFrame(appName);
        JPanel prePanel = new JPanel(new GridBagLayout());

        // form
        usernameChooser = new JTextField(15);
        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
        hostChooser = new JTextField(30);
        JLabel chooseHostLabel = new JLabel("Host:");
        portChooser = new JTextField(5);
        JLabel choosePortLabel = new JLabel("Port:");
        JButton enterServer = new JButton("Enter Chat Server");
        enterServer.addActionListener(new EnterServerButtonListener());

        GridBagConstraints preRightConstraints = new GridBagConstraints();
        preRightConstraints.insets = new Insets(0, 0, 0, 10);
        preRightConstraints.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeftConstraints = new GridBagConstraints();
        preLeftConstraints.anchor = GridBagConstraints.WEST;
        preLeftConstraints.insets = new Insets(0, 10, 0, 10);
        preRightConstraints.weightx = 2.0;
        preRightConstraints.fill = GridBagConstraints.HORIZONTAL;
        preRightConstraints.gridwidth = GridBagConstraints.REMAINDER;

        prePanel.add(chooseUsernameLabel, preLeftConstraints);
        prePanel.add(usernameChooser, preRightConstraints);
        prePanel.add(chooseHostLabel, preLeftConstraints);
        prePanel.add(hostChooser, preRightConstraints);
        prePanel.add(choosePortLabel, preLeftConstraints);
        prePanel.add(portChooser, preRightConstraints);
        preFrame.add(BorderLayout.CENTER, prePanel);
        preFrame.add(BorderLayout.SOUTH, enterServer);
        preFrame.setSize(400, 400);
        preFrame.setVisible(true);
        preFrame.setLocationRelativeTo(null);
        
    }

    public void display() {
        newFrame = new JFrame(username);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(new SendMessageButtonListener());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessageButton, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(470, 300);
        newFrame.setVisible(true);
        newFrame.setLocationRelativeTo(null);
    }
    
    

    class SendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (messageBox.getText().length() < 1) {
                // do nothing
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                String newChatLine = "<" + username + ">:  " + messageBox.getText(); 
                chatBox.append(newChatLine
                        + "\n");
                socket.printString(newChatLine);
                messageBox.setText("");
            }
            messageBox.requestFocusInWindow();
        }
    }

    String username;
    String host;
    String port;

    class EnterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            
            username = usernameChooser.getText();
            host = hostChooser.getText();
            port = portChooser.getText();
            
            if (username.length() < 1 || host.length() < 1 || port.length() < 1) {
                popUpErrorMessage("Please fill in the required fields")
            } else {
                try {
                    createClientSocket(host, port);
                    preFrame.setVisible(false);
                    display();
                    startListenning();
                } catch (Exception e) {
                    popUpErrorMessage("Please enter the host and the port in the correct format")
                }
            }
        }
    }
    
    private void createClientSocket(String host, String port) {
        int portInt = Integer.parseInt(port);
        socket = new MySocket(host, portInt);
        socket.printString(username);
    }
    
    private void startListenning(){
        new Thread(){
            public void run() {
                String line;
                while((line = socket.readString()) != null){
                    appendNewMessage(line);
                }
                socket.closeReader();
                socket.closeSocket();
                System.exit(0);
            }
        }.start();
    }

    void popUpErrorMessage(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
        JOptionPane.ERROR_MESSAGE);
    }

    private void appendNewMessage(String line) {
        String nick = getNick(line);
        if (!nick.isEmpty() && nick != null) {
            Color clientColor = clientColors.get(nick);
            if (clientColor != null) {
                // existing client messages
                printMessageWithColor(line + "\n", clientColor);
            } else if (clientColor == null && nick != username) {
                // new client messages
                clientColor = getRandomColor();
                clientsColor.set(nick, clientColor);
                printMessageWithColor(line + "\n", clientColor);
            } else {
                // own messages
                chatBox.append( line + "\n");        
            }
        } else {
            // server messages
            chatBox.append( line + "\n");        
        }
    }
    
    private String getNick(String message) {
        String nick = "";
        if (message != null)
            nick = message.split("<")[1].split(">")[0];
        return nick;
    }

    private void printMessageWithColor(String message, Color color) {
        chatBox.setForeground(color);
        chatBox.append(message + "\n")
        chatBox.setForeground(null);
    }

    Random rand = new Random();
    private Color getRandomColor() {
        float r = rand.nextFloat();
        float g = rand.nextFloat() / 2f;
        float b = rand.nextFloat() / 2f;
        Color randomColor = new Color(r, g, b);
        return randomColor;
    }
}
