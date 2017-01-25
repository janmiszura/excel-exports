package com.gleidsonfersanp.exports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gleidsonfersanp.db.dao.EXEXDaoImpl;
import com.gleidsonfersanp.db.dao.IEXEXDao;
import com.gleidsonfersanp.db.query.ExportColumnQuery;
import com.gleidsonfersanp.db.query.ExportQuery;
import com.gleidsonfersanp.db.query.ExportQueryBuilder;
import com.gleidsonfersanp.db.query.ExportResultQuery;
import com.gleidsonfersanp.extra.DBUtilTestImpl;
import com.gleidsonfersanp.extra.IDBUtilTest;
import com.gleidsonfersanp.extra.exception.GeneralException;

public class ExcelExportsTest {

	private IDBUtilTest iUtilTest;
	private IExcelExports iExcelExports;

	private IEXEXDao dao;

	@Before
	public void init(){

		try {

			iUtilTest = new DBUtilTestImpl();

			iUtilTest.createDataBase();

			iUtilTest.injectData();

			iExcelExports = new ExcelExportsImpl(iUtilTest.getDataSource());

			dao = new EXEXDaoImpl(iUtilTest.getDataSource());

		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void exportExcelPathTest(){

		String pathFile = null;

		pathFile = getPathFile("/home/afrodite/dev/teste","exportacao-test");

		Assert.assertNotNull(pathFile);

		Assert.assertEquals(pathFile, "/home/afrodite/dev/teste/exportacao-test.xlsx");

	}

	@Test
	public void exportExcelFileTest(){

		String path = "/home/afrodite/dev/teste";
		String fileName = "exportacao-test.xlsx";

		File file = null;

		try {
			file = getFile(
					new ExportQueryBuilder()
					.table("cliente")
					.columnQueries(new ExportColumnQuery("nome","NAME"))
					.columnQueries(new ExportColumnQuery("idade","AGE"))
					.columnQueries(new ExportColumnQuery("email","EMAIL"))
					.build(),getPathFile(path, fileName));
		} catch (SQLException | GeneralException | IOException e) {
			e.printStackTrace();
		}

		//Assert.assertNotNull(file);


	}

	private File getFile(ExportQuery exportQuery, String pathFile) throws SQLException, GeneralException, IOException {
		String path = "/home/afrodite/dev/teste";
		String fileName = "exportacao-test.xlsx";

		File file = iExcelExports.exportsForLocalPath(exportQuery, path, fileName);

		return file;
	}

	private ExportResultQuery getExportResultQuery(ExportQuery exportQuery) throws SQLException, GeneralException{

		ExportResultQuery exportResultQuery = dao.executeQuery(new ExportQueryBuilder()
				.table("cliente")
				.columnQueries(new ExportColumnQuery("nome","NAME"))
				.columnQueries(new ExportColumnQuery("idade","AGE"))
				.columnQueries(new ExportColumnQuery("email","EMAIL"))
				.columnQueries(new ExportColumnQuery("telefone","FONE"))
				.build());

		return exportResultQuery;
	}

	private String getPathFile(String path, String fileName) {
		String pathFile = path+"/"+fileName+".xlsx";
		return pathFile;
	}


	@After
	public void postTest(){
		iUtilTest.dropDataBase();
	}

}
