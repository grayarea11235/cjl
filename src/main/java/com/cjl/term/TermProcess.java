/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.term;

/**
 *
 * @author ciara
 */
public class TermProcess {
    public TermProcess(String program) {
        Process p = new ProcessBuilder("notepad.exe").start();
        System.out.println(p.getPid());        
    }
}
