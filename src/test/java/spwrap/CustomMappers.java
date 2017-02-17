package spwrap;

import static java.sql.Types.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import spwrap.Caller.ResultSetMapper;
import spwrap.Caller.TypedOutputParamMapper;
import spwrap.result.Result;

public class CustomMappers {

	/**
	 * Used with any Stored Procedure that returns 1 output parameter of type
	 * Integer, typically a save new Record Stored Procedure
	 * 
	 * @author mhewedy
	 *
	 */
	public static class GenericIdMapper implements TypedOutputParamMapper<Integer> {

		public Integer map(Result<?> result) {
			return result.getInt(1);
		}

		public List<Integer> getTypes() {
			return Arrays.asList(INTEGER);
		}
	};

	public static class CustomParamsMapper implements TypedOutputParamMapper<Customer> {

		public Customer map(Result<?> result) {
			return new Customer(null, result.getString(1), result.getString(2));
		}

		public List<Integer> getTypes() {
			return Arrays.asList(VARCHAR, VARCHAR);
		}
	}

	public static class CustomResultSetMapper implements ResultSetMapper<Customer> {

		public Customer map(Result<?> result) {
			return new Customer(result.getInt(1), result.getString(2), result.getString(3));
		}
	}

	public static class DateMapper implements TypedOutputParamMapper<Date> {

		public Date map(Result<?> result) {
			return result.getDate(1);
		}

		public List<Integer> getTypes() {
			return Arrays.asList(DATE);
		}
	}

	public static class TableNamesMapper implements ResultSetMapper<String> {

		public String map(Result<?> result) {
			return result.getString(1);
		}

	}

	public static class SingleStringMapper implements TypedOutputParamMapper<String> {

		public String map(Result<?> result) {
			return result.getString(1);
		}

		public List<Integer> getTypes() {
			return Arrays.asList(VARCHAR);
		}

	}
}
