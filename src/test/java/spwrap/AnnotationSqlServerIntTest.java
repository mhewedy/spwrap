package spwrap;

import java.sql.Date;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zaxxer.hikari.HikariDataSource;

import spwrap.Caller.ResultSetMapper;
import spwrap.Caller.TypedOutputParamMapper;
import spwrap.annotations.Mapper;
import spwrap.annotations.Param;
import spwrap.annotations.StoredProc;

//RUN src/test/resources/sqlserver_script.sql first

public class AnnotationSqlServerIntTest {

	TestService testService;

	@Before
	public void setup() {

		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");

		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:sqlserver://192.168.43.12:1433;DatabaseName=TEST");
		ds.setUsername("test");
		ds.setPassword("test");

		testService = new Caller(ds).create(TestService.class);

	}

	// sp no input or output params
	@Test
	public void test1() {
		testService.callFirstSp();
	}

	// sp with input param and result set (methodName can be the sp name)
	@Test
	public void test2() {
		List<String> list = testService.echo("hello");

		Assert.assertTrue(list.size() > 4);
		Assert.assertTrue(list.contains("ECHO"));
		Assert.assertTrue(list.contains("ECHO2"));
		Assert.assertTrue(list.contains("EXE_NO_IN_NO_OUT"));
	}

	// sp with input param and output params
	@Test
	public void test3() {
		String input = "hello";
		String output = testService.callThirdSp(input);

		Assert.assertEquals(input, output);
	}

	// sp with no input and multi output params
	@Test
	public void test4() {

		FourthSpReturnType callFourthSp = testService.callFourthSp();

		Assert.assertEquals("HELLO", callFourthSp.s1);
		Assert.assertEquals("WORLD", callFourthSp.s2);
		Assert.assertTrue(99L == callFourthSp.l1);
	}

	// sp with no input and multi output params (implicit mapper are detected
	// from the return type since it implements TypedOutputParamMapper
	// interface)
	@Test
	public void test5() {

		Fourth2ReturnTypeAndMapper callFourthSp = testService.callFourthSp2();

		Assert.assertEquals("HELLO", callFourthSp.s1);
		Assert.assertEquals("WORLD", callFourthSp.s2);
		Assert.assertTrue(99L == callFourthSp.l1);
	}

	@Test
	public void test6() {

		Tuple<FifthResultSet, FifthOutParams> callFourthSp = testService.callFifthSp();

		Assert.assertEquals("HELLO", callFourthSp.object().s1);
		Assert.assertEquals("WORLD", callFourthSp.object().s2);
		Assert.assertTrue(99L == callFourthSp.object().l1);

		Assert.assertTrue(callFourthSp.list().size() >= 4);
	}

	@Test
	public void test7() {

		List<FifthResultSet> callSixSp = testService.callSixSp();
		Assert.assertTrue(callSixSp.size() >= 4);
	}

	@Test
	public void test8() {

		List<FifthResultSet> callSixSp = testService.callSixSp2();
		Assert.assertTrue(callSixSp.size() >= 4);
	}

	@Test
	public void test9() {

		Tuple<FifthResultSet, FifthOutParams> callFourthSp = testService.callFifthSp2();

		Assert.assertEquals("HELLO", callFourthSp.object().s1);
		Assert.assertEquals("WORLD", callFourthSp.object().s2);
		Assert.assertTrue(99L == callFourthSp.object().l1);

		Assert.assertTrue(callFourthSp.list().size() >= 4);
	}

	// sp interface

	public static interface TestService {

		@StoredProc("EXE_NO_IN_NO_OUT")
		void callFirstSp();

		@Mapper(SecondSpMapper.class)
		@StoredProc // ("ECHO")
		List<String> echo(@Param(Types.VARCHAR) String input);

		@Mapper(ThirdSpMapper.class)
		@StoredProc("ECHO2")
		String callThirdSp(@Param(Types.VARCHAR) String input);

		@Mapper(FourthSpMapper.class)
		@StoredProc("OUTPUT")
		FourthSpReturnType callFourthSp();

		@StoredProc("OUTPUT")
		Fourth2ReturnTypeAndMapper callFourthSp2();

		@Mapper({ FifthResultSet.class, FifthOutParams.class })
		@StoredProc("OUTPUT_WITH_RS")
		Tuple<FifthResultSet, FifthOutParams> callFifthSp();

		@Mapper(FifthResultSet.class)
		@StoredProc("OUTPUT_WITH_RS2")
		List<FifthResultSet> callSixSp();

		@StoredProc("OUTPUT_WITH_RS2")
		List<FifthResultSet> callSixSp2();

		@StoredProc("OUTPUT_WITH_RS")
		Tuple<FifthResultSet, FifthOutParams> callFifthSp2();
	}

	// ------------------------------------ Mappers

	public static class SecondSpMapper implements ResultSetMapper<String> {

		@Override
		public String map(Result result) {
			return result.getString(1);
		}
	}

	public static class ThirdSpMapper implements TypedOutputParamMapper<String> {

		@Override
		public String map(Result result, int index) {
			return result.getString(index);
		}

		@Override
		public List<Integer> getTypes() {
			return Arrays.asList(Types.VARCHAR);
		}
	}

	public static class FourthSpReturnType {
		String s1, s2;
		Long l1;
	}

	public static class FourthSpMapper implements TypedOutputParamMapper<FourthSpReturnType> {

		@Override
		public FourthSpReturnType map(Result result, int index) {
			FourthSpReturnType r = new FourthSpReturnType();
			r.s1 = result.getString(index);
			r.s2 = result.getString(index + 1);
			r.l1 = result.getLong(index + 2);
			return r;
		}

		@Override
		public List<Integer> getTypes() {
			return Arrays.asList(Types.VARCHAR, Types.VARCHAR, Types.BIGINT);
		}

	}

	public static class Fourth2ReturnTypeAndMapper implements TypedOutputParamMapper<Fourth2ReturnTypeAndMapper> {

		String s1, s2;
		Long l1;

		@Override
		public Fourth2ReturnTypeAndMapper map(Result result, int index) {
			this.s1 = result.getString(index);
			this.s2 = result.getString(index + 1);
			this.l1 = result.getLong(index + 2);
			return this;
		}

		@Override
		public List<Integer> getTypes() {
			return Arrays.asList(Types.VARCHAR, Types.VARCHAR, Types.BIGINT);
		}
	}

	public static class FifthResultSet implements ResultSetMapper<FifthResultSet> {

		String routineName;
		Date created;

		@Override
		public FifthResultSet map(Result result) {
			FifthResultSet obj = new FifthResultSet();
			obj.routineName = result.getString(1);
			obj.created = result.getDate(2);
			return obj;
		}

		@Override
		public String toString() {
			return routineName + ", " + created;
		}
	}

	public static class FifthOutParams implements TypedOutputParamMapper<FifthOutParams> {
		String s1, s2;
		Long l1;

		@Override
		public FifthOutParams map(Result result, int index) {
			this.s1 = result.getString(index);
			this.s2 = result.getString(index + 1);
			this.l1 = result.getLong(index + 2);
			return this;
		}

		@Override
		public List<Integer> getTypes() {
			return Arrays.asList(Types.VARCHAR, Types.VARCHAR, Types.BIGINT);
		}
	}

}
