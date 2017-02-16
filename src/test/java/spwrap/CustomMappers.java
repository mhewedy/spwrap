package spwrap;

import static java.sql.Types.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import spwrap.Caller.ResultSetMapper;
import spwrap.Caller.TypedOutputParamMapper;

public class CustomMappers {

	/**
	 * Used with any Stored Procedure that returns 1 output parameter of type
	 * Integer, typically a save new Record Stored Procedure
	 * 
	 * @author mhewedy
	 *
	 */
	public static class GenericIdMapper implements TypedOutputParamMapper<Integer> {

		@Override
		public Integer map(Result result, int index) {
			return result.getInt(index);
		}

		@Override
		public List<Integer> getTypes() {
			return Arrays.asList(INTEGER);
		}
	};

	public static class CustomParamsMapper implements TypedOutputParamMapper<Customer> {

		@Override
		public Customer map(Result result, int index) {
			return new Customer(null, result.getString(index), result.getString(index + 1));
		}

		@Override
		public List<Integer> getTypes() {
			return Arrays.asList(VARCHAR, VARCHAR);
		}
	}

	public static class CustomResultSetMapper implements ResultSetMapper<Customer> {

		@Override
		public Customer map(Result result) {
			return new Customer(result.getInt(1), result.getString(2), result.getString(3));
		}
	}

	public static class DateMapper implements TypedOutputParamMapper<Date> {

		@Override
		public Date map(Result result, int index) {
			return result.getDate(index);
		}

		@Override
		public List<Integer> getTypes() {
			return Arrays.asList(DATE);
		}
	}

	public static class TableNamesMapper implements ResultSetMapper<String> {

		@Override
		public String map(Result result) {
			return result.getString(1);
		}

	}
}
