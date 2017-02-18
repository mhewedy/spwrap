package spwrap;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

import com.zaxxer.hikari.HikariDataSource;

public class TestUtils {
	
	static HikariDataSource ds = new HikariDataSource();

	private static final String SQL_PATH = "src/test/resources/schema.sql";

	static void loadData() {
		
		ds.setJdbcUrl("jdbc:hsqldb:mem:customers");

		Connection connection = null;
		Statement stmt = null;
		Scanner scanner = null;

		try {
			connection = ds.getConnection();
			stmt = connection.createStatement();

			scanner = new Scanner(new File(SQL_PATH));
			String content = scanner.useDelimiter("\\Z").next();
			
			String[] split = content.split(";;");
			
			for (String sql : split) {
				System.out.println(sql);
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
}
