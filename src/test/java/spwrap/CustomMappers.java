package spwrap;

import static java.sql.Types.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import spwrap.mappers.ResultSetMapper;
import spwrap.mappers.TypedOutputParamMapper;
import spwrap.result.Result;

public class CustomMappers {

	public static class CustomParamsMapper implements TypedOutputParamMapper<Customer> {

		public Customer map(Result<?> result) {
			return new Customer(null, result.getString(1) + ".CustomParamsMapper",
					result.getString(2) + ".CustomParamsMapper");
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
			return Collections.singletonList(DATE);
		}
	}

	// Use @AutoMapper instead when you have a result set of 1 column
	public static class TableNamesMapper implements ResultSetMapper<String> {

		public String map(Result<?> result) {
			return result.getString(1);
		}

	}

    public static class CustomParamsMapperByName implements TypedOutputParamMapper<Customer> {
        public List<Integer> getTypes() {
            return Arrays.asList(VARCHAR, VARCHAR);
        }

        public Customer map(Result<?> result) {
            return new Customer(null, result.getString(1), result.getString(2));
        }
    }

    public static class CustomParamsMapperByInvalidName implements TypedOutputParamMapper<Customer> {
        public List<Integer> getTypes() {
            return Arrays.asList(VARCHAR, VARCHAR);
        }

        public Customer map(Result<?> result) {
            return new Customer(null, result.getString("invalidParamName"), result.getString("lastname"));
        }
    }
}
