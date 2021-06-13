/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.net.simpleirc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class IrcProcess {
    

}


/**
 *
 * @author ciara
 */
public class Irc {
    //private Socket socket;
    
    private void processLine(String line) {
        
    }
    
    private void sendCmd(PrintStream out, String cmd) {
        System.out.println(String.format(" SENDING : %s", cmd));
        out.println(cmd);
    }
    
    
    public String Ping() {
        return "PONG";
    }
    
    public void connect(String server, int port) {
        try {
            try (Socket socket = new Socket(server, port)) {
                PrintStream out = new PrintStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader( socket.getInputStream()));
                
    
                //USER
                //<username> <hostname> <servername> <realname>
                sendCmd(out, "NICK xyzzy123");
                sendCmd(out, "USER xyzzy123 * * :Jim Bob");
                out.println();


                String line;
                while (true) {
                    line = in.readLine();   
                    if (line != null) {
                        System.out.println(line);
                        if (line.startsWith("PING")) {
                            String[] toks = line.split(" ");
                            sendCmd(out, "PONG " + toks[1]);
                        }
                    }
                }
                
                /*
                String line = in.readLine();
                while( line != null )
                {
                    System.out.println( line );
                    line = in.readLine();
                }
                */
                //in.close();
                //out.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Irc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void main(String[] args) {
        //var ircClient = new Irc();
        
        //ircClient.connect("eu.undernet.org", 6667);
    }
}
