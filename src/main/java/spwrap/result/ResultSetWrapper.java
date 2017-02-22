package spwrap.result;

import spwrap.CallException;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;

final class ResultSetWrapper extends Result<ResultSet> {

	ResultSetWrapper(ResultSet wrappedObject) {
		super(wrappedObject);
	}

    @Override
    public String getString(int columnIndex) {
        try {
            return wrappedObject.getString(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public boolean getBoolean(int columnIndex) {
        try {
            return wrappedObject.getBoolean(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public byte getByte(int columnIndex) {
        try {
            return wrappedObject.getByte(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public short getShort(int columnIndex) {
        try {
            return wrappedObject.getShort(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public int getInt(int columnIndex) {
        try {
            return wrappedObject.getInt(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public long getLong(int columnIndex) {
        try {
            return wrappedObject.getLong(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public float getFloat(int columnIndex) {
        try {
            return wrappedObject.getFloat(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public double getDouble(int columnIndex) {
        try {
            return wrappedObject.getDouble(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public byte[] getBytes(int columnIndex) {
        try {
            return wrappedObject.getBytes(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Date getDate(int columnIndex) {
        try {
            return wrappedObject.getDate(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Time getTime(int columnIndex) {
        try {
            return wrappedObject.getTime(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) {
        try {
            return wrappedObject.getTimestamp(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Object getObject(int columnIndex) {
        try {
            return wrappedObject.getObject(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) {
        try {
            return wrappedObject.getBigDecimal(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Ref getRef(int columnIndex) {
        try {
            return wrappedObject.getRef(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Blob getBlob(int columnIndex) {
        try {
            return wrappedObject.getBlob(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Clob getClob(int columnIndex) {
        try {
            return wrappedObject.getClob(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Array getArray(int columnIndex) {
        try {
            return wrappedObject.getArray(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public URL getURL(int columnIndex) {
        try {
            return wrappedObject.getURL(columnIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public String getString(String columnLabel) {
        try {
            return wrappedObject.getString(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public boolean getBoolean(String columnLabel) {
        try {
            return wrappedObject.getBoolean(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public byte getByte(String columnLabel) {
        try {
            return wrappedObject.getByte(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public short getShort(String columnLabel) {
        try {
            return wrappedObject.getShort(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public int getInt(String columnLabel) {
        try {
            return wrappedObject.getInt(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public long getLong(String columnLabel) {
        try {
            return wrappedObject.getLong(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public float getFloat(String columnLabel) {
        try {
            return wrappedObject.getFloat(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public double getDouble(String columnLabel) {
        try {
            return wrappedObject.getDouble(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public byte[] getBytes(String columnLabel) {
        try {
            return wrappedObject.getBytes(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Date getDate(String columnLabel) {
        try {
            return wrappedObject.getDate(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Time getTime(String columnLabel) {
        try {
            return wrappedObject.getTime(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) {
        try {
            return wrappedObject.getTimestamp(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Object getObject(String columnLabel) {
        try {
            return wrappedObject.getObject(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) {
        try {
            return wrappedObject.getBigDecimal(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Ref getRef(String columnLabel) {
        try {
            return wrappedObject.getRef(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Blob getBlob(String columnLabel) {
        try {
            return wrappedObject.getBlob(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Clob getClob(String columnLabel) {
        try {
            return wrappedObject.getClob(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public Array getArray(String columnLabel) {
        try {
            return wrappedObject.getArray(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public URL getURL(String columnLabel) {
        try {
            return wrappedObject.getURL(columnLabel);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }
}
