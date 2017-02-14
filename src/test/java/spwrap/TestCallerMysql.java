package spwrap;

import static java.sql.Types.BIGINT;
import static java.sql.Types.VARCHAR;
import static spwrap.Caller.paramTypes;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zaxxer.hikari.HikariDataSource;

import spwrap.Caller.ResultSetMapper;
import spwrap.annotations.Mapper;
import spwrap.annotations.Mapper.TypedOutputParamMapper;
import spwrap.annotations.StoredProc;

//RUN src/test/resources/mysql_script.sql first

public class TestCallerMysql {

	private Caller dsCaller;

	@Before
	public void setup() {
		System.setProperty("spwarp.success_code", "0");

		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
		ds.setUsername("root");
		ds.setPassword("");

		dsCaller = new Caller(ds);
	}

	@Test
	public void test5() {

		Result<SPInfo, DateHolder> result = dsCaller.call("OUTPUT_WITH_RS", null, paramTypes(VARCHAR, VARCHAR, BIGINT),
				new DateHolder(), new SPInfo());

		Assert.assertEquals("HELLO", result.object().s1);
		Assert.assertEquals("WORLD", result.object().s2);
		Assert.assertEquals(99, result.object().l1);

		Assert.assertTrue(result.list().size() >= 40);
	}

	interface MyService {
		@Mapper(resultSet = SPInfo.class, outParams = DateHolder.class)
		@StoredProc("OUTPUT_WITH_RS")
		Result<SPInfo, DateHolder> callMySp();
	}

	@Test
	public void test6() {

		MyService myService = dsCaller.create(MyService.class);
		Result<SPInfo, DateHolder> result = myService.callMySp();

		Assert.assertEquals("HELLO", result.object().s1);
		Assert.assertEquals("WORLD", result.object().s2);
		Assert.assertEquals(99, result.object().l1);

		Assert.assertTrue(result.list().size() >= 40);
	}

	// -----------------------------

	public static class DateHolder implements TypedOutputParamMapper<DateHolder> {
		private String s1, s2;
		private long l1;

		@Override
		public DateHolder map(ResultSet call, int index) {
			DateHolder holder = new DateHolder();
			holder.s1 = call.getString(index);
			holder.s2 = call.getString(index + 1);
			holder.l1 = call.getLong(index + 2);
			return holder;
		}

		@Override
		public List<Integer> getSQLTypes() {
			return Arrays.asList(VARCHAR, VARCHAR, BIGINT);
		}

		@Override
		public String toString() {
			return "DateHolder [s1=" + s1 + ", s2=" + s2 + ", l1=" + l1 + "]";
		}
	}

	public static class SPInfo implements ResultSetMapper<SPInfo> {
		String spName;
		Timestamp created;

		public SPInfo() {
		}

		private SPInfo(String spName, Timestamp created) {
			super();
			this.spName = spName;
			this.created = created;
		}

		public SPInfo(ResultSet rs) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public SPInfo map(ResultSet rs) {
			return new SPInfo(rs.getString(1), rs.getTimestamp(2));
		}

		@Override
		public String toString() {
			return "[name: " + spName + ", created: " + created + "]";
		}
	}
}
