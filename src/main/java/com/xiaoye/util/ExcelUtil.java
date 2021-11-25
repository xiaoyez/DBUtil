package com.xiaoye.util;

import com.xy.entity.Column;
import com.xy.entity.Table;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

/**
 * @author xiaoye
 * @create 2021-10-15 15:03
 */
public class ExcelUtil {

    @SneakyThrows
    public static void write(File excelFile, List<Table> tables)
    {
        OutputStream os = new FileOutputStream(excelFile);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        String[] header = new String[]{"名称","数据库key","类型","长度","备注","原字段","表"};

        writeHeader(sheet,header);

        int rowNumber = 0;
        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
            Collection<Column> columns = table.columns();
            int j = 1;
            for (Column column : columns) {
                HSSFRow row = sheet.createRow(++rowNumber);
                writeColumn(row,column,table);
                j++;
            }

        }

        workbook.setActiveSheet(0);
        workbook.write(os);
    }

    @SneakyThrows
    public static void write(File excelFile, Table table)
    {
        OutputStream os = new FileOutputStream(excelFile);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        String[] header = new String[]{"名称","数据库key","类型","长度","备注","原字段","表"};

        writeHeader(sheet,header);

        Collection<Column> columns = table.columns();
        int i = 1;
        for (Column column : columns) {
            HSSFRow row = sheet.createRow(i);
            writeColumn(row,column,table);
            i++;
        }

        workbook.setActiveSheet(0);
        workbook.write(os);

    }

    public static void writeColumn(HSSFRow row, Column column, Table table) {
        String fieldName = StringUtil.castUnderlineToCamel(column.getName());
        fieldName = StringUtil.firstLetterToLowerCase(fieldName);
        row.createCell(0).setCellValue(fieldName);
        row.createCell(1).setCellValue(column.getName());
        row.createCell(2).setCellValue(column.getTypeName());
        row.createCell(3).setCellValue(StringUtil.getDefaultEmpty(column.getLength()));
        row.createCell(4).setCellValue(column.getComment());
        row.createCell(5).setCellValue("");
        row.createCell(6).setCellValue(table.getName());
    }

    public static void writeHeader(HSSFSheet sheet, String[] header) {
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            row.createCell(i).setCellValue(header[i]);
        }
    }
}
