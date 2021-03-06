/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author lsadusr11
 */
public class MySocket {
    
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    
    public MySocket(String host, int port) throws Exception{
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public MySocket(Socket socket) {
        try {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public String readString() throws Exception{
        return reader.readLine();
    }
    
    public void printString(String text) {
        try {
            writer.println(text);
        } catch (Exception e) {
        }
    }
    
    public void printStringWithColor(String text) {
        try {
            writer.println(text);
        } catch (Exception e) {
        }
    }
    
    
    public void closeReader() {
        try {
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void closeSocket() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void closeWriter(){
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    public boolean isClosed() {
        return socket.isClosed();
    }

    public boolean isSocketClosed() throws Exception{
        return socket.getInputStream().read() == -1;
    }

    public boolean isConnected() {
        return socket.isConnected();
    }
    /*
    void closeAll() {
        closeReader();
        closeSocket();
        closeWriter();
    }
    */
    
    
}
