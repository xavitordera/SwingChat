/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lsadusr11
 */
public class ChatClient {
    private static MySocket socket;
    
    public ChatClient(String host, int port) {
        new MySocket(host, port);
    }
    
    
    
    
    public static void main(String[] args) {
        final MySocket socket = new MySocket(args[0], Integer.parseInt(args[1]));
        final String nick = args[2];
        
        new Thread(){
            public void run() {
                socket.printString(nick);
                while(true){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String line = null;
                    try {
                        while((line = reader.readLine()) != null){
                            socket.printString(nick + "> " + line);
                        }
                        socket.closeWriter();
                    } catch (IOException ex) {
                        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
        
        new Thread(){
            public void run() {
                String line;
                while((line = socket.readString()) != null){
                    System.out.println("\t\t\t\t" + line);
                }
                System.out.println("Client disconnected...");
                socket.closeReader();
                socket.closeSocket();
                System.exit(0);
            }
        }.start();
    }
}
