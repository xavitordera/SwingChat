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
    
    public MySocket(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
    
    public String readString(){
        try {
            return reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void printString(String text) {
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
    
    void closeWriter(){
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    /*
    void closeAll() {
        closeReader();
        closeSocket();
        closeWriter();
    }
    */
    
    
}
