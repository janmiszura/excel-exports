package com.gleidsonfersanp.extra;

import java.sql.Connection;
import java.sql.SQLException;

import com.gleidsonfersanp.db.connection.DataBase;
import com.gleidsonfersanp.db.connection.EXEXConnectionServiceImpl;
import com.gleidsonfersanp.db.connection.EXEXDataSource;
import com.gleidsonfersanp.db.connection.EXEXDataSourceBuilder;
import com.gleidsonfersanp.db.connection.IEXEXConnectionService;
import com.gleidsonfersanp.extra.exception.GeneralException;

public class DBUtilTestImpl implements IDBUtilTest{

	private EXEXDataSource dataSource;

	private static final String DATA_BASE_URL = "jdbc:postgresql://192.168.254.5:5432/aaaaexcellib";
	private static final String DATA_BASE_USER = "postgres";
	private static final String DATA_BASE_PASSWORD = "12345";
	private static final DataBase DATABASE = DataBase.POSTGRES;


	private static final String SQL_CREATE_TABLE =
			"CREATE TABLE cliente ( codigo char(5) CONSTRAINT firstkey PRIMARY KEY,"
					+" nome varchar(40) NOT NULL, "
					+" idade integer NOT NULL, "
					+" datanascimento date, "
					+" telefone varchar(30), "
					+" email varchar(30)); ";

	private static final String SQL_INSERT =
			"INSERT INTO cliente VALUES ('UA502', 'Michelangelo', 85, '1971-07-13', '(62) 9999-9999', 'michelangelo@email.com');"
			+"INSERT INTO cliente VALUES ('UA503', 'Paulo Coelho', 69, '1971-07-13', '(62) 9999-9999', 'paulo@email.com');"
			+"INSERT INTO cliente VALUES ('UA504', 'José de Alencar', 76, '1971-07-14', '(62) 9999-9999', 'jose@email.com');"
			+"INSERT INTO cliente VALUES ('UA505', 'Vinicios de Moraes', 45, '1971-07-15', '(62) 9999-9999', 'vinicius@email.com');"
			+"INSERT INTO cliente VALUES ('UA506', 'Cumpade Washington', 69, '1971-07-16', '(62) 9999-9999', 'cumpade@email.com');"
			+"INSERT INTO cliente VALUES ('UA507', 'Cumpade Washington', 69, '1971-07-16', '(62) 9999-9999', 'cumpade1@email.com');"
			+"INSERT INTO cliente VALUES ('UA508', 'Cumpade Washington2', 69, '1971-07-16', '(62) 9999-9999', 'cumpade2@email.com');"
			+"INSERT INTO cliente VALUES ('UA509', 'Cumpade Washington3', 69, '1971-07-16', '(62) 9999-9999', 'cumpade3@email.com');"
			+"INSERT INTO cliente VALUES ('UA510', 'Cumpade Washington4', 69, '1971-07-16', '(62) 9999-9999', 'cumpade4@email.com');"
			+"INSERT INTO cliente VALUES ('UA511', 'Cumpade Washington5', 69, '1971-07-16', '(62) 9999-9999', 'cumpade5@email.com');"
			+"INSERT INTO cliente VALUES ('UA512', 'Cumpade Washington6', 69, '1971-07-16', '(62) 9999-9999', 'cumpade6@email.com');";

	private static final String SQL_DROP_TABLE = "DROP TABLE cliente";

	private IEXEXConnectionService connectionService;

	public DBUtilTestImpl() throws GeneralException {

		this.dataSource = new EXEXDataSourceBuilder()
				.dataBase(DATABASE)
				.url(DATA_BASE_URL)
				.user(DATA_BASE_USER)
				.password(DATA_BASE_PASSWORD)
				.build();

		connectionService = new EXEXConnectionServiceImpl(dataSource);

	}

	public void createDataBase() throws SQLException {

		Connection connection = connectionService.getConnection();

		connection.createStatement().executeUpdate(SQL_CREATE_TABLE);

		connection.close();

	}

	public void injectData() throws SQLException {

		Connection connection = connectionService.getConnection();

		connection.createStatement().executeUpdate(SQL_INSERT);

		connection.close();

	}

	public void dropDataBase() {
		try {

			Connection connection = connectionService.getConnection();

			connection.createStatement().executeUpdate(SQL_DROP_TABLE);

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public EXEXDataSource getDataSource() {
		return this.dataSource;
	}

}
