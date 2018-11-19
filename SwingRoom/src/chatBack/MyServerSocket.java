/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatBack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lsadusr11
 */
public class MyServerSocket {
    
    ServerSocket socket;

    public MyServerSocket(ServerSocket socket) {
        this.socket = socket;
    }

    public MyServerSocket(int port) {
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    MySocket accept() {
        try {
            return new MySocket(socket.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
}
