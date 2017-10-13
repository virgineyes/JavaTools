package com.wintech.test;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;

public class DecompressZip {
    private static final Logger LOGGER = Logger.getLogger(DecompressZip.class);
    private static final String ZIP_BACK_UP_FOLDER = "C:\\uploads\\ECCN\\backUp";
    
    public static void main(String[] args) {
        List<File> sourceZipList = FileUtil.crawlFolder("C:\\uploads\\ECCN","zip");
        for (File zipFile : sourceZipList){
            String targetDir = "C:\\uploads\\ECCN";
            DecompressZip.unZip(zipFile, targetDir);
        }
    }

    private static void unZip(File zipFile, String targetDir) {
        ZipFile file = null;
        try {
            file = new ZipFile(zipFile);
            Enumeration<?> e = file.getEntries();
            while (e.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) e.nextElement();
                LOGGER.info("unziping " + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    unZipFolder(targetDir, zipEntry);
                } else {    
                    unZipFile(targetDir, file, zipEntry);
                }
            }
    
            FileUtil.copyFile(zipFile, ZIP_BACK_UP_FOLDER);
            FileUtil.delFile(zipFile);
        } catch (Exception e) {
            LOGGER.error("Failed in unZip", e);
        } finally {
            closeZipFile(file);
        }
    }

    private static void closeZipFile(ZipFile file) {
        if (file != null){
            try {
                file.close(); 
            } catch (Exception e){
                LOGGER.error("close file failed", e);
            }
        }
    }

    private static void unZipFile(String targetDir, ZipFile file, ZipEntry zipEntry) throws IOException {
        File targetFile = new File(targetDir + File.separator + zipEntry.getName());
        if (targetFile.createNewFile()) {
            InputStream in = file.getInputStream(zipEntry);
            FileOutputStream out = new FileOutputStream(targetFile);
    
            byte[] by = new byte[2048];
            int c;
            while ((c = in.read(by)) != -1) {
                out.write(by, 0, c);
            }
            out.close();
            in.close();
        } else {
            LOGGER.error("File create error!!");
        }
    }

    private static void unZipFolder(String targetDir, ZipEntry zipEntry) {
        String name = zipEntry.getName();
        FileUtil.createDirectory(targetDir, name);
    }
}
