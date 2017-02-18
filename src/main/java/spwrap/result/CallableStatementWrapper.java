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
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

import spwrap.CallException;

final class CallableStatementWrapper extends Result<CallableStatement> {

	private int outSIndex;

	CallableStatementWrapper(CallableStatement cstmt, int outParamStartIndex) {
		super(cstmt);
		this.outSIndex = outParamStartIndex;
	}

	@Override
	public String getString(int columnIndex) {
		try {
			return wrappedObject.getString(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public boolean getBoolean(int columnIndex) {
		try {
			return wrappedObject.getBoolean(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public byte getByte(int columnIndex) {
		try {
			return wrappedObject.getByte(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public short getShort(int columnIndex) {
		try {
			return wrappedObject.getShort(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public int getInt(int columnIndex) {
		try {
			return wrappedObject.getInt(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public long getLong(int columnIndex) {
		try {
			return wrappedObject.getLong(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public float getFloat(int columnIndex) {
		try {
			return wrappedObject.getFloat(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public double getDouble(int columnIndex) {
		try {
			return wrappedObject.getDouble(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public byte[] getBytes(int columnIndex) {
		try {
			return wrappedObject.getBytes(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public Date getDate(int columnIndex) {
		try {
			return wrappedObject.getDate(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public Time getTime(int columnIndex) {
		try {
			return wrappedObject.getTime(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) {
		try {
			return wrappedObject.getTimestamp(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public <U> U getObject(int columnIndex, Class<U> clazz) {
		try {
			return wrappedObject.getObject(outSIndex + columnIndex, clazz);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}
	
	@Override
	public Object getObject(int columnIndex) {
		try {
			return wrappedObject.getObject(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public Reader getCharacterStream(int columnIndex) {
		try {
			return wrappedObject.getCharacterStream(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) {
		try {
			return wrappedObject.getBigDecimal(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public Ref getRef(int columnIndex) {
		try {
			return wrappedObject.getRef(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public Blob getBlob(int columnIndex) {
		try {
			return wrappedObject.getBlob(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public Clob getClob(int columnIndex) {
		try {
			return wrappedObject.getClob(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public Array getArray(int columnIndex) {
		try {
			return wrappedObject.getArray(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public URL getURL(int columnIndex) {
		try {
			return wrappedObject.getURL(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public RowId getRowId(int columnIndex) {
		try {
			return wrappedObject.getRowId(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public NClob getNClob(int columnIndex) {
		try {
			return wrappedObject.getNClob(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) {
		try {
			return wrappedObject.getSQLXML(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public String getNString(int columnIndex) {
		try {
			return wrappedObject.getNString(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) {
		try {
			return wrappedObject.getNCharacterStream(outSIndex + columnIndex);
		} catch (SQLException e) {
			throw new CallException(e);
		}
	}

}
