package com.gleidsonfersanp.exports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import com.gleidsonfersanp.db.query.ExportResultQuery;
import com.gleidsonfersanp.extra.Util;
import com.gleidsonfersanp.extra.exception.GeneralException;

public class ExcelExportsImpl implements IExcelExports{

	final static Logger logger = Logger.getLogger(ExcelExportsImpl.class);

	private IEXEXDao dao;
	private SXSSFWorkbook wb = null;
	private SXSSFSheet sheet = null;
	private CellStyle cellStyle = null;
	private CreationHelper createHelper = null;
	private SXSSFRow rowHeader = null;

	private String path;
	private String pathExports;
	private String fileName;
	private String sqlQuery;

	private ExportQuery exportQuery;

	private EXEXDataSource dataSource;

	private List<ExportColumnResult> columns;

	public ExcelExportsImpl(EXEXDataSource dataSource) {
		this.dataSource = dataSource;
	}

	private void init(String sql, String path, String fileName) throws GeneralException{
		this.sqlQuery = sql;
		this.path = path;
		this.fileName = fileName;
		this.pathExports = getPathFile();
		dao = new EXEXDaoImpl(this.dataSource);
	}

	private void init(ExportQuery exportQuery, String path, String fileName) throws GeneralException{
		this.exportQuery = exportQuery;
		this.path = path;
		this.fileName = fileName;
		this.pathExports = getPathFile();
		this.wb = new SXSSFWorkbook (100);

		dao = new EXEXDaoImpl(this.dataSource);
	}

	public void writeFileForOutputStream(ExportQuery exportQuery, String fileName, OutputStream os)
			throws IOException, SQLException, GeneralException {

		init( exportQuery, null, fileName );

		generateFile();

		outPut(os);

	}

	public void writeFileForOutputStream(String sql, String fileName, OutputStream os)
			throws IOException, SQLException, GeneralException {

		init( sql, null, fileName );

		generateFile();

		outPut(os);

	}

	public File writeFileForLocalPath(ExportQuery exportQuery, String path, String fileName) throws IOException, SQLException, GeneralException {

		init( exportQuery, path, fileName );

		generateFile();

		return writeFile();
	}

	public File writeFileForLocalPath(String sql, String path, String fileName) throws IOException, SQLException, GeneralException {

		init(sql, path, fileName);

		generateFile();

		return writeFile();
	}

	private File writeFile() throws FileNotFoundException, IOException {
		OutputStream fileOut = new FileOutputStream(pathExports);

		outPut(fileOut);

		File file = new File(pathExports);

		if(file.exists())
			return file;

		return null;
	}

	private String getPathFile() {
		String pathFile = path+"/"+fileName;
		return pathFile;
	}

	private void createHeader() throws SQLException {

		int indexCol = 0;

		for (ExportColumnResult exportColumnResult : columns) {
			addColumn(rowHeader, indexCol, exportColumnResult.getName().toUpperCase());
			indexCol++;
		}

	}

	private void generateExportColumnResult() throws SQLException {
		ExportResultQuery exportResultQuery = null;

		if(!Util.isNullOrEmpty(this.sqlQuery))
			exportResultQuery =  dao.executeQuery(this.sqlQuery);
		else
			exportResultQuery =  dao.executeQuery(this.exportQuery);

		this.columns = exportResultQuery.getColumnResults();
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


	private void generateFile() throws IOException, SQLException {

		generateExportColumnResult();

		configureDocument();

		createHeader();

		List<Map<SXSSFRow, List<Object>>> maps = new ArrayList<Map<SXSSFRow, List<Object>>>();

		List<List<Object>> listOfListObjects = createListOfObjects();

		maps = createListMap(listOfListObjects);

		addContentColumns(maps);

		Integer size = columns.size() > 0 ? columns.size(): 1;

		addColumnsFilters(size);
	}

	private void configureDocument() {

		if(columns.isEmpty())
			this.wb = new SXSSFWorkbook(100);
		else
			this.wb = new SXSSFWorkbook(columns.get(0).getObjects().size());

		cellStyle = wb.createCellStyle();
		createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy hh:mm"));
		this.sheet = wb.createSheet("tmp-file");
		rowHeader = sheet.createRow(0);

		wb.setCompressTempFiles(true);

	}

	private List<List<Object>> createListOfObjects() throws SQLException {

		List<List<Object>> listOfListObjects = new ArrayList<List<Object>>();

		for (int i = 0; i< columns.size(); i++) {

			List<Object>listObjects= new ArrayList<Object>();

			for (int j = 0; j < columns.get(i).getObjects().size(); j++) {
				listObjects.add(columns.get(i).getObjects().get(j));
			}

			listOfListObjects.add(listObjects);

		}
		return listOfListObjects;
	}

	private List<Map<SXSSFRow, List<Object>>> createListMap(List<List<Object>> listOfListObjects) {

		List<Map<SXSSFRow, List<Object>>> maps = new ArrayList<Map<SXSSFRow, List<Object>>>();

		if(listOfListObjects.isEmpty())
			return maps;

		int indexRow = 1;

		for (int i = 0; i < listOfListObjects.get(0).size();i++) {
			List<Object> objects = new ArrayList<Object>();

			for (int j = 0; j < listOfListObjects.size();j++) {
				objects.add(listOfListObjects.get(j).get(i));
			}

			Map<SXSSFRow, List<Object>> map = createRowMap(indexRow,objects);

			maps.add(map);

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

	private void outPut(OutputStream fileOut) throws IOException {
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}

}
