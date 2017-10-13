package com.wintech.test;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class FileUtil {
    private static final Logger LOGGER = Logger.getLogger(FileUtil.class);
    private static final String DATETIME_FORMATE = "yyyyMMddHHmmssSSS";
    
    private FileUtil() {
    }
    
    public static void delFile(File delFile){
        if (!delFile.delete()){
            LOGGER.error("File delete error!!");
        }
    }
    
    public static void createDirectory(String directory, String subDirectory) {
        File fl = new File(directory);
        try {
            if (subDirectory == ""){
                fl.mkdir();
            } else {
                String[] dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (!subFile.exists()) {
                        subFile.mkdir();
                    }
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getStackTrace(), e);
        }
    }
    
    public static void copyFile(File oldFile, String newPath) {
        try {
            int readbyte;
            File oldfile = oldFile;
            if (oldfile.exists()) { 
                InputStream inStream = new FileInputStream(oldFile);
                Date now = new Date();
                SimpleDateFormat nowdate = new SimpleDateFormat(DATETIME_FORMATE);
                FileOutputStream outStream = new FileOutputStream(new File(newPath + "//" + nowdate.format(now) + "-" + oldFile.getName()));
                byte[] buffer = new byte[1024];
                while ((readbyte = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, readbyte);
                }
                inStream.close();
                outStream.close();
            }
        } catch(Exception e) {
            LOGGER.error(e.getStackTrace(), e);
        } 
    }
    
    public static List<File> crawlFolder(String sourceFolder, String crawlType){
        List<File> fileList = new ArrayList<File>();
        File folder = new File(sourceFolder);
        File[] sourceFileList = folder.listFiles();
        for (File sourceFile : sourceFileList) {
            if (sourceFile.getName().contains(crawlType)) {
                fileList.add(new File(sourceFolder + "\\" + sourceFile.getName()));    
            }
        }
        return fileList;
    }
    

    public static void createFileByInputStream(InputStream in, File storefile) {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(storefile));
            bis = new BufferedInputStream(in);
            int c;
            int temp = 0;
            while ((c = bis.read()) != -1) {
                temp = temp + c;
                bos.write(c);
                bos.flush();
            }
            LOGGER.info("temp:" + temp);

        } catch (Exception e) {
            LOGGER.error(e.getStackTrace(), e);
        } finally {
            if (bos != null) {
               try {
                   bos.close();
               } catch (Exception e){
                   LOGGER.error(e.getStackTrace(), e);
               }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception e){
                    LOGGER.error(e.getStackTrace(), e);
                }
             }
        }
    }
}
