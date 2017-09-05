/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.net.http;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cpd
 */
public class HttpRequest extends HttpMessage {
    public HttpRequest() {
    }

    //region Private
    private HttpMethod method;
    private List<HttpHeader> headers = new ArrayList<>();
    private String path;
    //endregion Private
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HttpHeader> headers) {
        this.headers = headers;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpMethod getMethod() {
        return method;
    }
    
    //public HttpHeader getHeader(String name) {
        //headers.filter(s -> s.name == name);
    //}
    
    public static HttpRequest createRequest(List<String> in) {
        HttpRequest result = new HttpRequest();
 
        assert(in.size() > 0);
        
        String firstLine = in.get(0);
        
        if (firstLine.startsWith("GET")) {
            System.out.println("Got a GET request!");
            
            String[] f = firstLine.split(" ");
            result.setPath(f[1]);
            result.setVersion(f[2].replaceAll("\r\n", ""));
            
            result.setMethod(HttpMethod.GET);
            in.removeIf(s -> s.equals("\r\n"));
            in.stream().skip(1).forEach(
                    s -> { 
                    System.out.print("Stream : " + s); 
                    HttpHeader newHeader = new HttpHeader(s);
                    result.getHeaders().add(newHeader);
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
            result.setMethod(HttpMethod.POST);
            in.removeIf(s -> s.equals("\r\n"));
            in.stream().skip(1).forEach(                                                                                                                                                                    
                    s -> { 
                    System.out.println("Stream : " + s); 
                    HttpHeader newHeader = new HttpHeader(s);
                    result.getHeaders().add(newHeader);
                });
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Method : %s", this.method));
        //sb.append(String.format(""));
        
        
    //private List<HttpHeader> headers = new ArrayList<>();
    //private String path;
    //private String version;
        
        return sb.toString();
    }
}
