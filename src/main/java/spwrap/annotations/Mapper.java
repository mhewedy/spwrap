package spwrap.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import spwrap.Caller.OutputParamMapper;
import spwrap.Caller.ResultSetMapper;
import spwrap.ResultSet;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Mapper {

	Class<? extends spwrap.Caller.ResultSetMapper<?>> resultSet() default NullResultSetMapper.class;

	Class<? extends TypedOutputParamMapper<?>> outParams() default NullTypedOutputParamMapper.class;

	static interface TypedOutputParamMapper<T> extends OutputParamMapper<T> {
		List<Integer> getSQLTypes();
	}

	static final class NullResultSetMapper implements ResultSetMapper<Object> {
		@Override
		public Object map(ResultSet rs) {
			return null;
		}
	}

	static final class NullTypedOutputParamMapper implements TypedOutputParamMapper<Object> {
		@Override
		public Object map(CallableStatement call, int index) throws SQLException {
			return null;
		}
		@Override
		public List<Integer> getSQLTypes() {
			return null;
		}
	}

}
