package com.wintech.test;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class ReadExcel {
    private static final Logger LOGGER = Logger.getLogger(FileUtil.class);
    private static final String[] ST_NO_ACCESS_CONTROL_ECCN = {"NEC", "NEC_E"};
    private static final String NO_ACCESS_CONTROL_ECCN_CODE = "EAR99"; 
    private static final String EXCEL_BACK_UP_FOLDER = "C:\\uploads\\ECCN\\excelBackUp";
    
    public static void main(String[] args) {
        List<File> excelList = FileUtil.crawlFolder("C:\\uploads\\ECCN","xls");
        for (File excel : excelList){
            ReadExcel read = new ReadExcel();
            Map<String, String> map = read.readEccnCode(excel);
            for (Entry<String, String> entry : map.entrySet()) {
                LOGGER.info("Key: " + entry.getKey() + ", ECCN: " + entry.getValue());
            }
        }
    }     
    
    public Map<String, String> readEccnCode(File excelFile) {
        Map<String, String> map = new HashMap<String, String>();
        HSSFWorkbook workbook = null;
        try {
            POIFSFileSystem poi = new POIFSFileSystem(new FileInputStream(excelFile));
            workbook = new HSSFWorkbook(poi);
        } catch (IOException e) {
            LOGGER.error(e.getStackTrace(), e);
        }
        if (workbook != null) {
            HSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                if (sheet.getRow(i) != null) {
                    addEccnCodeMap(map, sheet.getRow(i));
                }
            }
        }
        FileUtil.copyFile(excelFile, EXCEL_BACK_UP_FOLDER);
        FileUtil.delFile(excelFile);
        return map;
    }

    private void addEccnCodeMap(Map<String, String> map, HSSFRow row) {
        HSSFCell materialCell;
        HSSFCell eccnCell;
        materialCell = row.getCell(0);
        materialCell.setCellType(Cell.CELL_TYPE_STRING);
        eccnCell = row.getCell(15);
        eccnCell.setCellType(Cell.CELL_TYPE_STRING);
        if (ST_NO_ACCESS_CONTROL_ECCN[0].equals(eccnCell.getStringCellValue())
                        || ST_NO_ACCESS_CONTROL_ECCN[1].equals(eccnCell.getStringCellValue())) {
            map.put(materialCell.getStringCellValue(), NO_ACCESS_CONTROL_ECCN_CODE);
        } else {
            map.put(materialCell.getStringCellValue(), eccnCell.getStringCellValue());
        }
    }
}
