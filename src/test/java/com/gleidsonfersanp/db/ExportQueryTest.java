package com.gleidsonfersanp.db;

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

public class ExportQueryTest {

	private IEXEXDao dao;

	private IDBUtilTest iUtilTest;

	@Before
	public void init(){

		try {

			iUtilTest = new DBUtilTestImpl();

			iUtilTest.createDataBase();

			iUtilTest.injectData();

			dao = new EXEXDaoImpl(iUtilTest.getDataSource());


		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void sqlCreatorTest(){

		String sql = null;

		try {
			sql = generateASqlQuery(
					new ExportQueryBuilder()
					.table("cliente")
					.build()
					);
		} catch (GeneralException e) {
			e.printStackTrace();
		}

		Assert.assertEquals(" SELECT * FROM cliente ", sql);

	}

	@Test
	public void sqlWithOneColumnQueriesTest(){

		String sql = null;

		try {
			sql = generateASqlQuery(
					new ExportQueryBuilder()
					.table("cliente")
					.columnQueries(new ExportColumnQuery("nome","NAME"))
					.build()
					);
		} catch (GeneralException e) {
			e.printStackTrace();
		}

		Assert.assertEquals(" SELECT nome AS NAME FROM cliente ", sql);

	}

	@Test
	public void sqlWithTwoColumnQueriesTest(){

		String sql = null;

		try {
			sql = generateASqlQuery(
					new ExportQueryBuilder()
					.table("cliente")
					.columnQueries(new ExportColumnQuery("nome","NAME"))
					.columnQueries(new ExportColumnQuery("idade","AGE"))
					.build()
					);
		} catch (GeneralException e) {
			e.printStackTrace();
		}

		Assert.assertEquals(" SELECT nome AS NAME, idade AS AGE FROM cliente ", sql);

	}

	@Test
	public void sqlWithThreeColumnQueriesTest(){

		String sql = null;

		try {
			sql = generateASqlQuery(
					new ExportQueryBuilder()
					.table("cliente")
					.columnQueries(new ExportColumnQuery("nome","NAME"))
					.columnQueries(new ExportColumnQuery("idade","AGE"))
					.columnQueries(new ExportColumnQuery("email","EMAIL"))
					.build()
					);
		} catch (GeneralException e) {
			e.printStackTrace();
		}

		Assert.assertEquals(" SELECT nome AS NAME, idade AS AGE, email AS EMAIL FROM cliente ", sql);

	}

	@Test
	public void sqlWithFourColumnQueriesTest(){

		String sql = null;

		try {
			sql = generateASqlQuery(
					new ExportQueryBuilder()
					.table("cliente")
					.columnQueries(new ExportColumnQuery("nome","NAME"))
					.columnQueries(new ExportColumnQuery("idade","AGE"))
					.columnQueries(new ExportColumnQuery("email","EMAIL"))
					.columnQueries(new ExportColumnQuery("telefone","FONE"))
					.build()
					);
		} catch (GeneralException e) {
			e.printStackTrace();
		}

		Assert.assertEquals(" SELECT nome AS NAME, idade AS AGE, email AS EMAIL, telefone AS FONE FROM cliente ", sql);

	}

	@Test
	public void getExportResultQueryTest() throws GeneralException, SQLException{

		ExportResultQuery exportResultQuery = dao.executeQuery(new ExportQueryBuilder()
				.table("cliente")
				.columnQueries(new ExportColumnQuery("nome","NAME"))
				.columnQueries(new ExportColumnQuery("idade","AGE"))
				.columnQueries(new ExportColumnQuery("email","EMAIL"))
				.columnQueries(new ExportColumnQuery("telefone","FONE"))
				.build());



		Assert.assertNotNull(exportResultQuery);

	}

	@After
	public void postTest(){
		iUtilTest.dropDataBase();
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
