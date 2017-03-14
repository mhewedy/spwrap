package spwrap.db;

import spwrap.CallException;
import spwrap.Caller;
import spwrap.Caller.ParamType;
import spwrap.Config;
import spwrap.mappers.ResultSetMapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static spwrap.Caller.INVALID_INDEX;
import static spwrap.Caller.PARAM_OFFSET;
import static spwrap.Util.*;

public class GenericDatabase implements Database {

    public static Database from(Connection connection) {
        try {
            String dbProductName = connection.getMetaData().getDatabaseProductName().toLowerCase();
            if (dbProductName.contains("hsql")) {
                return new HSQL();
            } else if (dbProductName.contains("oracle")) {
                return new Oracle();
            } else {
                return new GenericDatabase();
            }
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    public boolean executeCall(CallableStatement call) throws SQLException {
        return call.execute();
    }

    public String getCallString(String procName, Config config, List<Caller.Param> inParams,
                                List<ParamType> outParamsTypes, ResultSetMapper<?> rsMapper){
        return createCallableString(procName, getParamsCount(config, inParams, outParamsTypes));
    }

    public int getStatusParamsIndex(Config config, List<Caller.Param> inParams,
                                    List<ParamType> outParamsTypes, ResultSetMapper<?> rsMapper) {
        return PARAM_OFFSET + length(inParams) + length(outParamsTypes);
    }

    public int getResultSetParamIndex(int statusParamIndex, ResultSetMapper<?> rsMapper) {
        return INVALID_INDEX;
    }

    public void registerResultSet(CallableStatement statement, int resultSetParamIndex) {
        throw new UnsupportedOperationException("registerResultSet");
    }

    public ResultSet getResultSet(CallableStatement statement, int resultSetParamIndex) {
        try {
            return statement.getResultSet();
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }
}
