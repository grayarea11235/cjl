/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.net.http;

import java.net.URL;

/**
 *
 * @author ciaran
 */
public class HttpHeader {
    private String name;
    private String value;
  
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public HttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public HttpHeader(String name, int value) {
        this.name = name;
        this.value = Integer.toString(value);
    }

    public HttpHeader(String in) {
        if (in.contains(":")) {
            String[] sp = in.split(":");
            name = sp[0];
            value = sp[1];
        } else {
            // TODO : Throw exception
        }
    }
    
    @Override
    public String toString() {
        return name + ": " + value + "\r\n";
    }
}
