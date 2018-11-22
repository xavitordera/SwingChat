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
    ChatRoom    chatRoom;
    static JFrame      newFrame    = new JFrame();
    static JButton     sendMessage;
    static JTextField  messageBox;
    static JTextArea   chatBox;
    static JTextField  portChooser;
    static JTextField  hostChooser;
    static JTextField  usernameChooser;
    static JFrame      preFrame;
    
    Color clientcolor;
    
    
    
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
    
    private static void createAndShowGUI() {
        
    }

    public void preDisplay() {
        newFrame.setVisible(false);
        preFrame = new JFrame(appName);
        usernameChooser = new JTextField(15);
        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
        hostChooser = new JTextField(30);
        JLabel chooseHostLabel = new JLabel("Host:");
        portChooser = new JTextField(5);
        JLabel choosePortLabel = new JLabel("Port:");
        JButton enterServer = new JButton("Enter Chat Server");
        enterServer.addActionListener(new EnterServerButtonListener());
        JPanel prePanel = new JPanel(new GridBagLayout());
        
        
        GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 10, 0, 10);
        preRight.weightx = 2.0;
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        prePanel.add(chooseUsernameLabel, preLeft);
        prePanel.add(usernameChooser, preRight);
        prePanel.add(chooseHostLabel, preLeft);
        prePanel.add(hostChooser, preRight);
        prePanel.add(choosePortLabel, preLeft);
        prePanel.add(portChooser, preRight);
        preFrame.add(BorderLayout.CENTER, prePanel);
        preFrame.add(BorderLayout.SOUTH, enterServer);
        preFrame.setSize(300, 300);
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

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new SendMessageButtonListener());

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
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(470, 300);
        newFrame.setVisible(true);
        newFrame.setLocationRelativeTo(null);
        
        startListening();
    }
    
    

    class SendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (messageBox.getText().length() < 1) {
                // do nothing
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                
                chatBox.append("<" + username + ">:  " + messageBox.getText()
                        + "\n");
                socket.printString("<" + username + ">:" + messageBox.getText());
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
                System.out.println("No!");
            } else {
                try {
                    createClientSocket(host, port);
                    preFrame.setVisible(false);
                    display();
                } catch (Exception e) {
                    System.out.println("hey write a number mdfk");
                }
                
                
            }
        }
    }
    
    private void createClientSocket(String host, String port) {
        int portInt = Integer.parseInt(port);
        socket = new MySocket(host, portInt);
        socket.printString(username);
    }
    
    void startListening(){
        new Thread(){
            public void run() {
                String line;
                while((line = socket.readString()) != null){
                    chatBox.append( line + "\n");
                }
                System.out.println("Client disconnected...");
                socket.closeReader();
                socket.closeSocket();
                System.exit(0);
            }
        }.start();
    }
    
}
