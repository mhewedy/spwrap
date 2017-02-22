package spwrap.result;

import  static spwrap.result.ExceptionWrapper.*;

import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

final class ResultSetWrapper extends Result<ResultSet> {

	ResultSetWrapper(ResultSet wrappedObject) {
		super(wrappedObject);
	}

	@Override
	public String getString(final int columnIndex) {
		return call(new Caller<String>() {
            public String call() throws SQLException {
                return wrappedObject.getString(columnIndex);
            }
        });
	}

	@Override
	public boolean getBoolean(final int columnIndex) {
        return call(new Caller<Boolean>() {
            public Boolean call() throws SQLException {
                return wrappedObject.getBoolean(columnIndex);
            }
        });
	}

	@Override
	public byte getByte(final int columnIndex) {
        return call(new Caller<Byte>() {
            public Byte call() throws SQLException {
                return wrappedObject.getByte(columnIndex);
            }
        });
	}

	@Override
	public short getShort(final int columnIndex) {
		return call(new Caller<Short>() {
            public Short call() throws SQLException {
                return wrappedObject.getShort(columnIndex);
            }
        });
	}

	@Override
	public int getInt(final int columnIndex) {
		return call(new Caller<Integer>() {
            public Integer call() throws SQLException {
                return wrappedObject.getInt(columnIndex);
            }
        });
	}

	@Override
	public long getLong(final int columnIndex) {
		return call(new Caller<Long>() {
            public Long call() throws SQLException {
                return wrappedObject.getLong(columnIndex);
            }
        });
	}

	@Override
	public float getFloat(final int columnIndex) {
		return call(new Caller<Float>() {
            public Float call() throws SQLException {
                return wrappedObject.getFloat(columnIndex);
            }
        });
	}

	@Override
	public double getDouble(final int columnIndex) {
		return call(new Caller<Double>() {
            public Double call() throws SQLException {
                return wrappedObject.getDouble(columnIndex);
            }
        });
	}

	@Override
	public byte[] getBytes(final int columnIndex) {
		return call(new Caller<byte[]>() {
            public byte[] call() throws SQLException {
                return wrappedObject.getBytes(columnIndex);
            }
        });
	}

	@Override
	public Date getDate(final int columnIndex) {
		return call(new Caller<Date>() {
            public Date call() throws SQLException {
                return wrappedObject.getDate(columnIndex);
            }
        });
	}

	@Override
	public Time getTime(final int columnIndex) {
		return call(new Caller<Time>() {
            public Time call() throws SQLException {
                return wrappedObject.getTime(columnIndex);
            }
        });
	}

	@Override
	public Timestamp getTimestamp(final int columnIndex) {
		return call(new Caller<Timestamp>() {
            public Timestamp call() throws SQLException {
                return wrappedObject.getTimestamp(columnIndex);
            }
        });
	}

	@Override
	public Object getObject(final int columnIndex) {
		return call(new Caller<Object>() {
            public Object call() throws SQLException {
                return wrappedObject.getObject(columnIndex);
            }
        });
	}

	@Override
	public Reader getCharacterStream(final int columnIndex) {
		return call(new Caller<Reader>() {
            public Reader call() throws SQLException {
                return wrappedObject.getCharacterStream(columnIndex);
            }
        });
	}

	@Override
	public BigDecimal getBigDecimal(final int columnIndex) {
		return call(new Caller<BigDecimal>() {
            public BigDecimal call() throws SQLException {
                return wrappedObject.getBigDecimal(columnIndex);
            }
        });
	}

	@Override
	public Ref getRef(final int columnIndex) {
		return call(new Caller<Ref>() {
            public Ref call() throws SQLException {
                return wrappedObject.getRef(columnIndex);
            }
        });
	}

	@Override
	public Blob getBlob(final int columnIndex) {
		return call(new Caller<Blob>() {
            public Blob call() throws SQLException {
                return wrappedObject.getBlob(columnIndex);
            }
        });
	}

	@Override
	public Clob getClob(final int columnIndex) {
		return call(new Caller<Clob>() {
            public Clob call() throws SQLException {
                return wrappedObject.getClob(columnIndex);
            }
        });
	}

	@Override
	public Array getArray(final int columnIndex) {
		return call(new Caller<Array>() {
            public Array call() throws SQLException {
                return wrappedObject.getArray(columnIndex);
            }
        });
	}

	@Override
	public URL getURL(final int columnIndex) {
		return call(new Caller<URL>() {
            public URL call() throws SQLException {
                return wrappedObject.getURL(columnIndex);
            }
        });
	}

	@Override
	public RowId getRowId(final int columnIndex) {
		return call(new Caller<RowId>() {
            public RowId call() throws SQLException {
                return wrappedObject.getRowId(columnIndex);
            }
        });
	}

	@Override
	public NClob getNClob(final int columnIndex) {
		return call(new Caller<NClob>() {
            public NClob call() throws SQLException {
                return wrappedObject.getNClob(columnIndex);
            }
        });
	}

	@Override
	public SQLXML getSQLXML(final int columnIndex) {
		return call(new Caller<SQLXML>() {
            public SQLXML call() throws SQLException {
                return wrappedObject.getSQLXML(columnIndex);
            }
        });
	}

	@Override
	public String getNString(final int columnIndex) {
		return call(new Caller<String>() {
            public String call() throws SQLException {
                return wrappedObject.getNString(columnIndex);
            }
        });
	}

	@Override
	public Reader getNCharacterStream(final int columnIndex) {
		return call(new Caller<Reader>() {
            public Reader call() throws SQLException {
                return wrappedObject.getNCharacterStream(columnIndex);
            }
        });
	}

	@Override
	public String getString(final String columnLabel) {
		return call(new Caller<String>() {
            public String call() throws SQLException {
                return wrappedObject.getString(columnLabel);
            }
        });
	}

	@Override
	public boolean getBoolean(final String columnLabel) {
		return call(new Caller<Boolean>() {
            public Boolean call() throws SQLException {
                return wrappedObject.getBoolean(columnLabel);
            }
        });
	}

	@Override
	public byte getByte(final String columnLabel) {
		return call(new Caller<Byte>() {
            public Byte call() throws SQLException {
                return wrappedObject.getByte(columnLabel);
            }
        });
	}

	@Override
	public short getShort(final String columnLabel) {
		return call(new Caller<Short>() {
            public Short call() throws SQLException {
                return wrappedObject.getShort(columnLabel);
            }
        });
	}

	@Override
	public int getInt(final String columnLabel) {
		return call(new Caller<Integer>() {
            public Integer call() throws SQLException {
                return wrappedObject.getInt(columnLabel);
            }
        });
	}

	@Override
	public long getLong(final String columnLabel) {
		return call(new Caller<Long>() {
            public Long call() throws SQLException {
                return wrappedObject.getLong(columnLabel);
            }
        });
	}

	@Override
	public float getFloat(final String columnLabel) {
		return call(new Caller<Float>() {
            public Float call() throws SQLException {
                return wrappedObject.getFloat(columnLabel);
            }
        });
	}

	@Override
	public double getDouble(final String columnLabel) {
		return call(new Caller<Double>() {
            public Double call() throws SQLException {
                return wrappedObject.getDouble(columnLabel);
            }
        });
	}

	@Override
	public byte[] getBytes(final String columnLabel) {
		return call(new Caller<byte[]>() {
            public byte[] call() throws SQLException {
                return wrappedObject.getBytes(columnLabel);
            }
        });
	}

	@Override
	public Date getDate(final String columnLabel) {
		return call(new Caller<Date>() {
            public Date call() throws SQLException {
                return wrappedObject.getDate(columnLabel);
            }
        });
	}

	@Override
	public Time getTime(final String columnLabel) {
		return call(new Caller<Time>() {
            public Time call() throws SQLException {
                return wrappedObject.getTime(columnLabel);
            }
        });
	}

	@Override
	public Timestamp getTimestamp(final String columnLabel) {
		return call(new Caller<Timestamp>() {
            public Timestamp call() throws SQLException {
                return wrappedObject.getTimestamp(columnLabel);
            }
        });
	}

	@Override
	public Object getObject(final String columnLabel) {
		return call(new Caller<Object>() {
            public Object call() throws SQLException {
                return wrappedObject.getObject(columnLabel);
            }
        });
	}

	@Override
	public Reader getCharacterStream(final String columnLabel) {
		return call(new Caller<Reader>() {
            public Reader call() throws SQLException {
                return wrappedObject.getCharacterStream(columnLabel);
            }
        });
	}

	@Override
	public BigDecimal getBigDecimal(final String columnLabel) {
		return call(new Caller<BigDecimal>() {
            public BigDecimal call() throws SQLException {
                return wrappedObject.getBigDecimal(columnLabel);
            }
        });
	}

	@Override
	public Ref getRef(final String columnLabel) {
		return call(new Caller<Ref>() {
            public Ref call() throws SQLException {
                return wrappedObject.getRef(columnLabel);
            }
        });
	}

	@Override
	public Blob getBlob(final String columnLabel) {
		return call(new Caller<Blob>() {
            public Blob call() throws SQLException {
                return wrappedObject.getBlob(columnLabel);
            }
        });
	}

	@Override
	public Clob getClob(final String columnLabel) {
		return call(new Caller<Clob>() {
            public Clob call() throws SQLException {
                return wrappedObject.getClob(columnLabel);
            }
        });
	}

	@Override
	public Array getArray(final String columnLabel) {
		return call(new Caller<Array>() {
            public Array call() throws SQLException {
                return wrappedObject.getArray(columnLabel);
            }
        });
	}

	@Override
	public URL getURL(final String columnLabel) {
		return call(new Caller<URL>() {
            public URL call() throws SQLException {
                return wrappedObject.getURL(columnLabel);
            }
        });
	}

	@Override
	public RowId getRowId(final String columnLabel) {
		return call(new Caller<RowId>() {
            public RowId call() throws SQLException {
                return wrappedObject.getRowId(columnLabel);
            }
        });
	}

	@Override
	public NClob getNClob(final String columnLabel) {
		return call(new Caller<NClob>() {
            public NClob call() throws SQLException {
                return wrappedObject.getNClob(columnLabel);
            }
        });
	}

	@Override
	public SQLXML getSQLXML(final String columnLabel) {
		return call(new Caller<SQLXML>() {
            public SQLXML call() throws SQLException {
                return wrappedObject.getSQLXML(columnLabel);
            }
        });
	}

	@Override
	public String getNString(final String columnLabel) {
		return call(new Caller<String>() {
            public String call() throws SQLException {
                return wrappedObject.getNString(columnLabel);
            }
        });
	}

	@Override
	public Reader getNCharacterStream(final String columnLabel) {
		return call(new Caller<Reader>() {
            public Reader call() throws SQLException {
                return wrappedObject.getNCharacterStream(columnLabel);
            }
        });
	}
}
