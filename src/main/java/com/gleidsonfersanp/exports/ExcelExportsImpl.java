package com.gleidsonfersanp.exports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.gleidsonfersanp.db.connection.EXEXDataSource;
import com.gleidsonfersanp.db.dao.EXEXDaoImpl;
import com.gleidsonfersanp.db.dao.IEXEXDao;
import com.gleidsonfersanp.db.query.ExportColumnResult;
import com.gleidsonfersanp.db.query.ExportQuery;
import com.gleidsonfersanp.db.query.ExportQueryBuilder;
import com.gleidsonfersanp.db.query.ExportResultQuery;
import com.gleidsonfersanp.extra.exception.GeneralException;

public class ExcelExportsImpl implements IExcelExports{

	final static Logger logger = Logger.getLogger(ExcelExportsImpl.class);

	private IEXEXDao dao;
	private SXSSFWorkbook wb = null;
	private SXSSFSheet sheet = null;
	private CellStyle cellStyle = null;
	private CreationHelper createHelper = null;
	private SXSSFRow rowHeader = null;

	private EXEXDataSource dataSource;

	public ExcelExportsImpl(EXEXDataSource dataSource) {
		this.dataSource = dataSource;
		this.wb = new SXSSFWorkbook (100);
		wb.setCompressTempFiles(true);
		cellStyle = wb.createCellStyle();
		createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy hh:mm"));
		this.sheet = wb.createSheet("tmp-file");
		rowHeader = sheet.createRow(0);
	}

	public File exportsForLocalPath(ExportQuery exportQuery, String path, String fileName) throws IOException, SQLException, GeneralException {
		String pathExports = getPathFile(path, fileName);

		ExportResultQuery exportResultQuery = getExportResultQuery(exportQuery);

		generateFile(exportResultQuery, pathExports);

		File file = new File(pathExports);

		if(file.exists())
			return file;

		return null;

	}

	public File exportsForLocalPath(String sql, String path, String fileName) throws IOException, SQLException, GeneralException {
		String pathExports = getPathFile(path, fileName);

		ExportResultQuery exportResultQuery = getExportResultQuery(sql);

		generateFile(exportResultQuery, pathExports);

		File file = new File(pathExports);

		if(file.exists())
			return file;

		return null;

	}

	private String getPathFile(String path, String fileName) {
		String pathFile = path+"/"+fileName+".xlsx";
		return pathFile;
	}

	private ExportResultQuery getExportResultQuery(ExportQuery exportQuery) throws SQLException, GeneralException{

		dao = new EXEXDaoImpl(this.dataSource);

		ExportResultQuery exportResultQuery = dao.executeQuery(new ExportQueryBuilder()
				.table("cliente")
				.columnsQueries(exportQuery.getColumnQuerys())
				.build());

		return exportResultQuery;
	}

	private ExportResultQuery getExportResultQuery(String sql) throws SQLException, GeneralException{

		dao = new EXEXDaoImpl(this.dataSource);

		ExportResultQuery exportResultQuery = dao.executeQuery(sql);

		return exportResultQuery;
	}

	private void createHeader(ExportResultQuery exportResultQuery) {
		List<ExportColumnResult> columns = exportResultQuery.getColumnResults();
		int indexCol = 0;
		for (ExportColumnResult exportColumnResult : columns) {
			addColumn(rowHeader, indexCol, exportColumnResult.getName().toUpperCase());
			indexCol++;
		}
	}

	private void addColumn(SXSSFRow row, int columnNumber, Object value) {

		if(value == null) {
			return;
		}

		Cell cell = row.createCell(columnNumber);

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

	private void generateFile(ExportResultQuery exportResultQuery, String path) throws IOException {

		List<ExportColumnResult> columns = exportResultQuery.getColumnResults();

		createHeader(exportResultQuery);

		List<Map<SXSSFRow, List<Object>>> maps = new ArrayList<Map<SXSSFRow, List<Object>>>();

		List<List<Object>> listOfListObjects = createListOfObjects(columns);

		maps = createListMap(listOfListObjects);

		addContentColumns(maps);

		Integer size = listOfListObjects.size();

		addColumnsFilters(size);

		FileOutputStream fileOut = new FileOutputStream(path);

		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();

	}

	private List<List<Object>> createListOfObjects(List<ExportColumnResult> columns) {

		List<List<Object>> listOfListObjects = new ArrayList<List<Object>>();

		if(columns.size() == 1){

			List<Object>listObjects= new ArrayList<Object>();

			listObjects.addAll(columns.get(0).getObjects());

			listOfListObjects.add(listObjects);
			return listOfListObjects;
		}


		for (int i = 0; i< columns.size(); i++) {

			List<Object>listObjects= new ArrayList<Object>();

			int indexCol = 0;
			for (int j = 0; j< columns.size(); j++) {
				listObjects.add(columns.get(indexCol).getObjects().get(i));
				indexCol++;
			}

			listOfListObjects.add(listObjects);

		}
		return listOfListObjects;
	}

	private List<Map<SXSSFRow, List<Object>>> createListMap(List<List<Object>> listOfListObjects) {
		List<Map<SXSSFRow, List<Object>>> maps = new ArrayList<Map<SXSSFRow, List<Object>>>();

		int indexRow = 1;

		for (List<Object> list : listOfListObjects) {

			for (Object object : list) {

				Map<SXSSFRow, List<Object>> map = createRowMap(indexRow, list);

				maps.add(map);
			}

			indexRow++;

		}

		return maps;
	}

	private Map<SXSSFRow, List<Object>> createRowMap(int indexRow, List<Object> list) {
		SXSSFRow row = sheet.createRow(indexRow);

		Map<SXSSFRow, List<Object>> map = new HashMap<SXSSFRow, List<Object>>();

		map.put(row, list);
		return map;
	}

	private void addContentColumns(List<Map<SXSSFRow, List<Object>>> maps) {

		for (Map<SXSSFRow, List<Object>> map : maps) {

			for (Map.Entry<SXSSFRow,  List<Object>> entry : map.entrySet()) {

				int columnNumber =0;
				for(Object obj: entry.getValue()){
					addColumn(entry.getKey(), columnNumber, obj);
					columnNumber++;
				}

			}

		}
	}

	private void addColumnsFilters(Integer size) {
		sheet.setAutoFilter(CellRangeAddress.valueOf("A1:"+getAlphabeticalColumn(size)+size));
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

}
