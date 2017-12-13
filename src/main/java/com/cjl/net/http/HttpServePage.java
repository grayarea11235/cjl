/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.net.http;

import java.time.LocalDateTime;

/**
 *
 * @author ciaran
 */
public class HttpServePage {
    public HttpResponse serve(HttpRequest req) {
        HttpResponse resp = new HttpResponse();
        resp.setCode(200);
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n")
          .append("  <head>\n")
          .append("  </head>\n")
          .append("  <body>\n")
          .append(String.format("    <b>Hello From the server!!! Time is %s</b>\n", LocalDateTime.now().toString()))
          .append("  </body>\n")
          .append("</html>\n");
        resp.setBody(sb.toString());
        resp.addHeader(new HttpHeader("Content-Type", "text/html"));
        
        switch (req.getPath().toLowerCase()) {
            case "info":
                break;
        }
        
        return resp;
    }
}
