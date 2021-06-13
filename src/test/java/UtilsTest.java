/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.cjl.utils.MailSend;
import com.cjl.utils.SmtpConfig;
import javax.mail.MessagingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ciaran
 */
public class UtilsTest {
    
    public UtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void mailSendTest() throws MessagingException {
        /*
        SmtpConfig config = new SmtpConfig();
        config.setServer("smtp.gmail.com");
        config.setPort(587);
        config.setAuth(true);
        config.setTls(true);
        config.setUserName("ciaran.dunn@gmail.com");
        */

        // This uses a local smtp server with not security of auth
        SmtpConfig config = new SmtpConfig();
        config.setServer("127.0.0.1");
        config.setPort(25);
        config.setAuth(false);
        config.setTls(false);
        MailSend sm = new MailSend(config);
        sm
                .from("ciaran.dunn@gmail.com")
                .to("ciaran.dunn@gmail.com")
                .from("ciaran.dunn@gmail.com")
                .subject("This is a test email!")
                .textContent("This is just a text email")
                .send();

                
    }
}
