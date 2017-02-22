package spwrap;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

import com.zaxxer.hikari.HikariDataSource;

public class TestUtils {

	static HikariDataSource dataSource = new HikariDataSource();

	private static final String INSTALL_SQL_PATH = "src/test/resources/install.sql";
	private static final String ROLLBACK_SQL_PATH = "src/test/resources/rollback.sql";

	static void install() {
		executeScript(INSTALL_SQL_PATH);
	}

	static void rollback() {
		executeScript(ROLLBACK_SQL_PATH);
	}

	private static void executeScript(String scriptPath) {
		Connection connection = null;
		Statement stmt = null;
		Scanner scanner = null;

		try {
			connection = dataSource.getConnection();
			stmt = connection.createStatement();

			scanner = new Scanner(new File(scriptPath));
			String content = scanner.useDelimiter("\\Z").next();

			String[] split = content.split(";;");

			for (String sql : split) {
				// System.out.println(sql);
				stmt.execute(sql.trim());
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			Util.closeDBObjects(connection, stmt, null);
		}
	}

	static {
		dataSource.setJdbcUrl("jdbc:hsqldb:mem:customers");
	}
}
