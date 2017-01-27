package com.gleidsonfersanp.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class EXEXConnectionServiceImpl implements IEXEXConnectionService{

	final static Logger logger = Logger.getLogger(EXEXConnectionServiceImpl.class);

	private static final String DRIVER_POSTGRES = "org.postgresql.Driver";
	private static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";

	private EXEXDataSource dataSource;

	public EXEXConnectionServiceImpl(EXEXDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Connection getConnection() {

		logger.info("-------- "+dataSource.getDataBase().toString()+" JDBC Connection Testing ------------");

		try {
			Class.forName(dataSource.getDataBase().equals(DataBase.POSTGRES) ? DRIVER_POSTGRES:DRIVER_MYSQL);
		} catch (ClassNotFoundException e) {
			logger.error("Where is your "+dataSource.getDataBase()+" JDBC Driver? Include in your library path!", e);
			return null;
		}

		logger.info(dataSource.getDataBase()+" JDBC Driver Registered!");

		Connection connection = null;

		try {

			connection = DriverManager.getConnection(
					dataSource.getUrl(),
					dataSource.getUser(),
					dataSource.getPassword());

		} catch (SQLException e) {
			logger.error("Connection Failed! Check output console");
			return null;
		}

		return connection;
	}

}
