/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.apps.jgrep;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;

/**
 *
 * @author ciaran
 */
public class jGrep {

    // jgrep path glob pattern
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Application.launch(jGrepFx.class, args);
        } else {
            final String path = args[0];
            final String glob = args[1];
            final String pattern = args[2];
        
        
    //        Search search = new Search("C:\\Temp", "**/*.c", ".*");
            Search search = new Search(path, glob, pattern);
            try {
                search.run(true);
            } catch (IOException ex) {
                Logger.getLogger(jGrep.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
