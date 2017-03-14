package spwrap.db;

import spwrap.Config;
import spwrap.mappers.ResultSetMapper;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static spwrap.Caller.Param;
import static spwrap.Caller.ParamType;

public interface Database {

    boolean executeCall(CallableStatement call) throws SQLException;

    String getCallString(String procName, Config config, List<Param> inParams,
                         List<ParamType> outParamsTypes, ResultSetMapper<?> rsMapper);

    int getStatusParamsIndex(Config config, List<Param> inParams,
                             List<ParamType> outParamsTypes, ResultSetMapper<?> rsMapper);

    int getResultSetParamIndex(int statusParamIndex, ResultSetMapper<?> rsMapper);

    void registerResultSet(CallableStatement statement, int resultSetParamIndex);

    ResultSet getResultSet(CallableStatement statement, int resultSetParamIndex);
}
