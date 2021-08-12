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
    private static final Logger log = Logger.getLogger(IrcThread.class.getName());
            
    private final BlockingQueue<String> in;
    private final BlockingQueue<String> out;
    
    private String url;
    private int port;
    
    private String nick;
    
    private Socket socket;
    private boolean connected = false;
    
    private PrintStream sock_out;
    private BufferedReader sock_in;
        
    public IrcThread(BlockingQueue<String> requests,
                     BlockingQueue<String> replies) {
        this.in = requests;
        this.out = replies;
    }
    
    private void sendSocket(PrintStream ps, String data) {
        log.log(Level.INFO, String.format("SEND : %s", data));
        ps.println(data);
    }
    
    private void processCommand(String command) {
        var strs = command.split(" ");
        
        switch (strs[0]) {
            case "/j":
            case "/join":
                join(strs[1]); // Support multiple channel?
                break;
            default:
                System.err.println("Unknown command!");
        }
    }
    
    
    // Connect to the IRC server and send the NICK message. Then start the recv thread.
    // TODO The NICK and USER messages shoudl be moved out... just lazy for now for testing.
    public void connect(String nick) {
        sendSocket(sock_out, String.format("NICK %s", nick));
        sendSocket(sock_out, String.format("USER %s * * :Jim Bob", nick));   
        this.nick = nick;
        
        // Read thread
        new Thread(() -> {
            while (true) {
                String line;
                try {
                    line = sock_in.readLine();

                    if (line != null) {
                        log.log(Level.INFO, String.format("RECV : ", line));
                        
                        System.out.print(line);
                        
                        String[] split = line.split(" ");
                        System.out.print(String.format(" (%d)", split.length));
                        System.out.println(String.format(" (cmd=%s)", split[1]));
                        
                        
                        if (line.startsWith("PING")) {
                            String[] toks = line.split(" ");
                            sendSocket(sock_out, "PONG " + toks[1]);
                        }
                    }
                } catch (IOException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
    
    // Start the IRC system and connect to the single socket. DOnt return until the socket is fully connected.
    public void start() throws InterruptedException {
        new Thread(() -> {
            log.log(Level.INFO, "In socket thread");
            try {
                // irc.libera.chat:6697 (TLS)
                this.socket = new Socket("irc.eu.libera.chat", 6667);
//                this.socket = new Socket("irc.undernet.org", 6667);
                //this.socket.setSoTimeout(500);
                
                sock_out = new PrintStream(this.socket.getOutputStream());
                sock_in = new BufferedReader(new InputStreamReader( this.socket.getInputStream()));
        
                String line;
                String req;
                while (true) {
                    req = "";
                    try {

                        synchronized(this) {
                            this.connected = true;
                            notifyAll();
                        }

                        req = in.take();
                    } catch (InterruptedException ex) {
                        log.log(Level.SEVERE, null, ex);
                    }

                    if (!req.equals("")) {
                        log.log(Level.INFO, String.format("GOT : %s", req));

                        if (req.startsWith("/")) {
                            // process cmd
                            processCommand(req);
                        } else {
                            // send raw to server
                            sendSocket(sock_out, req);
                        }
                        
                    }
                }
            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
            }
         }).start();
        
        synchronized(this){
            while(!this.connected) {
                wait();
            }
        }
        
        log.log(Level.INFO, "Connected to socket");
    }
    
    // Disconnect from IRC server
    public void disconnect() {
        
    }
    
    // Join an IRC channel
    public void join(String channel) {
        sendSocket(sock_out, String.format("JOIN %s", channel));
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        BlockingQueue<String> requests = new LinkedBlockingQueue<>();
        BlockingQueue<String> replies = new LinkedBlockingQueue<>();
        
        var ircThread = new IrcThread(requests, replies);
        ircThread.start();

        var random = new Random();
        String nick = String.format("test%d", random.nextInt());
        ircThread.connect(nick);
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.print("> ");
            String name = reader.readLine();
            
            if (name.equals("exit")) {
                ircThread.disconnect();
                
                System.out.println("Bye");
                break;
            }
            
            requests.put(name);
        }
    }
}
