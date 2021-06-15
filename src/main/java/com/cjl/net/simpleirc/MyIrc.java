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
import java.util.concurrent.ExecutionException;
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
    
    private InetSocketAddress hostAddress;
    private AsynchronousSocketChannel client;
    
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
    
    
    public void startReaad() {
        
    }
    
    
    
    private void start_read() throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(2048);
                                
        while(true) {
            //Future<Integer> fs = client.read(buf);
            
            this.client.read(buf, this.client, new CompletionHandler<Integer, AsynchronousSocketChannel>(){
                @Override
                public void completed(Integer result, AsynchronousSocketChannel channel) {
                    //message is read from server
                    //messageRead.getAndIncrement();

                    //print the message
                    System.out.println("Read message:" + new String(buf.array()));
                }

                @Override
                public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                    System.out.println("fail to read message from server");
                }
            });
        }
    }


    // This is the TCP thread
    public void start() {
        new Thread( () -> {
                try {
                //this.client = AsynchronousSocketChannel.open();
                //this.hostAddress = new InetSocketAddress("eu.undernet.org", 6667);
                
                String req;
                try {
                    while (true) {
                        req = in_queue.take();
                        if (req != null) {
                            System.out.println(req);
                            
                            if (req.toLowerCase().equals("connect")) {
                                var nick = String.format("test%d", new Random().nextInt());
                                do_connect(nick);
                                
                                var sendStr = String.format("NICK %s", nick);
                                var bb = ByteBuffer.wrap(sendStr.getBytes());
    
                                Future<Integer> fi = client.write(bb);
                                
                                System.out.println(String.format("Have written %d bytes", fi.get()));
/*                                
                                client.write(bb, client,  new CompletionHandler<Integer, AsynchronousSocketChannel >() {
                                    @Override
                                    public void completed(Integer result, AsynchronousSocketChannel channel ) {
                                        System.out.println("Written");
                                        //after message written
                                        //NOTHING TO DO
                                    }

                                    @Override
                                    public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                                        System.out.println( "Fail to write the message to server");
                                    }
                                });


                                final ByteBuffer buf = ByteBuffer.allocate(2048);
                                while(true) {
                                    Future<Integer> fs = client.read(buf);

                                    try {
                                        Integer rep = fs.get();

                                        System.out.println(String.format("Read res is %d and returned is %s", rep, new String(buf.array())));
                                        buf.clear();
                                    } catch (ExecutionException ex) {
                                        Logger.getLogger(MyIrc.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
*/
                            }

                        }
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(IrcThread.class.getName()).log(Level.SEVERE, null, ex);
                }   catch (ExecutionException ex) {
                        Logger.getLogger(MyIrc.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                
            } catch (IOException ex) {
                Logger.getLogger(MyIrc.class.getName()).log(Level.SEVERE, null, ex);
            }
            
                
        }).start();
    }
    
    public void do_connect(String nick) throws InterruptedException, IOException {
        this.client = AsynchronousSocketChannel.open();
        this.hostAddress = new InetSocketAddress("eu.undernet.org", 6667);

       // This has to complete sync before we start a sending
        Future<Void> future = client.connect(hostAddress);
        try {
            future.get();
            System.out.println("Connection complete");
        } catch (ExecutionException ex) {
            Logger.getLogger(MyIrc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //start_read();
        
        new Thread(() -> { 
            while (true) {
                final ByteBuffer buf = ByteBuffer.allocate(2048);
                Future<Integer> fs = client.read(buf);

                try {
                    Integer rep = fs.get();

                    System.out.println(String.format("Read res is %d and returned is %s", rep, new String(buf.array())));
                    buf.clear();
                } catch (ExecutionException | InterruptedException ex) {
                    Logger.getLogger(MyIrc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        ).start();
    }
    
    public void join(String channel) {
        
    }
    
    
    public static void main(String[] args) throws IOException, InterruptedException {
        BlockingQueue<String> requests = new LinkedBlockingQueue<>();
        BlockingQueue<String> replies = new LinkedBlockingQueue<>();
        
        var myIrc 
                = new MyIrc("eu.undernet.org", 6667, requests, replies);
        
        myIrc.do_connect("Chickens");
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
