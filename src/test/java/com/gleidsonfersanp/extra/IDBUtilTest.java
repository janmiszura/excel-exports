package com.gleidsonfersanp.extra;

import java.sql.SQLException;

import com.gleidsonfersanp.db.connection.EXEXDataSource;

public interface IDBUtilTest {

	EXEXDataSource getDataSource();
	void createDataBase() throws SQLException;
	void injectData() throws SQLException;
	void dropDataBase();

}
