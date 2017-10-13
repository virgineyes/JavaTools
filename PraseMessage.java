package com.wintech.test;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeUtility;

public class PraseMessage {
    private static final Logger LOGGER = Logger.getLogger(PraseMessage.class);
    private static final String DATETIME_FORMATE = "yyyyMMddHHmmssSSS";
    private Message message;
    private String attachPath;
    
    public PraseMessage(Message message) {
        this.message = message;
    }

    public String getAttachPath() {
        return attachPath;
    }

    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath;
    }
    
    public String getSubject()throws MessagingException{
        String subject = "";
        try {
            subject = MimeUtility.decodeText(message.getSubject());
            if (subject == null){
                subject = "";
            }
        } catch (Exception e) {
            LOGGER.error(e.getStackTrace(), e);
        }
        return subject;
    }
    
    public void saveAttachMent(Part part) throws Exception {
        String fileName;
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            LOGGER.info("count:" + mp.getCount());
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                LOGGER.info("fileName:" + mpart.getFileName());
                fileName = mpart.getFileName();
                if (fileName != null) {
                    fileName = MimeUtility.decodeText(fileName);
                    LOGGER.info("MimeUtility.decodeText(fileName): " + fileName);
                    saveFile(fileName, mpart.getInputStream());
                } 
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachMent((Part) part.getContent());

        } else if (part.isMimeType("application/pdf")) {
            LOGGER.info("application/pdf");
        }
    }
    
    private void saveFile (String inputFileName,InputStream in) {
        OperatorSystem os = new OperatorSystem();
        String separator = os.getSeparator(); 
        String storeDir = getStoreDir(os, getAttachPath());
        String fileName = null;
        if ("C://uploads//ECCN".equalsIgnoreCase(storeDir)) {
            Date now = new Date();
            SimpleDateFormat nowdate = new SimpleDateFormat(DATETIME_FORMATE);
            if (inputFileName.toLowerCase().contains("zip")) {
                fileName = nowdate.format(now) + "_" + inputFileName;
            }
        }
        
        File storefile = new File(storeDir + separator + fileName);

        if (storefile.exists()) {
            LOGGER.info("duplicate:¡@" + storefile.toString());
            return;
        }
        LOGGER.info("storefile's¡@path:¡@" + storefile.toString());
        
        FileUtil.createFileByInputStream(in, storefile);
    }

    private String getStoreDir(OperatorSystem os, String storeDir) {
        String dir = storeDir;
        if (os.getOsName().toLowerCase().indexOf("win") != -1) {
            if (storeDir == null || "".equals(storeDir)){
                dir = "c:\\tmp";
            }
        } else {
            dir = "/tmp";
        }
        return dir;
    }

}
