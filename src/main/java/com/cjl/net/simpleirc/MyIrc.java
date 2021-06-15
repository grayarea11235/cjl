/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.net.simpleirc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ciara
 */
public class MyIrc {
    private final String address;
    private final int port;
    
    private BlockingQueue<String> in_queue = new LinkedBlockingQueue<>();
    private BlockingQueue<String> out_queue = new LinkedBlockingQueue<>();
    
    public MyIrc(   String address, 
                    int port, 
                    BlockingQueue<String> requests,
                    BlockingQueue<String> replies) {
        this.address = address;
        this.port = port;
        
        this.in_queue = requests;
        this.out_queue = replies;    
    }
    
    // This is the TCP thread
    public void start() {
        new Thread( () -> {
            try {
               var client = AsynchronousSocketChannel.open();
               var hostAddress = new InetSocketAddress("eu.undernet.org", 6667);
                
                
                String req;
                try {
                    while (true) {
                        req = in_queue.take();
                        if (req != null) {
                            System.out.println(req);
                            
                            if (req.toLowerCase().equals("connect")) {
                                // This has to complete sync before we start a sending
//                                Future<Void> future = client.connect(hostAddress);

                                var nick = String.format("test%d", new Random().nextInt());
                                var sendStr = String.format("NICK %s", nick);
                            
                                var bb = ByteBuffer.wrap(sendStr.getBytes());
                                    
                                client.write(bb, client,  new CompletionHandler<Integer, AsynchronousSocketChannel >() {
                                    @Override
                                    public void completed(Integer result, AsynchronousSocketChannel channel ) {
                                        //after message written
                                        //NOTHING TO DO
                                    }

                                    @Override
                                    public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                                        System.out.println( "Fail to write the message to server");
                                    }
                                });
                                
                            }
                        }
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(IrcThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            } catch (IOException ex) {
                Logger.getLogger(MyIrc.class.getName()).log(Level.SEVERE, null, ex);
            }
            
                
        }).start();
    }
    
    public void connect(String nick) {
        
    }
    
    public void join(String channel) {
        
    }
    
    
    public static void main(String[] args) throws IOException, InterruptedException {
        BlockingQueue<String> requests = new LinkedBlockingQueue<>();
        BlockingQueue<String> replies = new LinkedBlockingQueue<>();
        
        var myIrc 
                = new MyIrc("eu.undernet.org", 6667, requests, replies);
        myIrc.start();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean done = false;
        
        while (!done) {
            System.out.print("> ");

            String inp = reader.readLine();
            if (inp.toLowerCase().equals("exit")) {
                 done = true;
            } else {
                requests.put(inp);
            }
        }
    }
}
