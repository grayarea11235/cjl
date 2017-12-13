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
public class HttpResponse {
    private String body;
    private int code;
    private String contentType;
    private List<HttpHeader> headers = new ArrayList<>();

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HttpHeader> headers) {
        this.headers = headers;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    
    public void addHeader(HttpHeader header) {
        this.headers.add(header);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("HTTP/1.1 %s OK\r\n", Integer.toString(this.code)))
          .append("\r\n")
          .append(this.getBody())
          .append("\r\n");
          

        
        return sb.toString();
    }
    
}
