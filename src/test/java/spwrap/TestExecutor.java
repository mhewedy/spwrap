package spwrap;

import static spwrap.Executor.paramTypes;
import static spwrap.Executor.params;
import static spwrap.Executor.Param.of;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zaxxer.hikari.HikariDataSource;

import spwrap.Executor.OutputParamMapper;
import spwrap.Executor.ResultSetMapper;
import spwrap.result.ListObjectResult;
import spwrap.result.ObjectResult;
import spwrap.result.Result;

//RUN src/test/resources/script.sql first

public class TestExecutor {

	private Executor executor;

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
		ds.setJdbcUrl("jdbc:sqlserver://192.168.43.12:1433;DatabaseName=TEST");
		ds.setUsername("test");
		ds.setPassword("test");

		executor = new Executor(ds);
	}

	@Test
	public void test1() {
		Result result = executor.execute("EXE_NO_IN_NO_OUT");
		Assert.assertTrue(result.isSuccess());
	}

	@Test
	public void test2() {
		String echoMessage = "HELLO";
		Result result = executor.execute("ECHO", params(of(echoMessage, Types.VARCHAR)));
		Assert.assertEquals(echoMessage, result.getMessage());
	}

	@Test
	public void test3() {

		ObjectResult<DateHolder> result = executor.execute("OUTPUT",
				paramTypes(Types.VARCHAR, Types.VARCHAR, Types.BIGINT), DATA_HOLDER_MAPPER);

		Assert.assertTrue(result.isSuccess());
		Assert.assertEquals("HELLO", result.getObject().s1);
		Assert.assertEquals("WORLD", result.getObject().s2);
		Assert.assertEquals(99, result.getObject().l1);
	}

	@Test
	public void test4() {

		ListObjectResult<SPInfo, DateHolder> result = executor.execute("OUTPUT_WITH_RS", null,
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

		Assert.assertTrue(result.isSuccess());
		Assert.assertEquals("HELLO", result.getObject().s1);
		Assert.assertEquals("WORLD", result.getObject().s2);
		Assert.assertEquals(99, result.getObject().l1);

		Assert.assertTrue(result.getList().size() >= 4);
	}

	@Test
	public void test5() {
		Result result = executor.execute("NOT_FOUND");
		Assert.assertTrue(result.isSuccess() == false);
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
