package com.gleidsonfersanp.exports;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gleidsonfersanp.db.query.ExportColumnQuery;
import com.gleidsonfersanp.db.query.ExportQueryBuilder;
import com.gleidsonfersanp.extra.DBUtilTestImpl;
import com.gleidsonfersanp.extra.IDBUtilTest;
import com.gleidsonfersanp.extra.exception.GeneralException;

public class ExcelExportsTest {

	private IDBUtilTest iUtilTest;
	private IExcelExports iExcelExports;

	@Before
	public void init(){

		try {

			iUtilTest = new DBUtilTestImpl();

			iUtilTest.createDataBase();

			iUtilTest.injectData();

			iExcelExports = new ExcelExportsImpl(iUtilTest.getDataSource());

		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void exportExcelFileForExportQueryTest(){

		String path = "/home/afrodite/dev/teste";
		String fileName = "exportacao-test-1";

		File file = null;

		try {
			file = iExcelExports.exportsForLocalPath(new ExportQueryBuilder()
					.table("cliente")
					.columnQueries(new ExportColumnQuery("nome","NAME"))
					.columnQueries(new ExportColumnQuery("idade","AGE"))
					.columnQueries(new ExportColumnQuery("email","EMAIL"))
					.build(), path, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (GeneralException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(file);
	}

	@Test
	public void exportExcelFileForSqlTest(){

		String path = "/home/afrodite/dev/teste";
		String fileName = "exportacao-test-2";

		File file = null;

		try {
			file = iExcelExports.exportsForLocalPath("SELECT * FROM CLIENTE", path, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (GeneralException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(file);

	}

	@Test
	public void exportExcelFileForSqlColumnsTest(){

		String path = "/home/afrodite/dev/teste";
		String fileName = "exportacao-test-3";

		File file = null;

		try {
			file = iExcelExports.exportsForLocalPath("SELECT nome AS NOME FROM CLIENTE", path, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (GeneralException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(file);

	}

	@After
	public void postTest(){
		iUtilTest.dropDataBase();
	}

}
