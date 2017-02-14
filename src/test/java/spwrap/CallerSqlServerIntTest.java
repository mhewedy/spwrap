package spwrap;

import static spwrap.Caller.*;
import static spwrap.Caller.Param.*;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zaxxer.hikari.HikariDataSource;

import spwrap.Caller.OutputParamMapper;
import spwrap.Caller.ResultSetMapper;

//RUN src/test/resources/sqlserver_script.sql first

public class CallerSqlServerIntTest {

	private Caller dsCaller;

	private final OutputParamMapper<DateHolder> DATA_HOLDER_MAPPER = new OutputParamMapper<DateHolder>() {

		@Override
		public DateHolder map(Result result, int index) {
			DateHolder holder = new DateHolder();
			holder.s1 = result.getString(index);
			holder.s2 = result.getString(index + 1);
			holder.l1 = result.getLong(index + 2);
			return holder;
		}
	};

	@Before
	public void setup() {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:sqlserver://192.168.43.12:1433;DatabaseName=TEST");
		ds.setUsername("test");
		ds.setPassword("test");

		dsCaller = new Caller(ds);
	}

	@Test
	public void test1() {
		dsCaller.call("EXE_NO_IN_NO_OUT");
	}

	@Test(expected = CallException.class)
	public void test2() {
		dsCaller.call("NOT_FOUND");
	}

	@Test
	public void test3() {
		String value = "hello";
		List<String> call = dsCaller.call("ECHO", params(of(value, Types.VARCHAR)), new ResultSetMapper<String>() {

			@Override
			public String map(Result result) {
				return result.getString(1);
			}
		});

		Assert.assertEquals(value, call.get(0));
	}

	@Test
	public void test4() {

		DateHolder result = dsCaller.call("OUTPUT", paramTypes(Types.VARCHAR, Types.VARCHAR, Types.BIGINT),
				DATA_HOLDER_MAPPER);

		Assert.assertEquals("HELLO", result.s1);
		Assert.assertEquals("WORLD", result.s2);
		Assert.assertEquals(99, result.l1);

	}

	@Test
	public void test5() {

		Tuple<SPInfo, DateHolder> result = dsCaller.call("OUTPUT_WITH_RS", null,
				paramTypes(Types.VARCHAR, Types.VARCHAR, Types.BIGINT), DATA_HOLDER_MAPPER,
				new ResultSetMapper<SPInfo>() {

					@Override
					public SPInfo map(Result result) {
						return new SPInfo(result.getString(1), result.getTimestamp(2));
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
	}

	@Test
	public void test7() {
		String value = "hello";
		String call = dsCaller.call("ECHO2", params(of(value, Types.VARCHAR)), paramTypes(Types.VARCHAR),
				new OutputParamMapper<String>() {

					@Override
					public String map(Result result, int index) {
						return result.getString(index);
					}
				});

		Assert.assertEquals(value, call);
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
