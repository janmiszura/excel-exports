package com.gleidsonfersanp.db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gleidsonfersanp.db.connection.EXEXConnectionServiceImpl;
import com.gleidsonfersanp.db.connection.EXEXDataSource;
import com.gleidsonfersanp.db.connection.EXEXDataSourceBuilder;
import com.gleidsonfersanp.db.connection.IEXEXConnectionService;
import com.gleidsonfersanp.db.query.ExportColumnQuery;
import com.gleidsonfersanp.db.query.ExportColumnResult;
import com.gleidsonfersanp.db.query.ExportQuery;
import com.gleidsonfersanp.db.query.ExportResultQuery;
import com.gleidsonfersanp.extra.exception.GeneralException;

public class EXEXDaoImpl implements IEXEXDao{

	private IEXEXConnectionService connectionService;

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

	public ExportResultQuery executeQuery(ExportQuery exportQuery) throws SQLException {

		String sql = generateASqlQuery(exportQuery);

		ResultSet resultSet = null;

		try {
			resultSet = executeSql(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return createExportsResultQuery(resultSet, exportQuery);
	}

	public ExportResultQuery executeQuery(String sql) throws SQLException {

		ResultSet resultSet = null;

		try {
			resultSet = executeSql(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return createExportsResultQuery(resultSet, null);
	}

	public ResultSet executeSql(String sql) throws SQLException{

		Connection connection = connectionService.getConnection();

		ResultSet resultSet = connection.createStatement().executeQuery(sql);

		connection.close();

		return resultSet;
	}

	private ExportResultQuery createExportsResultQuery(ResultSet resultSet,ExportQuery exportQuery) throws SQLException {

		List<ExportColumnResult>columnResults = new ArrayList<ExportColumnResult>();

		if(exportQuery != null){

			for (ExportColumnQuery query : exportQuery.getColumnQuerys()) {
				columnResults.add(new ExportColumnResult(query.getColumnAlias(),new ArrayList<Object>()));
			}

			if(!exportQuery.getColumnQuerys().isEmpty()){

				columnResults = getListOfExportColumns(resultSet);
			}

			return new ExportResultQuery(columnResults);
		}

		columnResults = getListOfExportColumns(resultSet);

		return new ExportResultQuery(columnResults);
	}

	private List<ExportColumnResult> getListOfExportColumns(ResultSet resultSet) throws SQLException {

		List<ExportColumnResult>columnResults = new ArrayList<ExportColumnResult>();

		boolean flag = false;
		while (resultSet.next()) {

			ResultSetMetaData rsmd = resultSet.getMetaData();

			int columnCount = rsmd.getColumnCount();

			if(columnCount == 1 && !flag){
				String columnName = rsmd.getColumnName(1);
				ExportColumnResult columnResult = new ExportColumnResult(columnName);
				columnResults.add(columnResult);
				flag = true;
			}

			if(!flag){

				for (int i = 1; i < columnCount; i++) {
					String columnName = rsmd.getColumnName(i);
					ExportColumnResult columnResult = new ExportColumnResult(columnName);
					columnResults.add(columnResult);

				}
				flag = true;
			}

			for (ExportColumnResult columnResult : columnResults) {
				columnResult.getObjects().add(resultSet.getObject(columnResult.getName()));
			}
		}

		return columnResults;
	}

	public String generateASqlQuery(ExportQuery exportQuery) {

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
