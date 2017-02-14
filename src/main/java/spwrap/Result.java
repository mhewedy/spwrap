package spwrap;

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
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Wrapper class for both {@link java.sql.ResultSet} and
 * {@link java.sql.CallableStatement} <br />
 * <br />
 * If exposed methods are not enough, use methods {@link #resultSet()} and
 * {@link #callableStatement()} to get reference to the respective object
 * 
 * @author mhewedy
 *
 */
public class Result {

	private static final String OBJECTS_ARE_NULL = "resultSet and callableStatement are null";

	private final ResultSet rs;

	private final CallableStatement cstmt;

	public Result(java.sql.ResultSet resultSet, CallableStatement callableStatement) {
		this.rs = resultSet;
		this.cstmt = callableStatement;
	}

	/**
	 * 
	 * @return underlying {@link java.sql.ResultSet} object
	 */
	public java.sql.ResultSet resultSet() {
		return rs;
	}

	/**
	 * 
	 * @return underlying {@link java.sql.CallableStatement} object
	 */
	public CallableStatement callableStatement() {
		return cstmt;
	}

	public String getString(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getString(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getString(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public boolean getBoolean(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getBoolean(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getBoolean(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public byte getByte(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getByte(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getByte(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}

	}

	public short getShort(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getShort(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getShort(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public int getInt(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getInt(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getInt(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public long getLong(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getLong(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getLong(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public float getFloat(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getFloat(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getFloat(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public double getDouble(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getDouble(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getDouble(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public byte[] getBytes(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getBytes(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getBytes(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Date getDate(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getDate(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getDate(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Time getTime(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getTime(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getTime(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Timestamp getTimestamp(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getTimestamp(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getTimestamp(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public <T> T getObject(int columnIndex, Class<T> clazz) {
		try {
			if (rs != null) {
				return rs.getObject(columnIndex, clazz);
			} else if (cstmt != null) {
				return cstmt.getObject(columnIndex, clazz);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Reader getCharacterStream(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getCharacterStream(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getCharacterStream(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public BigDecimal getBigDecimal(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getBigDecimal(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getBigDecimal(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Ref getRef(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getRef(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getRef(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Blob getBlob(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getBlob(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getBlob(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Clob getClob(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getClob(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getClob(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Array getArray(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getArray(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getArray(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public URL getURL(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getURL(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getURL(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public RowId getRowId(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getRowId(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getRowId(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public NClob getNClob(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getNClob(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getNClob(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public SQLXML getSQLXML(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getSQLXML(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getSQLXML(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public String getNString(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getNString(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getNString(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	public Reader getNCharacterStream(int columnIndex) {
		try {
			if (rs != null) {
				return rs.getNCharacterStream(columnIndex);
			} else if (cstmt != null) {
				return cstmt.getNCharacterStream(columnIndex);
			} else {
				throw new RuntimeException(OBJECTS_ARE_NULL);
			}
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

}
