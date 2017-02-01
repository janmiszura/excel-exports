package com.gleidsonfersanp.db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gleidsonfersanp.db.connection.EXEXConnectionServiceImpl;
import com.gleidsonfersanp.db.connection.EXEXDataSource;
import com.gleidsonfersanp.db.connection.EXEXDataSourceBuilder;
import com.gleidsonfersanp.db.connection.IEXEXConnectionService;
import com.gleidsonfersanp.db.query.ExportColumnResult;
import com.gleidsonfersanp.db.query.ExportQuery;
import com.gleidsonfersanp.db.query.ExportResultQuery;
import com.gleidsonfersanp.extra.exception.GeneralException;

public class EXEXDaoImpl implements IEXEXDao{

	private IEXEXConnectionService connectionService;
	private List<ExportColumnResult>columnResults;
	private Connection connection;

	public EXEXDaoImpl(EXEXDataSource dataSource) throws GeneralException {

		connectionService = new EXEXConnectionServiceImpl(
				new EXEXDataSourceBuilder()
				.dataBase(dataSource.getDataBase())
				.url(dataSource.getUrl())
				.user(dataSource.getUser())
				.password(dataSource.getPassword())
				.build()
				);
	}

	private void openConnection(){
		connection = connectionService.getConnection();
	}

	private void closeConnection() throws SQLException{
		connection.close();
	}

	public ExportResultQuery executeQuery(ExportQuery exportQuery) throws SQLException {
		openConnection();

		ExportResultQuery exportResultQuery = null;

		try {
			exportResultQuery = createExportsResultQuery(exportQuery);
		} finally {
			closeConnection();
		}

		return exportResultQuery;
	}

	public ExportResultQuery executeQuery(String sql) throws SQLException {

		openConnection();

		ExportResultQuery exportResultQuery = null;

		try {
			exportResultQuery = createExportsResultQuery(sql);
		} finally {
			closeConnection();
		}

		return exportResultQuery;
	}

	private ResultSet executeSql(String sql) throws SQLException{

		Statement statement = connection.createStatement();

		return statement.executeQuery(sql);
	}

	private ExportResultQuery createExportsResultQuery(ExportQuery exportQuery) throws SQLException {

		String sql = generateASqlQuery(exportQuery);

		return createExportsResultQuery(sql);
	}

	private ExportResultQuery createExportsResultQuery(String sql) throws SQLException {

		extractColumns(sql);

		return new ExportResultQuery(columnResults);
	}

	private void extractColumns(String sql) throws SQLException {

		ResultSet resultSet = executeSql(sql);
		ResultSetMetaData rsmd = resultSet.getMetaData();

		int columnCount = rsmd.getColumnCount();

		columnResults = new ArrayList<ExportColumnResult>();

		for (int i = 1; i <= columnCount; i++) {
			String columnName = rsmd.getColumnLabel(i);
			ExportColumnResult columnResult = new ExportColumnResult(columnName);
			columnResults.add(columnResult);
		}

		while (resultSet.next()) {

			for (ExportColumnResult columnResult : columnResults) {
				columnResult.getObjects().add(resultSet.getObject(columnResult.getName()));
			}

		}

	}

	private String generateASqlQuery(ExportQuery exportQuery) {

		StringBuilder sb = new StringBuilder();

		sb.append(" SELECT ");

		if(exportQuery.getColumnQuerys().isEmpty()){
			sb.append("*");
		}else{

			for (int i = 0; i < exportQuery.getColumnQuerys().size(); i++) {

				sb.append(exportQuery.getColumnQuerys().get(i).getColumnName());
				sb.append(" ");
				sb.append("AS");
				sb.append(" ");
				sb.append(exportQuery.getColumnQuerys().get(i).getColumnAlias());

				if(i < (exportQuery.getColumnQuerys().size() - 1)){
					sb.append(", ");
				}

			}
		}

		sb.append(" ");
		sb.append("FROM");
		sb.append(" ");
		sb.append(exportQuery.getTable());
		sb.append(" ");

		return sb.toString();
	}

}
