/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.net.http;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ciaran
 */
public class HttpMessage {
    private HttpMethod method;

    protected int code;
    protected String version;

    protected List<HttpHeader> headers = new ArrayList<>();
    protected URL url;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
    private byte[] body;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    public void setHeaders(List<HttpHeader> headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
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
    
    public HttpHeader getHeader(String name) throws Exception {
        Optional<HttpHeader> accept = this.headers
                .stream()
                .filter(s -> s.getName().toUpperCase().equals("ACCEPT"))
                .findFirst();

        HttpHeader res = null;
        if (accept.isPresent() == true) {
            res = accept.get();
        } else {
            // TODO Maybe throw here 
            throw new Exception("Header not found in HttpRequest!");
        }
        
        return res;
    }
    
    public String getHeaderStrings() {
        StringBuilder sb = new StringBuilder();
        headers.stream().forEach((head) -> {
            sb.append(head.toString());
        });
        
        return sb.toString();
    }
    
    public int getContentLength() {
        return this.body.length;
    }
}
