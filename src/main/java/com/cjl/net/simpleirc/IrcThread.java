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
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ciara
 */
public class IrcThread {
    private final BlockingQueue<String> in;
    private final BlockingQueue<String> out;
    
    public IrcThread(BlockingQueue<String> requests,
                     BlockingQueue<String> replies) {
        this.in = requests;
        this.out = replies;
    }
    
    private void sendSocket(PrintStream ps, String data) {
        System.out.println(String.format("SEND : %s", data));
        ps.println(data);
    }
    
    public void start_socket() {
        new Thread(() -> {
            System.out.println("In socket thread");
            Socket socket;
            try {
                socket = new Socket("irc.undernet.org", 6667);
                socket.setSoTimeout(500);
                
                PrintStream sock_out = new PrintStream(socket.getOutputStream());
                BufferedReader sock_in = new BufferedReader(new InputStreamReader( socket.getInputStream()));

                var random = new Random();
                String nick = String.format("test%d", random.nextLong());
                        
                //sock_out.println(String.format("NICK %s", nick)); 
                sendSocket(sock_out, String.format("NICK %s", nick));
                
                //sock_out.println(String.format("USER %s * * :Jim Bob", nick));
                sendSocket(sock_out, String.format("USER %s * * :Jim Bob", nick));

                //sock_out.println("/J #bookz");
                //sendSocket(sock_out, "/J #bookz");
                
                sock_out.println();

                String line;
                String req;
                while (true) {
                    try {
                        //System.out.println("Polling");
                        line = sock_in.readLine();

                        //System.out.println(line);
                        if (line != null) {
                            System.out.println(line);
                            if (line.startsWith("PING")) {
                                String[] toks = line.split(" ");
                                sendSocket(sock_out, "PONG " + toks[1]);
                                //sock_out.println("PONG " + toks[1]);
                            }
                        }

                    } catch(SocketTimeoutException ste) {
                        //System.out.println("In timeout");
                        //System.out.println(String.format("Q isEmpty %s", in.isEmpty())); 
                        if (!in.isEmpty()) {
                            req = "";
                            try {
                                req = in.take();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(IrcThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            if (!req.equals("")) {
                                System.out.println(String.format("GOT : %s", req));
                            
                                sendSocket(sock_out, req);
                            }
                        }
                        /*
                        socket.setSoTimeout(1000);
                        try {
                            req = in.take();
                            if (req != null) {
                                System.out.println(String.format("GOT : %s", req));
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(IrcThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        */
                    }

              
                }
            } catch (IOException ex) {
                Logger.getLogger(IrcThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    
    
     public void start() {
        new Thread(() -> {
            Socket socket;
            try {
                socket = new Socket("eu.undernet.org", 6667);
                PrintStream sock_out = new PrintStream(socket.getOutputStream());
                BufferedReader sock_in = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            } catch (IOException ex) {
                Logger.getLogger(IrcThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            while (true) {
                // TODO: we may want a way to stop the thread
                try {
                    String req = in.take();
                    
                    if (req.equals("/connect")) {
                        //sock_out.println();
                    } else {
                    }
                    
                    
                    
                    out.put(String.format("Hi there %s", req));
                } catch (InterruptedException ie) {
                }
            }
        }).start();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        BlockingQueue<String> requests = new LinkedBlockingQueue<>();
        BlockingQueue<String> replies = new LinkedBlockingQueue<>();
        
        var ircThread = new IrcThread(requests, replies);
        //ircThread.start();
        ircThread.start_socket();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.print("> ");
            String name = reader.readLine();
            
            if (name.equals("exit")) {
                System.out.println("Bye");
                break;
            }
            
            requests.put(name);
            //System.out.println(replies.take());
        }
    }
}
