package spwrap;

import static spwrap.Caller.*;
import static spwrap.Caller.Param.*;

import java.sql.CallableStatement;
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

public class TestCallerSqlServer {

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
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:sqlserver://localhost:1433;DatabaseName=TEST");
		ds.setUsername("test");
		ds.setPassword("test");

		dsCaller = new Caller(ds);

		jdbcCaller = new Caller("jdbc:sqlserver://localhost:1433;DatabaseName=TEST", "test", "test");
	}

	@Test
	public void test1() {
		dsCaller.call("EXE_NO_IN_NO_OUT");
		jdbcCaller.call("EXE_NO_IN_NO_OUT");
	}

	@Test(expected = CallException.class)
	public void test2() {
		dsCaller.call("NOT_FOUND");
		jdbcCaller.call("NOT_FOUND");
	}

	@Test
	public void test3() {
		dsCaller.call("ECHO", params(of("hello", Types.VARCHAR)));
		jdbcCaller.call("ECHO", params(of("hello", Types.VARCHAR)));
	}

	@Test
	public void test4() {

		DateHolder result = dsCaller.call("OUTPUT", paramTypes(Types.VARCHAR, Types.VARCHAR, Types.BIGINT),
				DATA_HOLDER_MAPPER);

		Assert.assertEquals("HELLO", result.s1);
		Assert.assertEquals("WORLD", result.s2);
		Assert.assertEquals(99, result.l1);

		result = jdbcCaller.call("OUTPUT", paramTypes(Types.VARCHAR, Types.VARCHAR, Types.BIGINT), DATA_HOLDER_MAPPER);

		Assert.assertEquals("HELLO", result.s1);
		Assert.assertEquals("WORLD", result.s2);
		Assert.assertEquals(99, result.l1);
	}

	@Test
	public void test5() {

		Result<SPInfo, DateHolder> result = dsCaller.call("OUTPUT_WITH_RS", null,
				paramTypes(Types.VARCHAR, Types.VARCHAR, Types.BIGINT), DATA_HOLDER_MAPPER,
				new ResultSetMapper<SPInfo>() {

					@Override
					public SPInfo map(ResultSet rs) {
						return new SPInfo(rs.getString(1), rs.getTimestamp(2));
					}
				});

		Assert.assertEquals("HELLO", result.object().s1);
		Assert.assertEquals("WORLD", result.object().s2);
		Assert.assertEquals(99, result.object().l1);

		Assert.assertTrue(result.list().size() >= 4);

		result = jdbcCaller.call("OUTPUT_WITH_RS", null, paramTypes(Types.VARCHAR, Types.VARCHAR, Types.BIGINT),
				DATA_HOLDER_MAPPER, new ResultSetMapper<SPInfo>() {

					@Override
					public SPInfo map(ResultSet rs) {
						return new SPInfo(rs.getString(1), rs.getTimestamp(2));
					}
				});

		Assert.assertEquals("HELLO", result.object().s1);
		Assert.assertEquals("WORLD", result.object().s2);
		Assert.assertEquals(99, result.object().l1);

		Assert.assertTrue(result.list().size() >= 4);
	}

	@Test(expected = CallException.class)
	public void test6() {
		dsCaller.call("SP_WITH_INT_OUTPUT");
		jdbcCaller.call("SP_WITH_INT_OUTPUT");
	}

	// -----------------------------

	static class DateHolder {
		String s1, s2;
		long l1;
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
