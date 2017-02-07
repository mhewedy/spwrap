package spwrap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

	private static Logger log = LoggerFactory.getLogger(Util.class);

	static String createCallableString(String procName, int paramsCount) {
		StringBuffer call = new StringBuffer();

		call.append("{call ").append(procName).append("(");

		for (int i = 0; i < paramsCount; i++) {
			if (i > 0) {
				call.append(", ");
			}
			call.append("?");
		}

		call.append(")}");

		return call.toString();
	}

	static void closeDBObjects(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}
}
