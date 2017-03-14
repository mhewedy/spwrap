package spwrap.db;

import spwrap.CallException;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Oracle extends ResultSetAsOutParamDatabase {

    private static final int CURSOR = -10;

    @Override
    public void registerResultSet(CallableStatement statement, int resultSetParamIndex) {
        try {
            statement.registerOutParameter(resultSetParamIndex, CURSOR);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    public ResultSet getResultSet(CallableStatement statement, int resultSetParamIndex) {
        try {
            return (ResultSet) statement.getObject(resultSetParamIndex);
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }
}
