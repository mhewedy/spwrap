package spwrap.result;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;

import static spwrap.result.ExceptionWrapper.Caller;
import static spwrap.result.ExceptionWrapper.call;

final class CallableStatementWrapper extends Result<CallableStatement> {

	private int outSIndex;

	CallableStatementWrapper(CallableStatement cstmt, int outParamStartIndex) {
		super(cstmt);
		this.outSIndex = outParamStartIndex;
	}

	@Override
	public String getString(final int parameterIndex) {
		return call(new Caller<String>() {
			public String call() throws SQLException {
				return wrappedObject.getString(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public boolean getBoolean(final int parameterIndex) {
		return call(new Caller<Boolean>() {
			public Boolean call() throws SQLException {
				return wrappedObject.getBoolean(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public byte getByte(final int parameterIndex) {
		return call(new Caller<Byte>() {
			public Byte call() throws SQLException {
				return wrappedObject.getByte(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public short getShort(final int parameterIndex) {
		return call(new Caller<Short>() {
			public Short call() throws SQLException {
				return wrappedObject.getShort(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public int getInt(final int parameterIndex) {
		return call(new Caller<Integer>() {
			public Integer call() throws SQLException {
				return wrappedObject.getInt(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public long getLong(final int parameterIndex) {
		return call(new Caller<Long>() {
			public Long call() throws SQLException {
				return wrappedObject.getLong(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public float getFloat(final int parameterIndex) {
		return call(new Caller<Float>() {
			public Float call() throws SQLException {
				return wrappedObject.getFloat(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public double getDouble(final int parameterIndex) {
		return call(new Caller<Double>() {
			public Double call() throws SQLException {
				return wrappedObject.getDouble(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public byte[] getBytes(final int parameterIndex) {
		return call(new Caller<byte[]>() {
			public byte[] call() throws SQLException {
				return wrappedObject.getBytes(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public Date getDate(final int parameterIndex) {
		return call(new Caller<Date>() {
			public Date call() throws SQLException {
				return wrappedObject.getDate(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public Time getTime(final int parameterIndex) {
		return call(new Caller<Time>() {
			public Time call() throws SQLException {
				return wrappedObject.getTime(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public Timestamp getTimestamp(final int parameterIndex) {
		return call(new Caller<Timestamp>() {
			public Timestamp call() throws SQLException {
				return wrappedObject.getTimestamp(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public Object getObject(final int parameterIndex) {
		return call(new Caller<Object>() {
			public Object call() throws SQLException {
				return wrappedObject.getObject(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public BigDecimal getBigDecimal(final int parameterIndex) {
		return call(new Caller<BigDecimal>() {
			public BigDecimal call() throws SQLException {
				return wrappedObject.getBigDecimal(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public Ref getRef(final int parameterIndex) {
		return call(new Caller<Ref>() {
			public Ref call() throws SQLException {
				return wrappedObject.getRef(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public Blob getBlob(final int parameterIndex) {
		return call(new Caller<Blob>() {
			public Blob call() throws SQLException {
				return wrappedObject.getBlob(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public Clob getClob(final int parameterIndex) {
		return call(new Caller<Clob>() {
			public Clob call() throws SQLException {
				return wrappedObject.getClob(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public Array getArray(final int parameterIndex) {
		return call(new Caller<Array>() {
			public Array call() throws SQLException {
				return wrappedObject.getArray(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public URL getURL(final int parameterIndex) {
		return call(new Caller<URL>() {
			public URL call() throws SQLException {
				return wrappedObject.getURL(outSIndex + parameterIndex);
			}
		});
	}

	@Override
	public String getString(final String parameterName) {
		return call(new Caller<String>() {
			public String call() throws SQLException {
				return wrappedObject.getString(parameterName);
			}
		});
	}

	@Override
	public boolean getBoolean(final String parameterName) {
		return call(new Caller<Boolean>() {
			public Boolean call() throws SQLException {
				return wrappedObject.getBoolean(parameterName);
			}
		});
	}

	@Override
	public byte getByte(final String parameterName) {
		return call(new Caller<Byte>() {
			public Byte call() throws SQLException {
				return wrappedObject.getByte(parameterName);
			}
		});
	}

	@Override
	public short getShort(final String parameterName) {
		return call(new Caller<Short>() {
			public Short call() throws SQLException {
				return wrappedObject.getShort(parameterName);
			}
		});
	}

	@Override
	public int getInt(final String parameterName) {
		return call(new Caller<Integer>() {
			public Integer call() throws SQLException {
				return wrappedObject.getInt(parameterName);
			}
		});
	}

	@Override
	public long getLong(final String parameterName) {
		return call(new Caller<Long>() {
			public Long call() throws SQLException {
				return wrappedObject.getLong(parameterName);
			}
		});
	}

	@Override
	public float getFloat(final String parameterName) {
		return call(new Caller<Float>() {
			public Float call() throws SQLException {
				return wrappedObject.getFloat(parameterName);
			}
		});
	}

	@Override
	public double getDouble(final String parameterName) {
		return call(new Caller<Double>() {
			public Double call() throws SQLException {
				return wrappedObject.getDouble(parameterName);
			}
		});
	}

	@Override
	public byte[] getBytes(final String parameterName) {
		return call(new Caller<byte[]>() {
			public byte[] call() throws SQLException {
				return wrappedObject.getBytes(parameterName);
			}
		});
	}

	@Override
	public Date getDate(final String parameterName) {
		return call(new Caller<Date>() {
			public Date call() throws SQLException {
				return wrappedObject.getDate(parameterName);
			}
		});
	}

	@Override
	public Time getTime(final String parameterName) {
		return call(new Caller<Time>() {
			public Time call() throws SQLException {
				return wrappedObject.getTime(parameterName);
			}
		});
	}

	@Override
	public Timestamp getTimestamp(final String parameterName) {
		return call(new Caller<Timestamp>() {
			public Timestamp call() throws SQLException {
				return wrappedObject.getTimestamp(parameterName);
			}
		});
	}

	@Override
	public Object getObject(final String parameterName) {
		return call(new Caller<Object>() {
			public Object call() throws SQLException {
				return wrappedObject.getObject(parameterName);
			}
		});
	}

	@Override
	public BigDecimal getBigDecimal(final String parameterName) {
		return call(new Caller<BigDecimal>() {
			public BigDecimal call() throws SQLException {
				return wrappedObject.getBigDecimal(parameterName);
			}
		});
	}

	@Override
	public Ref getRef(final String parameterName) {
		return call(new Caller<Ref>() {
			public Ref call() throws SQLException {
				return wrappedObject.getRef(parameterName);
			}
		});
	}

	@Override
	public Blob getBlob(final String parameterName) {
		return call(new Caller<Blob>() {
			public Blob call() throws SQLException {
				return wrappedObject.getBlob(parameterName);
			}
		});
	}

	@Override
	public Clob getClob(final String parameterName) {
		return call(new Caller<Clob>() {
			public Clob call() throws SQLException {
				return wrappedObject.getClob(parameterName);
			}
		});
	}

	@Override
	public Array getArray(final String parameterName) {
		return call(new Caller<Array>() {
			public Array call() throws SQLException {
				return wrappedObject.getArray(parameterName);
			}
		});
	}

	@Override
	public URL getURL(final String parameterName) {
		return call(new Caller<URL>() {
			public URL call() throws SQLException {
				return wrappedObject.getURL(parameterName);
			}
		});
	}
}
