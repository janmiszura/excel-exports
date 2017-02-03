package com.gleidsonfersanp.document;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelExportsDocument {

	final static Logger logger = Logger.getLogger(ExcelExportsDocument.class);

	private String path;
	private String fileName;
	private SXSSFWorkbook wb = null;
	private SXSSFSheet sheet = null;
	private CellStyle cellStyle = null;
	private CreationHelper createHelper = null;
	private List<ExcelExportsRow> rows = new ArrayList<ExcelExportsRow>();

	public void writeFileToOutputStrem(OutputStream fileOut, String fileName) throws IOException{
		this.fileName = fileName;

		configureDocument();

		populateDocument();

		outPut(fileOut);

	}

	private void populateDocument() {
		for (ExcelExportsRow row: rows){

			SXSSFRow poiRow = sheet.createRow(row.getNumber());

			for (ExcelExportsCell cell : row.getCells()) {

				Cell poiCell = poiRow.createCell(cell.getNumber());

				addColumn(poiRow, poiCell, cell.getObject());

			}
		}
	}

	private void configureDocument() {

		if(rows.isEmpty())
			this.wb = new SXSSFWorkbook(100);
		else
			this.wb = new SXSSFWorkbook(rows.size());


		addColumnsFilters();

		cellStyle = wb.createCellStyle();
		createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy hh:mm"));
		this.sheet = wb.createSheet(this.fileName);

		wb.setCompressTempFiles(true);

	}

	private void addColumn(SXSSFRow row, Cell cell, Object value) {

		if(value == null) {
			return;
		}

		if(value instanceof String) {
			String v = (String) value;

			cell.setCellValue(v);
		} else if(value instanceof Boolean) {
			Boolean b = (Boolean) value;

			cell.setCellValue(b);
		} else if(value instanceof Integer) {
			Integer i = (Integer) value;

			cell.setCellValue(i);
		} else if(value instanceof Double) {
			Double d = (Double) value;

			cell.setCellValue(d);
		} else if(value instanceof Long) {
			Long d = (Long) value;

			cell.setCellValue(d);
		} else if(value instanceof Float) {
			Float f = (Float) value;

			cell.setCellValue(f);
		} else if(value instanceof BigDecimal) {
			BigDecimal v = (BigDecimal) value;
			Double d = v.doubleValue();

			cell.setCellValue(d);
		}else if(value instanceof Date) {
			Date v = (Date) value;

			cell.setCellValue(v);
			cell.setCellStyle(cellStyle);

		} else {
			logger.error("object not identify: "+value);
		}
	}

	private void addColumnsFilters() {
		sheet.setAutoFilter(CellRangeAddress.valueOf("A1:"+getAlphabeticalColumn(rows.get(0).getCells().size())+rows.get(0).getCells().size()));
	}

	private String getAlphabeticalColumn(Integer number){

		switch (number) {
		case 1:
			return "A";
		case 2:
			return "B";
		case 3:
			return "C";
		case 4:
			return "D";
		case 5:
			return "E";
		case 6:
			return "F";
		case 7:
			return "G";
		case 8:
			return "H";
		case 9:
			return "I";
		case 10:
			return "J";
		case 11:
			return "K";
		case 12:
			return "L";
		case 13:
			return "M";
		case 14:
			return "O";
		case 15:
			return "P";
		case 16:
			return "Q";
		case 17:
			return "R";
		case 18:
			return "S";
		case 19:
			return "T";
		case 20:
			return "U";
		case 21:
			return "V";
		case 22:
			return "W";
		case 23:
			return "X";
		case 24:
			return "Y";
		case 25:
			return "Z";
		default:
			return "Z";
		}

	}

	private void outPut(OutputStream fileOut) throws IOException {
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}

	public List<ExcelExportsRow> getRows() {
		return rows;
	}
}
