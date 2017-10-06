/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.utils;

/**
 *
 * @author ciaran
 */
public class Arguments {
    private String[] args;
    
    public Arguments(String[] args) {
        this.args = args;
    }
    
    public boolean isFlag() {
        return false;
    }
}
