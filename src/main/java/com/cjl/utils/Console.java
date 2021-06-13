/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.utils;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author ciara
 */
public class Console extends JFrame {
    private JTextArea textArea;
    
    public Console() {
        
    }
    
    public void setPos() {
        
    }
 
    public void initGui() {
        this.setTitle("Terminal");
        this.textArea = new JTextArea(24, 80);
        this.textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setEditable(false);
        //this.textArea.getCaret()
        
        
        Color c = new Color(0,0,0,100);
        textArea.setBackground(c);
        
        this.add(this.textArea);
    }
            
    public static void main(String[] args) {
        Console console = new Console();
        console.initGui();
        
        console.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //console.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        console.pack();
        console.setVisible(true);        
    }
}
