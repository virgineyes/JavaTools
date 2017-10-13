package com.wintech.test;

import org.apache.log4j.Logger;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

public class MailController {
    private static final Logger LOGGER = Logger.getLogger(MailController.class);
    private static final String HOST = "tpecas1.wtmec.com";
    private static final String USER_NAME ="EyesC";
    private static final String PASS ="asdfveraloveeyesWT";
    
    private String hostName;
    private String userName;
    private String password;
    
    public MailController(String host, String userName, String password){
       this.hostName = host;
       this.userName = userName;
       this.password = password;
    }
    
    public static void main(String[] args) throws Exception {
        MailController mail = new MailController(HOST, USER_NAME, PASS);
        mail.saveAttachment();
    }
    
    public void saveAttachment() {
        Session session = MailController.getSession();
        try {
            Folder folder = getFolder(session);
            Message[] message = folder.getMessages();
            LOGGER.info("Messages's¡@length:¡@" + message.length);
            for (Message msg : message) {
                String subject = msg.getSubject();
                LOGGER.info("Messages's¡@subject:¡@" + subject);
                if (msg.getSubject().toUpperCase().indexOf("Product Information ECCN".toUpperCase()) > -1) {
                    PraseMessage praseMessage = new PraseMessage(msg);
                    praseMessage.setAttachPath("C://uploads//ECCN");
                    praseMessage.saveAttachMent((Part) msg);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getStackTrace(), e);
        }
    }

    private Folder getFolder(Session session) throws MessagingException {
        Store store = session.getStore("pop3");
        store.connect(this.hostName, this.userName, this.password);
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        return folder;
    }
    
    private static Session getSession() {
        Properties props = new Properties(System.getProperties());
        return Session.getInstance(props, null);   
    }
}
