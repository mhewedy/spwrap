package spwrap;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class ResultSet {

	private final java.sql.ResultSet resultSet;

	public ResultSet(java.sql.ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public java.sql.ResultSet getResultSet() {
		return resultSet;
	}

	public String getString(int columnIndex) {
		try {
			return resultSet.getString(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public boolean getBoolean(int columnIndex) {
		try {
			return resultSet.getBoolean(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public byte getByte(int columnIndex) {
		try {
			return resultSet.getByte(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public short getShort(int columnIndex) {
		try {
			return resultSet.getShort(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public int getInt(int columnIndex) {
		try {
			return resultSet.getInt(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public long getLong(int columnIndex) {
		try {
			return resultSet.getLong(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public float getFloat(int columnIndex) {
		try {
			return resultSet.getFloat(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public double getDouble(int columnIndex) {
		try {
			return resultSet.getDouble(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale) {
		try {
			return resultSet.getBigDecimal(columnIndex, scale);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public byte[] getBytes(int columnIndex) {
		try {
			return resultSet.getBytes(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Date getDate(int columnIndex) {
		try {
			return resultSet.getDate(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Time getTime(int columnIndex) {
		try {
			return resultSet.getTime(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Timestamp getTimestamp(int columnIndex) {
		try {
			return resultSet.getTimestamp(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public InputStream getAsciiStream(int columnIndex) {
		try {
			return resultSet.getAsciiStream(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public InputStream getUnicodeStream(int columnIndex) {
		try {
			return resultSet.getUnicodeStream(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public InputStream getBinaryStream(int columnIndex) {
		try {
			return resultSet.getBinaryStream(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public String getString(String columnLabel) {
		try {
			return resultSet.getString(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public boolean getBoolean(String columnLabel) {
		try {
			return resultSet.getBoolean(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public byte getByte(String columnLabel) {
		try {
			return resultSet.getByte(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public short getShort(String columnLabel) {
		try {
			return resultSet.getShort(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public int getInt(String columnLabel) {
		try {
			return resultSet.getInt(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public long getLong(String columnLabel) {
		try {
			return resultSet.getLong(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public float getFloat(String columnLabel) {
		try {
			return resultSet.getFloat(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public double getDouble(String columnLabel) {
		try {
			return resultSet.getDouble(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public BigDecimal getBigDecimal(String columnLabel, int scale) {
		try {
			return resultSet.getBigDecimal(columnLabel, scale);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public byte[] getBytes(String columnLabel) {
		try {
			return resultSet.getBytes(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Date getDate(String columnLabel) {
		try {
			return resultSet.getDate(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Time getTime(String columnLabel) {
		try {
			return resultSet.getTime(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Timestamp getTimestamp(String columnLabel) {
		try {
			return resultSet.getTimestamp(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public InputStream getAsciiStream(String columnLabel) {
		try {
			return resultSet.getAsciiStream(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public InputStream getUnicodeStream(String columnLabel) {
		try {
			return resultSet.getUnicodeStream(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public InputStream getBinaryStream(String columnLabel) {
		try {
			return resultSet.getBinaryStream(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Object getObject(int columnIndex) {
		try {
			return resultSet.getObject(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Object getObject(String columnLabel) {
		try {
			return resultSet.getObject(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Reader getCharacterStream(int columnIndex) {
		try {
			return resultSet.getCharacterStream(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Reader getCharacterStream(String columnLabel) {
		try {
			return resultSet.getCharacterStream(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public BigDecimal getBigDecimal(int columnIndex) {
		try {
			return resultSet.getBigDecimal(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public BigDecimal getBigDecimal(String columnLabel) {
		try {
			return resultSet.getBigDecimal(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Object getObject(int columnIndex, Map<String, Class<?>> map) {
		try {
			return resultSet.getObject(columnIndex, map);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Ref getRef(int columnIndex) {
		try {
			return resultSet.getRef(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Blob getBlob(int columnIndex) {
		try {
			return resultSet.getBlob(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Clob getClob(int columnIndex) {
		try {
			return resultSet.getClob(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Array getArray(int columnIndex) {
		try {
			return resultSet.getArray(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Object getObject(String columnLabel, Map<String, Class<?>> map) {
		try {
			return resultSet.getObject(columnLabel, map);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Ref getRef(String columnLabel) {
		try {
			return resultSet.getRef(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Blob getBlob(String columnLabel) {
		try {
			return resultSet.getBlob(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Clob getClob(String columnLabel) {
		try {
			return resultSet.getClob(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Array getArray(String columnLabel) {
		try {
			return resultSet.getArray(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Date getDate(int columnIndex, Calendar cal) {
		try {
			return resultSet.getDate(columnIndex, cal);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Date getDate(String columnLabel, Calendar cal) {
		try {
			return resultSet.getDate(columnLabel, cal);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Time getTime(int columnIndex, Calendar cal) {
		try {
			return resultSet.getTime(columnIndex, cal);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Time getTime(String columnLabel, Calendar cal) {
		try {
			return resultSet.getTime(columnLabel, cal);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) {
		try {
			return resultSet.getTimestamp(columnIndex, cal);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Timestamp getTimestamp(String columnLabel, Calendar cal) {
		try {
			return resultSet.getTimestamp(columnLabel, cal);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public URL getURL(int columnIndex) {
		try {
			return resultSet.getURL(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public URL getURL(String columnLabel) {
		try {
			return resultSet.getURL(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public RowId getRowId(int columnIndex) {
		try {
			return resultSet.getRowId(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public RowId getRowId(String columnLabel) {
		try {
			return resultSet.getRowId(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public NClob getNClob(int columnIndex) {
		try {
			return resultSet.getNClob(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public NClob getNClob(String columnLabel) {
		try {
			return resultSet.getNClob(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public SQLXML getSQLXML(int columnIndex) {
		try {
			return resultSet.getSQLXML(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public SQLXML getSQLXML(String columnLabel) {
		try {
			return resultSet.getSQLXML(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public String getNString(int columnIndex) {
		try {
			return resultSet.getNString(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public String getNString(String columnLabel) {
		try {
			return resultSet.getNString(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Reader getNCharacterStream(int columnIndex) {
		try {
			return resultSet.getNCharacterStream(columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Reader getNCharacterStream(String columnLabel) {
		try {
			return resultSet.getNCharacterStream(columnLabel);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}
}
