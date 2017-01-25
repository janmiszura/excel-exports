package com.gleidsonfersanp.db;

import java.sql.Connection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gleidsonfersanp.db.connection.DataBase;
import com.gleidsonfersanp.db.connection.EXEXConnectionServiceImpl;
import com.gleidsonfersanp.db.connection.EXEXDataSourceBuilder;
import com.gleidsonfersanp.db.connection.IEXEXConnectionService;
import com.gleidsonfersanp.extra.exception.GeneralException;


public class ConnectionTest {

	private IEXEXConnectionService connectionService;
	private static final String URL = "jdbc:postgresql://192.168.254.5:5432/aaaaexcellib";
	private static final String USER = "postgres";
	private static final String PASSWORD = "12345";

	@Before
	public void init(){
		try {
			connectionService = new EXEXConnectionServiceImpl(
					new EXEXDataSourceBuilder()
					.dataBase(DataBase.POSTGRES)
					.url(URL)
					.user(USER)
					.password(PASSWORD)
					.build()
					);

			Assert.assertNotNull(connectionService);

		} catch (GeneralException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void connectionTest(){

		Connection connection = connectionService.getConnection();

		Assert.assertNotNull(connection);

	}
}
