package com.gleidsonfersanp.db.dao;

import java.sql.SQLException;

import com.gleidsonfersanp.db.query.ExportQuery;
import com.gleidsonfersanp.db.query.ExportResultQuery;

public interface IEXEXDao {

	ExportResultQuery executeQuery(ExportQuery exportQuery) throws SQLException;
	ExportResultQuery executeQuery(String sql) throws SQLException;

}
