package spwrap;

import static spwrap.Caller.*;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zaxxer.hikari.HikariDataSource;

import spwrap.Caller.OutputParamMapper;
import spwrap.Caller.ResultSetMapper;

//RUN src/test/resources/sqlserver_script.sql first

public class TestCallerMysql {

	private Caller dsCaller;
	private Caller jdbcCaller;

	private final OutputParamMapper<DateHolder> DATA_HOLDER_MAPPER = new OutputParamMapper<DateHolder>() {

		@Override
		public DateHolder map(CallableStatement call, int index) throws SQLException {
			DateHolder holder = new DateHolder();
			holder.s1 = call.getString(index);
			holder.s2 = call.getString(index + 1);
			holder.l1 = call.getLong(index + 2);
			return holder;
		}
	};

	@Before
	public void setup() {
		System.setProperty("spwarp.success_code", "0");
		
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
		ds.setUsername("root");
		ds.setPassword("");

		dsCaller = new Caller(ds);
		
		jdbcCaller = new Caller("jdbc:mysql://localhost:3306/testdb", "root", "");
	}

	@Test
	public void test5() {

		Result<SPInfo, DateHolder> result = dsCaller.call("OUTPUT_WITH_RS", null,
				paramTypes(Types.VARCHAR, Types.VARCHAR, Types.BIGINT), DATA_HOLDER_MAPPER,
				new ResultSetMapper<SPInfo>() {

					@Override
					public SPInfo map(ResultSet rs) {
						try {
							return new SPInfo(rs.getString(1), rs.getTimestamp(2));
						} catch (SQLException e) {
							e.printStackTrace();
							return null;
						}
					}
				});

		Assert.assertEquals("HELLO", result.object().s1);
		Assert.assertEquals("WORLD", result.object().s2);
		Assert.assertEquals(99, result.object().l1);

		Assert.assertTrue(result.list().size() >= 40);
		
		
		result = jdbcCaller.call("OUTPUT_WITH_RS", null,
				paramTypes(Types.VARCHAR, Types.VARCHAR, Types.BIGINT), DATA_HOLDER_MAPPER,
				new ResultSetMapper<SPInfo>() {

					@Override
					public SPInfo map(ResultSet rs) {
						try {
							return new SPInfo(rs.getString(1), rs.getTimestamp(2));
						} catch (SQLException e) {
							e.printStackTrace();
							return null;
						}
					}
				});

		Assert.assertEquals("HELLO", result.object().s1);
		Assert.assertEquals("WORLD", result.object().s2);
		Assert.assertEquals(99, result.object().l1);

		Assert.assertTrue(result.list().size() >= 4);
	}

	// -----------------------------

	static class DateHolder {
		String s1, s2;
		long l1;
		@Override
		public String toString() {
			return "DateHolder [s1=" + s1 + ", s2=" + s2 + ", l1=" + l1 + "]";
		}
	}

	static class SPInfo {
		String spName;
		Timestamp created;

		public SPInfo(String spName, Timestamp created) {
			super();
			this.spName = spName;
			this.created = created;
		}

		@Override
		public String toString() {
			return "[name: " + spName + ", created: " + created + "]";
		}
	}
}
