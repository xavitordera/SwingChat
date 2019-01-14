/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lsadusr11
 */
public class ChatServer {
    static ServerHelper helper = ServerHelper.getInstance(); 
    public static void main(String[] args) {
        if (args.length < 1 || args == null) {
            System.out.println("Looks like you haven't entered a port correctly");
            return;
        }
        int port = -1;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
             System.out.println("Looks like you haven't entered a port correctly");
             return;
        }
        final MyServerSocket serverSocket = new MyServerSocket(port);

        System.out.println("Server listenning at " + port);
        
        while(true){
            final MySocket socket = serverSocket.accept();
            try {
                final String nick = socket.readString();
                System.out.println(nick);

                if (ServerHelper.isNickUsed(nick)) {
                    System.out.println(nick + "is used");
                    socket.closeWriter();
                    socket.closeReader();
                    socket.closeSocket();
                } else {
                
                helper.addNewUser(nick, socket);
                System.out.println("New user "+ nick);
                for(MySocket s: helper.getClientSocketsCollection()){
                    if(s != socket){
                        s.printString(nick + " joined the room, show him some love");
                    }
                }
                
                new Thread(){
                    public void run() {
                        String line;
                        try {
                            while((line = socket.readString()) != null ){
                                for(MySocket s:helper.getClientSocketsCollection()){
                                    if(s != socket){
                                        s.printString(line);
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            // do nothing
                        }
                        
                        for(MySocket s:helper.getClientSocketsCollection()){
                            if(s != socket){
                                s.printString(nick + " left the room");
                            }
                        }
                        System.out.println(nick + " disconnected");
                        
                        socket.closeWriter();
                        socket.closeReader();
                        socket.closeSocket();
                    }
                }.start();
            }} catch (Exception ex) {
                System.out.println("Something went wrong");
                socket.closeSocket();
                break;
            }
        }
    }
}
