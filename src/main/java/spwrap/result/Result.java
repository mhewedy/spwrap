package spwrap.result;

import spwrap.CallException;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;

/**
 * <b>1-based index </b> container for stored procedure results that either
 * coming from result set or callable statement. <br />
 * <br />
 * <p>
 * Wrapper class for both {@link java.sql.ResultSet} and
 * {@link java.sql.CallableStatement}
 * </p>
 * <br />
 * <p>
 * If exposed methods are not enough, use methods {@link #wrappedObject()} to
 * get reference to the wrapped object and then cast it. to make sure you are
 * casting to to correct type use {@link #isResultSet()} or
 * {@link #isCallableStatement()} to check the type of the underlying object
 * first.<br />
 * </p>
 * <br />
 * <p>
 * Use methods getXXX(1), getXXX(2), etc to get access to the first, second, etc
 * results regardless it is result set or callable statement output parameters.
 * <br />
 * <br />
 * <i> For example: if you have an Stored Procedure that have 4 input parameters
 * and 2 output parameters of type VARCHAR, then to get the result of the output
 * parameters use: <br />
 * <br />
 * </i> {@code  result.getString(1);} <br />
 * {@code  result.getString(2);}
 * </p>
 * <br />
 * <p>
 * Still you can use getXXX(String columnOrOutputParamName)
 * </p>
 *
 * @author mhewedy
 *
 */
public abstract class Result<T> {

	protected T wrappedObject;

	protected Result(T wrappedObject) {
		this.wrappedObject = wrappedObject;
	}

	public T wrappedObject() {
		return this.wrappedObject;
	}

	public static <U> Result<?> of(ResultSet rs, CallableStatement cstmt, int outParamStartIndex) {
		if (rs != null && cstmt != null) {
			throw new CallException("rs and cstmt both cannot be non-null");
		} else if (rs != null) {
			return new ResultSetWrapper(rs);
		} else if (cstmt != null) {
			return new CallableStatementWrapper(cstmt, outParamStartIndex);
		} else {
			throw new CallException("rs and cstmt both cannot be null");
		}
	}

	public boolean isResultSet() {
		return ResultSet.class.isAssignableFrom(wrappedObject.getClass());
	}

	public boolean isCallableStatement() {
		return CallableStatement.class.isAssignableFrom(wrappedObject.getClass());
	}

	public abstract String getString(int columnOrParamIndex);

	public abstract boolean getBoolean(int columnOrParamIndex);

	public abstract byte getByte(int columnOrParamIndex);

	public abstract short getShort(int columnOrParamIndex);

	public abstract int getInt(int columnOrParamIndex);

	public abstract long getLong(int columnOrParamIndex);

	public abstract float getFloat(int columnOrParamIndex);

	public abstract double getDouble(int columnOrParamIndex);

	public abstract byte[] getBytes(int columnOrParamIndex);

	public abstract Date getDate(int columnOrParamIndex);

	public abstract Time getTime(int columnOrParamIndex);

	public abstract Timestamp getTimestamp(int columnOrParamIndex);

	public abstract Object getObject(int columnOrParamIndex);

	public abstract BigDecimal getBigDecimal(int columnOrParamIndex);

	public abstract Ref getRef(int columnOrParamIndex);

	public abstract Blob getBlob(int columnOrParamIndex);

	public abstract Clob getClob(int columnOrParamIndex);

	public abstract Array getArray(int columnOrParamIndex);

	public abstract URL getURL(int columnOrParamIndex);

	public abstract String getString(String columnOrParamName);

	public abstract boolean getBoolean(String columnOrParamName);

	public abstract byte getByte(String columnOrParamName);

	public abstract short getShort(String columnOrParamName);

	public abstract int getInt(String columnOrParamName);

	public abstract long getLong(String columnOrParamName);

	public abstract float getFloat(String columnOrParamName);

	public abstract double getDouble(String columnOrParamName);

	public abstract byte[] getBytes(String columnOrParamName);

	public abstract Date getDate(String columnOrParamName);

	public abstract Time getTime(String columnOrParamName);

	public abstract Timestamp getTimestamp(String columnOrParamName);

	public abstract Object getObject(String columnOrParamName);

	public abstract BigDecimal getBigDecimal(String columnOrParamName);

	public abstract Ref getRef(String columnOrParamName);

	public abstract Blob getBlob(String columnOrParamName);

	public abstract Clob getClob(String columnOrParamName);

	public abstract Array getArray(String columnOrParamName);

	public abstract URL getURL(String columnOrParamName);
}
