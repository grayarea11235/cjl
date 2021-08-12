f/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.net.simpleirc;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ciara
 */
public class Channel {
    private final String name;
    private String topic;
    private final List<String> users = new ArrayList<>();
    
    public Channel(String name) {
        this.name = name;
    }
    
   public void addUser(String user) {
       users.add(user);
   }
}
