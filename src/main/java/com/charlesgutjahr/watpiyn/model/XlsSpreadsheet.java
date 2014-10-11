package com.charlesgutjahr.watpiyn.model;

import com.charlesgutjahr.watpiyn.config.Config;
import com.charlesgutjahr.watpiyn.config.Question;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.Date;
import java.util.Map;


public class XlsSpreadsheet {

  private final HSSFWorkbook workbook;
  private final File file;


  public XlsSpreadsheet(File file) throws IOException {
    this.file = file;
    if (file.exists()) {
      try (InputStream is =  new FileInputStream(file)) {
        workbook = new HSSFWorkbook(is);
      }
    } else {
      workbook = new HSSFWorkbook();
    }
  }

  public void setup(Config config) {
    HSSFSheet sheet = getSheet(config);
    if (sheet == null) {
      sheet = workbook.createSheet(getSheetName(config));
      writeHeaders(config, sheet);
    }

  }


  private void writeHeaders(Config config, HSSFSheet sheet) {
    // Create first row
    Row row = sheet.createRow((short)0);

    // Make the date column wider
    sheet.setColumnWidth(0, 256 * 16);

    // Create a bold font for the header
    // Create a new font and alter it.
    Font boldFont = workbook.createFont();
    boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    CellStyle boldStyle = workbook.createCellStyle();
    boldStyle.setFont(boldFont);
    boldStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);

    Cell dateCell = row.createCell(0);
    dateCell.setCellValue("Date Submitted");
    dateCell.setCellStyle(boldStyle);

    for (Question question : config.getQuestions()) {
      Cell cell = row.createCell(question.getNumber());
      String label = StringUtils.isBlank(question.getLabel()) ? question.getText() : question.getLabel();
      cell.setCellValue(label);
      cell.setCellStyle(boldStyle);
      sheet.setColumnWidth(question.getNumber(), 256 * 12);
    }
  }

  public void writeResponses(Config config, Map<Integer, String> responses) {
    HSSFSheet sheet = getSheet(config);
    int lastRow = sheet.getLastRowNum();

    Row row = sheet.createRow(lastRow + 1);
    Cell dateCell = row.createCell(0);
    dateCell.setCellValue(new Date());
    dateCell.setCellStyle(getDateStyle());

    for (Question question : config.getQuestions()) {
      Cell cell = row.createCell(question.getNumber());
      String value = responses.get(question.getNumber());
      if (question.getType() == QuestionType.CHECKBOX) {
        cell.setCellValue("true".equals(value));
      } else {
        if (value != null) {
          cell.setCellValue(value);
        }
      }
    }
  }


  public void save() throws IOException {
    try (OutputStream os = new FileOutputStream(file)) {
      workbook.write(os);
    }
  }


  private HSSFSheet getSheet(Config config) {
    String sheet = getSheetName(config);
    return workbook.getSheet(sheet);
  }


  private String getSheetName(Config config) {
    String sheet = config.getXlsSheet();
    if (StringUtils.isEmpty(sheet)) {
      sheet = "Form";
    }
    return sheet;
  }

  private CellStyle getDateStyle() {
    // Assume that the last style is our date style
    CellStyle cellStyle = workbook.getCellStyleAt((short) (workbook.getNumCellStyles() - 1));
    String format = cellStyle.getDataFormatString();
    if (!"d/m/yyyy h:mm".equals(format)) {
      cellStyle = workbook.createCellStyle();
      cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("d/m/yyyy h:mm"));
    }
    return cellStyle;
  }

}
