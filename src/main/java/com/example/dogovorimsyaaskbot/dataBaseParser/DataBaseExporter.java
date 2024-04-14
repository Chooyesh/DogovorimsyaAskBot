package com.example.dogovorimsyaaskbot.dataBaseParser;

import java.io.*;



import com.example.dogovorimsyaaskbot.asksDataHouse.Ask;
import com.example.dogovorimsyaaskbot.asksDataHouse.AskRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class DataBaseExporter {
    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet ;

    @Autowired
    private AskRepository askRepository;

    //public DataBaseExporter() {workbook = new XSSFWorkbook();}

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Questions");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(15);
        style.setFont(font);

        createCell(row, 0, "№", style);
        createCell(row, 1, "Пользователь", style);
        createCell(row, 2, "Вопрос", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        }else if (value instanceof Long){
            cell.setCellValue((Long) value);
        }
        cell.setCellStyle(style);
    }



    private void writeDataLines() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (long i = 0; i < askRepository.count(); i++) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            Optional<Ask> ask = askRepository.findById(i+1);
            Long number = ask.get().getNumAsk();
            String question = ask.get().getAsk();
            String user = ask.get().getUser();
            createCell(row, columnCount++, number, style);
            createCell(row, columnCount++, user, style);
            createCell(row, columnCount++, question, style);
        }

    }



    public File createFile() throws FileNotFoundException {
        writeHeaderLine();
        writeDataLines();
        String path = "/root/DogovorBot/libs/Questions.xlsx";
        File excelFile = null;
        try {
            //FileOutputStream file = new FileOutputStream(new File("C:\\Users\\chues\\AppData\\Roaming\\Questions" +workbook.getSheet("Questions").getLastRowNum()+ ".xlsx")); создает новый файл
            FileOutputStream file = new FileOutputStream(new File(path));
            workbook.write(file);
            System.out.println("-------File sucsefully created and sent to chat-------");
            //excelFile = new File("C:\\Users\\chues\\AppData\\Roaming\\Questions" +workbook.getSheet("Questions").getLastRowNum()+ ".xlsx"); каждый раз новый путь к новосозданному файлу
            excelFile = new File(path);
            workbook.removeSheetAt(workbook.getSheetIndex("Questions"));
            //workbook.close(); // не закрываем воркбук так как при закрытии далее метод отказывается работать //todo говнокод
        } catch (IOException e) {
            throw new RuntimeException(e);
        } //рабочий метод сохранения файла

        return excelFile;
    }




}
