package com.gleidsonfersanp.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.gleidsonfersanp.db.query.ExportQuery;
import com.gleidsonfersanp.db.query.ExportResultQuery;

public interface IEXEXDao {

	ExportResultQuery executeQuery(ExportQuery exportQuery) throws SQLException;
	ResultSet executeSql(String sql) throws SQLException;
	String generateASqlQuery(ExportQuery exportQuery);

}
