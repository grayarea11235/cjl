/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.msgthr;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ciara
 */
public class RecvThread implements Runnable {

    @Override
    public void run() {
        boolean done = false;
        
        // A simulated thread that releases random 'messages' at random intervals
        while(!done) {
            var waitValue = (int) (Math.random() * 10);
            try {
                Thread.sleep(waitValue * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RecvThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
