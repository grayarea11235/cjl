package com.cjl.net.http;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cpd
 */
public class HttpServer {
    private ServerSocket serverSocket;
    
    private final int port;
    private final String hostName;
    private final String rootPath = "/home/cpd/root";
    
    public int getPort()
    {
        return this.port;
    }
    
    public String getHostname()
    {
        return this.hostName;
    }
        
    HttpServer(String hostname, int port)
    {
        this.port = port;
        this.hostName = null;
    }
    
    private String loadFromFile(String fileName)
    {
        return null;
    }
    
    private String get404Message()
    {
        StringBuilder sb = new StringBuilder();
        
        String content = "<html><body>404 Not Found</body></html>";
        sb.append("HTTP/1.1 404 Not Found\n");
        sb.append("Content-Type: text/html; charset=UTF-8\n");
        sb.append(String.format("Content-Length: %d\n", content.length()));
        sb.append("\r\n");
        sb.append(content); 
                
        return sb.toString();
    }
    
    
    private String readHTTPLine(DataInputStream i) throws IOException {
        String result = "";
        boolean done = false;
        
        while (!done)
        {
            byte b = i.readByte();
            result += (char) b;

            if (b == 0x0D) {
                byte next = i.readByte();
                result += (char) next;
                if (next == 0x0A) {
                    done = true;
                } 
            }
        } 
        
        return result;
    }
    
    public void start() {
        // Create the server socket
        try {
            serverSocket = new ServerSocket(port);
            String inStr;
            do {
                try (Socket sock = serverSocket.accept(); DataInputStream inp = new DataInputStream(sock.getInputStream()); 
                        PrintStream out = new PrintStream(sock.getOutputStream())) {
                    
                    StringBuilder sb = new StringBuilder();
                    List<String> sl = new ArrayList<>();
                    
                    do
                    {
                        inStr = readHTTPLine(inp);
                        char[] c = inStr.toCharArray();
                        sl.add(inStr);
                        System.out.format("inStr = %s\n", inStr);
                    } while(!"\r\n".equals(inStr));
                    
                    HttpRequest req = HttpRequest.createRequest(sl);
                    
                    HttpResponse resp = null;
                    switch (req.getMethod()) {
                        case GET: 
                            /*
                            LocalDateTime ldt = LocalDateTime.now();                                    
                            sb.append("HTTP/1.1 200 OK\r\n")
                              .append("\r\n")
                              .append("<html><body><b>Hello from the Server!! ")
                              .append(ldt.toString())
                              .append(" ")
                              .append(UUID.randomUUID().toString())
                              .append("</b></body></html>");                                    
                            */
                            HttpServePage hsp = new HttpServePage();
                            resp = hsp.serve(req);
                            break;
                    }
                    out.print(resp);

/*                    
                        String[] parts = inStr.split(" ");

                        for (String s : parts) {
                            System.out.println(s);
                        }
                        
                        if (parts[0].equals("GET")) {
                            //StringBuilder sb = new StringBuilder();
                            switch (parts[1]) {
                                case "/":
                                    sb.append("HTTP/1.1 200 OK\r\n");
                                    sb.append("\r\n");
                                    LocalDateTime ldt = LocalDateTime.now();                                    
                                    sb.append("<html><body><b>Hello from the Server!! ")
                                      .append(ldt.toString())
                                      .append(" ")
                                      .append(UUID.randomUUID().toString())
                                      .append("</b></body></html>");                                    
                                    break;
                                    
                                case "/info":
                                    String returnContent = "<html><body><b>Lil HTTP Server Version 0.01</body></html>";
                                    int contentLength = returnContent.length();
                                    sb.append("HTTP/1.1 200 OK\r\n")
                                      .append("Content-Type: text/html; charset=ISO-8859-1\r\n")
                                      .append(String.format("Content-Length: %d\r\n", contentLength))
                                      .append("\r\n")
                                      .append(returnContent);
                                    break;
                                    
                                case "/favicon.ico":
                                    sb.append("HTTP/1.1 200 OK\r\n")
                                      .append("\r\n")
                                      .append("");
                                    //sb.append("<html><body><b>Lil HTTP Server faviconn</body></html>\n");
                                    break;
                                
                                case "/filelist":
//                                    HttpResponse resp - nw HttpResponse(200, "");
                                    break;
                                    
                                default:
                                    File rootFile = new File(rootPath);
                                    File fullPath = new File(rootFile, parts[1]);
                                    //returnContent = "<html><body><p>File name is " + fullPath.getPath() + "</p></body></html>";
                                    try {
                                    returnContent = readFileToString(new File(fullPath.getPath()), UTF_8);
                                    contentLength = returnContent.length();
                                    sb.append("HTTP/1.1 200 OK\r\n");
                                    //sb.append("Transfer-Encoding: chunked\r\n");
                                    sb.append("Content-Type: text/html; charset=ISO-8859-1\r\n")
                                      .append(String.format("Content-Length: %d\r\n", contentLength))
                                      .append("\r\n")
                                      .append(returnContent);
                                    } catch (IOException ioe) {
                                        sb.append(this.get404Message());
                                    }
                                    break;
                            }
                            
                            System.out.println("-------------------------------------------------------");
                            System.out.print(sb);
                            System.out.println("\n-------------------------------------------------------");
                            out.print(sb);
                        }
*/            
                }
            } while (!"exit".equals(inStr));
            serverSocket.close();
        }
        catch (IOException exp) {
            System.err.println(String.format("Exception : %s", exp.getMessage()));
        }
    }
}
