package spwrap;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

import spwrap.db.DBInterface;
import spwrap.db.HSQLInterface;
import spwrap.db.MySQLInterface;

public class TestUtils {

    public enum TestDB {
        HSQL(new HSQLInterface()),
        MYSQL(new MySQLInterface());

        DBInterface dbInterface;

        TestDB(DBInterface dbInterface) {
            this.dbInterface = dbInterface;
        }
    }

	static void install(TestDB testDb) {
		executeScript(testDb, testDb.dbInterface.installScript());
	}

	static void rollback(TestDB testDb) {
		executeScript(testDb, testDb.dbInterface.rollbackScript());
	}

	private static void executeScript(TestDB testDb, String scriptPath) {
		Connection connection = null;
		Statement stmt = null;
		Scanner scanner = null;

		try {
			connection = testDb.dbInterface.dataSource().getConnection();
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
}
