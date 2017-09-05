/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;

/**
 *
 * @author ciaran
 */
public class Requests {
    private void sendReq(BufferedWriter out, String req) throws IOException {
        //out.write("GET /news/world/rss.xml?edition=uk HTTP/1.1\r\n");
        
        out.write("GET /news/world/rss.xml?edition=uk HTTP/1.1\r\n");
        
        out.write("Host: feeds.bbci.co.uk\r\n");
        out.write("Cache-Control: no-cache\r\n");
        out.write("\r\n");
        
        out.flush();
    }
 
    private static void readResponse(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }
 
    public void get(String url) throws IOException {
        URL u = new URL(url);
        Socket socket = new Socket(u.getHost(), 80);
//        Socket socket = new Socket("google.co.uk", 443);
        
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
                
        sendReq(out, "/");
        readResponse(in);
        
        out.close();
        in.close();
    }
}
