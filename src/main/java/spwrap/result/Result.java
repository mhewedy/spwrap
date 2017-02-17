package spwrap.result;

import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

import spwrap.CallException;

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
 * 
 * </p>
 * 
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
		if (rs != null) {
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

	public abstract String getString(int columnIndex);

	public abstract boolean getBoolean(int columnIndex);

	public abstract byte getByte(int columnIndex);

	public abstract short getShort(int columnIndex);

	public abstract int getInt(int columnIndex);

	public abstract long getLong(int columnIndex);

	public abstract float getFloat(int columnIndex);

	public abstract double getDouble(int columnIndex);

	public abstract byte[] getBytes(int columnIndex);

	public abstract Date getDate(int columnIndex);

	public abstract Time getTime(int columnIndex);

	public abstract Timestamp getTimestamp(int columnIndex);

	public abstract <U> U getObject(int columnIndex, Class<U> clazz);

	public abstract Reader getCharacterStream(int columnIndex);

	public abstract BigDecimal getBigDecimal(int columnIndex);

	public abstract Ref getRef(int columnIndex);

	public abstract Blob getBlob(int columnIndex);

	public abstract Clob getClob(int columnIndex);

	public abstract Array getArray(int columnIndex);

	public abstract URL getURL(int columnIndex);

	public abstract RowId getRowId(int columnIndex);

	public abstract NClob getNClob(int columnIndex);

	public abstract SQLXML getSQLXML(int columnIndex);

	public abstract String getNString(int columnIndex);

	public abstract Reader getNCharacterStream(int columnIndex);

}
