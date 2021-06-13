/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.utils;

/**
 *
 * @author ciara
 */
public class PrettyPrint {
    public void pprintObj(Object o) {
        
    }
    
    
    public String jsonToHTML(String json) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n");
        sb.append("  <body>\n");
        
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);        
            switch (c) {
                case '{':
                case '}':
                    sb.append("<font color=\"red\">").append(c).append("</font>");
                    break;
                    
                default:
                    sb.append(c);
            }
        }
        
        
        sb.append("    <font color=\"red\">");
        
        
        sb.append("  </body>\n");
        sb.append("</html>\n");
        
        return sb.toString();
    } 
}
