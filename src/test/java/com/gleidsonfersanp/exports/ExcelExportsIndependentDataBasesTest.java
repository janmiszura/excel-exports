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
					.dataBase(DataBase.MYSQL)
					.user("root")
					.url("jdbc:mysql://192.168.254.5:3306/icf2013")
					.password("12345")
					.build());
		} catch (GeneralException e) {
			e.printStackTrace();
		}

		/*try {
			excelExports = new ExcelExportsImpl(
					new EXEXDataSourceBuilder()
					.dataBase(DataBase.POSTGRES)
					.url("jdbc:postgresql://192.168.254.5:5432/ipog-11-10-16")
					.user("postgres")
					.password("12345")
					.build());
		} catch (GeneralException e) {
			e.printStackTrace();
		}*/
	}

	private String formarSqlDaConsulta() {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT q.numero, q.enunciado,r.servico,r.textoresposta AS nota, r.textocomplementar AS obs, e.nome, e.email ");
		sb.append(" FROM icf_pesquisa_x_entrevistado AS pxe ");
		sb.append(" INNER JOIN icf_entrevistado AS e ON e.id = pxe.entrevistado_id ");
		sb.append(" INNER JOIN icf_respostaquestao AS r ON r.entrevistado_id = e.id ");
		sb.append(" INNER JOIN icf_questaopesquisa AS q ON q.id = r.questao_id ");
		sb.append(" WHERE  pesquisaSatisfacao_id = ");
		sb.append(1);
		sb.append(" ORDER BY e.nome");

/*		StringBuilder sb = new StringBuilder();

		sb.append(" SELECT e.id, e.nome, data, latitude, longitude, endereco FROM setec_executorcheckin AS c ");
		sb.append(" LEFT JOIN localizacao AS l ON l.id = c.localizacao_id ");
		sb.append(" INNER JOIN adminusuario AS e ON e.id = c.executor_id ");
		sb.append(" WHERE executor_id ");
		sb.append(" IN ");
		sb.append("(");
		sb.append("SELECT id FROM adminusuario WHERE perfil_id IN (SELECT p.id FROM adminperfil AS p WHERE p.permissoesJson ILIKE '%ehExecutor%')");
		sb.append(")");
		sb.append(" ORDER BY data DESC ");*/

		return sb.toString();
	}

	@Test
	public void otherDataBaseTest(){

		//String sql = "SELECT * FROM magnus_questao";
		String sql = formarSqlDaConsulta();

		try {
			excelExports.writeFileForLocalPath(sql, "/home/afrodite/dev/teste", "exportacao-icf.xlsx");

		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
