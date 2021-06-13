/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.net.http;

import java.net.URL;
import java.util.List;

/**
 *
 * @author cpd
 */
public class HttpRequest extends HttpMessage {
/*
    public HttpRequest() {
        this.method = null;
        this.path = null;
    }

    public HttpRequest(HttpMethod method) {
        this.method = method;
        this.path = null;
    }
*/
    public HttpRequest(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    //region Private
    private final HttpMethod method;
    private final String path;
    //endregion Private
        
    public String getPath()
    {
        return path;
    }

//    public void setPath(String path) {
//        this.path = path;
//    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

/*
    @Override
    public void setMethod(HttpMethod method)
    {
        this.method = method;
    }
*/
    
    @Override
    public HttpMethod getMethod()
    {
        return method;
    }
    
    
    public URL getUrl() {
        return url;
    }
    
    //public HttpHeader getHeader(String name) {
        //headers.filter(s -> s.name == name);
    //}
    
    public static HttpRequest createRequest(List<String> in) {
        //final var result = new HttpRequest();
        HttpRequest result = null;
        
        
        assert(in.size() > 0);
        
        String firstLine = in.get(0);
        
        if (firstLine.startsWith("GET")) {
            System.out.println("Got a GET request!");
            
            String[] f = firstLine.split(" ");
            // Turn string into enum... this was all fixed but got fucked by a bad merge.
//            HttpRequest result = new HttpRequest(f[1],"");
            result = new HttpRequest(HttpMethod.GET, "");
            //result.setPath(f[1]);
            
//            result.setMethod(HttpMethod.GET);
//            result.setPath(f[1]);
            result.setVersion(f[2].replaceAll("\r\n", ""));
            
            result.setMethod(HttpMethod.GET);
            in.removeIf(s -> s.equals("\r\n"));
            in.stream().skip(1).forEach(
                    s -> { 
                    System.out.print("Stream : " + s); 
                    HttpHeader newHeader = new HttpHeader(s);
// THIS IS WHATS FUCKING THIS!                   
//                    result.getHeaders().add(newHeader);
                });
/*            
            in.subList(1, in.size()).forEach(
                    s -> { System.out.println("forEach : " + s); 
                    });
            
            result.setMethod(HttpMethod.GET);
            for (int i = 1; i < in.size(); ++i) {
                String s = in.get(i);
                if ("\r\n".equals(s)) {
                    break;
                }
                HttpHeader newHeader = new HttpHeader(s);
                result.getHeaders().add(newHeader);
            }
*/
        } else if (firstLine.startsWith("POST")) {
            System.out.println("Got a POST request!");
            
            in.stream().forEach((line) -> {
                System.out.print(line);
            });
            
            //result = new HttpRequest();
            //result.setMethod(HttpMethod.POST);
            result = new HttpRequest(HttpMethod.POST, "");

            // TODO Under construction
            //result.setMethod(HttpMethod.POST);

            in.removeIf(s -> s.equals("\r\n"));
            in.stream().skip(1).forEach(                                                                                                                                                                    
                    s -> { 
                    System.out.println("Stream : " + s); 
                    HttpHeader newHeader = new HttpHeader(s);
// THIS IS WHATS FUCKING THIS!                   
//result.getHeaders().add(newHeader);
                });
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("GET %s HTTP/1.1\r\n", this.url.getFile()));
        
        this.getHeaders().forEach((header) -> {
            sb.append(header.toString());
        }); 
        
        
    //private List<HttpHeader> headers = new ArrayList<>();
    //private String path;
    //private String version;
        
        return sb.toString();
    }
}
