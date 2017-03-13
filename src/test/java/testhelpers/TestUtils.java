package testhelpers;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TestUtils {

    public static void install(TestDB testDb) {
		executeScript(testDb, testDb.dbInfo.installScript());
	}

    public static void rollback(TestDB testDb) {
		executeScript(testDb, testDb.dbInfo.rollbackScript());
	}

	private static void executeScript(TestDB testDb, String scriptPath) {

		Connection connection = null;
		Statement stmt;
		Scanner scanner = null;

		try {
			connection = testDb.dbInfo.dataSource().getConnection();
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
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
}
