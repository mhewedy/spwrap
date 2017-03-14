package spwrap.db;

import spwrap.Caller;
import spwrap.Caller.ParamType;
import spwrap.Config;
import spwrap.mappers.ResultSetMapper;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import static spwrap.Util.createCallableString;
import static spwrap.Util.getParamsCount;

/**
 * <p>
 * Databases that the call to {@link CallableStatement#getResultSet()} doesn't return
 * the output of a query that is executed in the body of the stored procedure.
 * <p>
 * Instead, it allow to return result set as Output Parameter see {@link Oracle} for example.
 */
abstract class ResultSetAsOutParamDatabase extends GenericDatabase {

    @Override
    public boolean executeCall(CallableStatement call) throws SQLException {
        super.executeCall(call);
        return false;
    }

    @Override
    public String getCallString(String procName, Config config, List<Caller.Param> inParams,
                                List<ParamType> outParamsTypes, ResultSetMapper<?> rsMapper) {
        if (rsMapper != null) {
            return createCallableString(procName, getParamsCount(config, inParams, outParamsTypes) + 1);
        } else {
            return super.getCallString(procName, config, inParams, outParamsTypes, null);
        }
    }

    @Override
    public int getStatusParamsIndex(Config config, List<Caller.Param> inParams, List<ParamType> outParamsTypes,
                                    ResultSetMapper<?> rsMapper) {
        return (rsMapper != null ? 1 : 0) +
                super.getStatusParamsIndex(config, inParams, outParamsTypes, rsMapper);
    }

    @Override
    public int getResultSetParamIndex(int statusParamIndex, ResultSetMapper<?> rsMapper) {
        if (rsMapper != null) {
            // result set param come immediately before status params
            return statusParamIndex - 1;
        } else {
            return super.getResultSetParamIndex(statusParamIndex, null);
        }
    }
}
