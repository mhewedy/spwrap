package spwrap;

import static java.sql.Types.*;
import static spwrap.Caller.*;
import static spwrap.Caller.Param.*;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import spwrap.Caller.OutputParamMapper;
import spwrap.result.Result;

public class CallerTest {

	private static Caller caller;

	@BeforeClass
	public static void setup() {
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");

		TestUtils.install();

		caller = new Caller(TestUtils.ds);
	}

	@AfterClass
	public static void tearDown() {
		TestUtils.rollback();
	}

	@Test
	public void testCreateCustomer() {

		Integer custId = caller.call("create_customer", params(of("abdullah", VARCHAR), of("mohammad", VARCHAR)),
				paramTypes(INTEGER), new OutputParamMapper<Integer>() {

					public Integer map(Result<?> result) {
						return result.getInt(1);
					}
				});
		Assert.assertTrue(custId == 0);

	}
}
