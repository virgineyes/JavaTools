package com.wintech.test;

public class OperatorSystem {
    private static String osName;
    private static String separator;
    
    static {
        osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("win") != -1) {
            separator = "\\";        
        } else {
            separator = "/";
        }
    }
    

    public String getOsName() {
        return osName;
    }

    public String getSeparator() {
        return separator;
    }
}
