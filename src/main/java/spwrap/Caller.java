package spwrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spwrap.db.Database;
import spwrap.db.GenericDatabase;
import spwrap.mappers.OutputParamMapper;
import spwrap.mappers.ResultSetMapper;
import spwrap.props.ConnectionProps;
import spwrap.props.ResultSetProps;
import spwrap.props.StatementProps;
import spwrap.result.Result;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Execute Stored Procedures. <p> Read the wiki and github at http://github.com/mhewedy/spwrap README for more
 * information
 *
 * @author mhewedy
 */
public class Caller {

    private static Logger log = LoggerFactory.getLogger(Caller.class);

    private static final int JDBC_PARAM_OFFSIT = 1;
    private static final int NUM_OF_STATUS_FIELDS = 2;

    private Config config = new Config();

    private final DataSource dataSource;

    private final String jdbcUrl;
    private final String username;
    private final String password;

    Caller(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource cannot be null");
        }
        this.jdbcUrl = null;
        this.username = null;
        this.password = null;
        this.dataSource = dataSource;
    }

    Caller(String jdbcUrl, String username, String password) {
        if (jdbcUrl == null) {
            throw new IllegalArgumentException("jdbcUrl cannot be null");
        }
        this.dataSource = null;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    void setConfig(Config config) {
        this.config = config;
    }

    /**
     * execute SP with input parameters and result list of result from result
     *
     * @param procName stored procedure name
     * @param inParams a list of input parameters values and types
     * @param rsMapper a mapper object that maps result list to type T
     * @return a list of object T that represents the result set
     * @see #call(String, java.util.List, java.util.List, OutputParamMapper, ResultSetMapper)
     * @see #params(Param...)
     * @see ResultSetMapper
     */
    public final <T> List<T> call(String procName, List<Param> inParams, ResultSetMapper<T> rsMapper) {
        return call(procName, inParams, null, null, rsMapper).list();
    }

    /**
     * execute SP without input parameters and with result list
     *
     * @param procName stored procedure name
     * @param rsMapper a mapper object that maps result list to type T
     * @return a list of object T that represents the result set
     * @see #call(String, java.util.List, java.util.List, OutputParamMapper, ResultSetMapper)
     * @see ResultSetMapper
     */
    public final <T> List<T> call(String procName, ResultSetMapper<T> rsMapper) {
        return call(procName, null, rsMapper);
    }

    /**
     * execute SP with list of input parameters without returning any result
     *
     * @param procName stored procedure name
     * @param inParams a list of input parameters values and types
     * @see #call(String, java.util.List, java.util.List, OutputParamMapper, ResultSetMapper)
     */
    public final void call(String procName, List<Param> inParams) {
        call(procName, inParams, null, null);
    }

    /**
     * execute SP with no input nor output parameters
     *
     * @param procName stored procedure name
     * @see #call(String, java.util.List, java.util.List, OutputParamMapper, ResultSetMapper)
     */
    public final void call(String procName) {
        call(procName, null, null, null);
    }

    /**
     * execute SP with no input parameters and return output parameters
     *
     * @param procName       stored procedure name
     * @param outParamsTypes a list of output parameter types
     * @param paramMapper    a mapper object that maps output parameters to type T
     * @return object of type T that represents the output parameters
     * @see #call(String, java.util.List, java.util.List, OutputParamMapper, ResultSetMapper)
     * @see #paramTypes(int...)
     * @see OutputParamMapper
     */
    public final <T> T call(String procName, List<ParamType> outParamsTypes, OutputParamMapper<T> paramMapper) {
        return call(procName, null, outParamsTypes, paramMapper);
    }


    /**
     * <p>
     * Method that takes Stored Proc call string {call SP(?, ...)} and additional -
     * optional - parameters (input parameters, output parameters, output
     * parameters mapping function and result set mapping function), and execute
     * the Stored Proc with input parameters (if not null) and return the output
     * parameters through the output parameters mapping function (if both output
     * parameter types and output parameter mapping functions are not null)
     * <p>
     * If there's a result set and the result set mapping function is not null,
     * then it returns a list of results.
     * <p>
     * <p>
     * All other call methods in this class are just a overloaded version of
     * this method
     *
     * @param procName       stored procedure name
     * @param inParams       a list of output parameter types
     * @param outParamsTypes a list of output parameter types
     * @param paramMapper    a mapper object that maps output parameters to type T
     * @param rsMapper       a list of object T that represents the result set
     * @return object of type T that represents the output parameters
     * @see #params(Param...)
     * @see #paramTypes(int...)
     * @see ResultSetMapper
     * @see OutputParamMapper
     * @see spwrap.annotations.Props
     */
    public final <T, U> Tuple<T, U> call(String procName, List<Param> inParams, List<ParamType> outParamsTypes,
                                         OutputParamMapper<U> paramMapper, ResultSetMapper<T> rsMapper) {
        return call(procName, inParams, outParamsTypes, paramMapper, rsMapper, new ConnectionProps(), new StatementProps(), new ResultSetProps());
    }

    /**
     * execute SP with list of input parameters and return list of output
     * parameters
     *
     * @param procName       stored procedure name
     * @param inParams       a list of output parameter types
     * @param outParamsTypes a list of output parameter types
     * @param paramMapper    a mapper object that maps output parameters to type T
     * @return object of type T that represents the output parameters
     * @see #call(String, java.util.List, java.util.List, OutputParamMapper, ResultSetMapper)
     * @see #paramTypes(int...)
     * @see OutputParamMapper
     */
    public final <T> T call(String procName, List<Param> inParams, List<ParamType> outParamsTypes,
                            OutputParamMapper<T> paramMapper) {
        return call(procName, inParams, outParamsTypes, paramMapper, null).object();
    }


    public final <T, U> Tuple<T, U> call(String procName
            , List<Param> inParams
            , List<ParamType> outParamsTypes
            , OutputParamMapper<U> paramMapper
            , ResultSetMapper<T> rsMapper
            , ConnectionProps connectionProps
            , StatementProps statementProps
            , ResultSetProps resultSetProps) {

        final long startTime = System.currentTimeMillis();

        Connection con = null;
        CallableStatement call = null;
        ResultSet rs = null;
        Tuple<T, U> result = null;
        ConnectionProps backupConProps = null;

        final String callableStmt = Util.createCallableString(procName, (config.useStatusFields() ? NUM_OF_STATUS_FIELDS : 0)
                        + (inParams != null ? inParams.size() : 0) + (outParamsTypes != null ? outParamsTypes.size() : 0));
        try {

            if (dataSource != null) {
                con = dataSource.getConnection();
            } else if (jdbcUrl != null) {
                con = DriverManager.getConnection(jdbcUrl, username, password);
            } else {
                throw new CallException("both dataSource and jdbcUrl are nulls");
            }

            backupConProps = ConnectionProps.from(con);
            connectionProps.apply(con);
            call = statementProps.apply(con.prepareCall(callableStmt));

            int startOfOutParamCnt = inParams != null ? inParams.size() : 0;

            if (inParams != null) {
                log.debug("setting input parameters");
                for (int i = 0; i < inParams.size(); i++) {
                    int jdbcParamId = i + JDBC_PARAM_OFFSIT;
                    call.setObject(jdbcParamId, inParams.get(i).value, inParams.get(i).sqlType);
                }
            }

            if (outParamsTypes != null) {
                log.debug("registering output parameters");
                for (int i = 0; i < outParamsTypes.size(); i++) {
                    call.registerOutParameter(i + startOfOutParamCnt + JDBC_PARAM_OFFSIT,
                            outParamsTypes.get(i).sqlType);
                }
            }

            int resultCodeIndex = (inParams != null ? inParams.size() : 0)
                    + (outParamsTypes != null ? outParamsTypes.size() : 0) + 1;
            if (config.useStatusFields()) {
                log.debug("registering status parameters");
                call.registerOutParameter(resultCodeIndex, Types.BOOLEAN); // RESULT_CODE
                call.registerOutParameter(resultCodeIndex + 1, Types.VARCHAR); // RESULT_MSG
            }

            Database database = GenericDatabase.from(con);
            boolean hasResult = database.executeCall(call);

            List<T> list = null;
            if (hasResult && rsMapper != null) {
                list = new ArrayList<T>();
                log.debug("reading result set");
                rs = resultSetProps.apply(call.getResultSet());
                int rowIndex = 0;
                while (rs.next()) {
                    list.add(rsMapper.map(Result.of(rs, null, -1, rowIndex++)));
                }
            }

            U object = null;
            if (outParamsTypes != null) {
                log.debug("reading output parameters");
                for (ParamType ignored : outParamsTypes) {
                    object = paramMapper.map(Result.of(null, call, startOfOutParamCnt, -1));
                }
            }

            if (config.useStatusFields()) {
                log.debug("reading status parameters");
                short resultCode = call.getShort(resultCodeIndex);
                if (resultCode != config.successCode()) {
                    String resultMsg = call.getString(resultCodeIndex + 1);
                    throw new CallException(resultCode, resultMsg);
                }
            }

            result = new Tuple<T, U>(list, object);

        } catch (CallException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("[" + callableStmt + "]", ex.getMessage());
            throw new CallException(ex.getMessage(), ex);
        } finally {
            logCall(startTime, callableStmt, inParams, outParamsTypes, result);
            if (backupConProps != null ) backupConProps.apply(con);
            Util.closeDBObjects(con, call, rs);
        }
        return result;
    }

    private <T, U> void logCall(long startTime, String callableStmt, List<Param> inParams, List<ParamType> outParamsTypes, Tuple<T, U> result) {
        if (log.isDebugEnabled()) {
            log.debug("\n>call sp: [{}] \n>\tIN Params: {}, \n>\tOUT Params Types: {}\n>\tResult: {}; \n>>\ttook: {} ms", callableStmt,
                    inParams != null ? Arrays.toString(inParams.toArray()) : "null",
                    outParamsTypes != null ? Arrays.toString(outParamsTypes.toArray()) : "null", result,
                    (System.currentTimeMillis() - startTime));
        } else {
            log.info(">call sp: [{}] took: {} ms", callableStmt, (System.currentTimeMillis() - startTime));
        }
    }

    // -------------- Classes and factory functions for dealing with Caller

    public static class ParamType {
        int sqlType;

        public static ParamType of(int sqlType) {
            ParamType p = new ParamType();
            p.sqlType = sqlType;
            return p;
        }

        @Override
        public String toString() {
            return Util.getAsString(sqlType);
        }
    }

    public static class Param extends ParamType {
        private Object value;

        public static Param of(Object value, int sqlType) {
            Param p = new Param();
            p.value = value;
            p.sqlType = sqlType;
            return p;
        }

        @Override
        public String toString() {
            return "[value=" + value + ", type=" + Util.getAsString(sqlType) + "]";
        }
    }

    public static List<ParamType> paramTypes(int... sqlTypes) {
        List<ParamType> pts = new ArrayList<ParamType>();
        for (int type : sqlTypes) {
            ParamType pt = new ParamType();
            pt.sqlType = type;
            pts.add(pt);
        }
        return pts;
    }

    public static List<Param> params(Param... params) {
        List<Param> pm = new ArrayList<Param>();
        Collections.addAll(pm, params);
        return pm;
    }
}
