/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.utils;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Transport;

/**
 *
 * @author ciaran
 */
// TODO : Complete MailSend
public class MailSend {
    //private List<String> to = new ArrayList<>();
    private String to;
    private String from;
    private String subject;
    private String textContent;
    private final SmtpConfig config;
    
    public MailSend(SmtpConfig config) {
        this.config = config;
    }
    
    // Comma seperated
    public MailSend to(String to) {
        this.to = to;
        
        return this;
    }

    /*
    public MailSend to(List<String> to) {
        this.to = to;
        
        return this;
    }
*/
    
    public MailSend from(String from) {
        this.from = from;
        
        return this;
    }
    
    public MailSend subject(String subject) {
        this.subject = subject;
        
        return this;
    }

    public MailSend textContent(String textContent) {
        this.textContent = textContent;
        
        return this;
    }
    
    public void send() throws javax.mail.MessagingException {
        String destmailid = this.to;
        String sendrmailid = this.from;
        
        final String uname = this.config.getUserName();
        final String pwd = this.config.getPassword();

        String smtphost = this.config.getServer();

        Properties props = new Properties();
        props.put("mail.smtp.auth", this.config.isAuth());
        props.put("mail.smtp.starttls.enable", this.config.isTls());
        props.put("mail.smtp.host", smtphost);
        props.put("mail.smtp.port", this.config.getPort());
        //props.setProperty("mail.debug", "true");
        
        Session sessionobj = Session.getInstance(props,
            new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(uname, pwd);
                }
            });
 
      try {
	   Message messageobj = new MimeMessage(sessionobj);
	   messageobj.setFrom(new InternetAddress(sendrmailid));
	   messageobj.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destmailid));
	   messageobj.setSubject(this.subject);
	   messageobj.setText(this.textContent);

	   Transport.send(messageobj);
      } catch (MessagingException exp) {
         throw new RuntimeException(exp);
      }
    }
}
