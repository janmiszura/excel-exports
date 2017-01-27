package com.gleidsonfersanp.exports;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.gleidsonfersanp.db.connection.DataBase;
import com.gleidsonfersanp.db.connection.EXEXDataSourceBuilder;
import com.gleidsonfersanp.extra.exception.GeneralException;

public class ExcelExportsIndependentDataBasesTest {

	private IExcelExports excelExports;

	@Before
	public void init(){

		try {
			excelExports = new ExcelExportsImpl(
					new EXEXDataSourceBuilder()
					.dataBase(DataBase.POSTGRES)
					.user("postgres")
					.url("jdbc:postgresql://192.168.254.5:5432/magnus")
					.password("12345")
					.build());
		} catch (GeneralException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void otherDataBaseTest(){

		try {
			excelExports.writeFileForLocalPath("SELECT * FROM magnus_questao", "/home/afrodite/dev/teste", "exportacao-contato.xls");

		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
