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

import static spwrap.Util.length;

/**
 * <p>
 * Execute Stored Procedures. <p> Read the wiki and github at http://github.com/mhewedy/spwrap README for more
 * information
 *
 * @author mhewedy
 */
public class Caller {

    private static Logger log = LoggerFactory.getLogger(Caller.class);

    public static final int PARAM_OFFSET = 1;
    public static final int INVALID_INDEX = -1;

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

    public final <T, U> Tuple<T, U> call(String procName,
                                         List<Param> inParams,
                                         List<ParamType> outParamsTypes,
                                         OutputParamMapper<U> paramMapper,
                                         ResultSetMapper<T> rsMapper,
                                         ConnectionProps connectionProps,
                                         StatementProps statementProps,
                                         ResultSetProps resultSetProps) {

        final long startTime = System.currentTimeMillis();

        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;

        Tuple<T, U> result = null;
        ConnectionProps connectionPropsBkup = null;
        String callString = null;
        Boolean connChanged = false;

        ConnectionManager connectionManager = ConnectionManager.instance();

        try {
            connection = connectionManager.getConnection(dataSource, jdbcUrl, username, password);
            Database database = GenericDatabase.from(connection);

            callString = database.getCallString(procName, config, inParams, outParamsTypes, rsMapper);

            // ----- Applying JDBC Object Props
            connectionPropsBkup = ConnectionProps.from(connection);
            connChanged = connectionProps.apply(connection);
            statement = resultSetProps.apply(connection, callString);
            statementProps.apply(statement);

            // ------- calc some indexes

            int outParamIndex = length(inParams);
            int statusParamsIndex = database.getStatusParamsIndex(config, inParams, outParamsTypes, rsMapper);
            int resultSetParamIndex = database.getResultSetParamIndex(statusParamsIndex, rsMapper);

            // ------ Setting input parameters

            if (inParams != null) {
                log.debug("setting input parameters");
                for (int i = 0; i < inParams.size(); i++) {
                    statement.setObject(i + PARAM_OFFSET, inParams.get(i).value, inParams.get(i).sqlType);
                }
            }

            // ------ Register output parameters

            if (outParamsTypes != null) {
                log.debug("registering output parameters");
                for (int i = 0; i < outParamsTypes.size(); i++) {
                    statement.registerOutParameter(i + PARAM_OFFSET + outParamIndex, outParamsTypes.get(i).sqlType);
                }
            }

            if (resultSetParamIndex != INVALID_INDEX){
                database.registerResultSet(statement, resultSetParamIndex);
            }

            if (config.useStatusFields()) {
                log.debug("registering status parameters");
                statement.registerOutParameter(statusParamsIndex, Types.SMALLINT); // RESULT_CODE
                statement.registerOutParameter(statusParamsIndex + 1, Types.VARCHAR); // RESULT_MSG
            }

            // -------- Getting result set

            List<T> list = null;
            boolean hasResult = database.executeCall(statement);
            if (hasResult || resultSetParamIndex != INVALID_INDEX) {
                resultSet = database.getResultSet(statement, resultSetParamIndex);
                list = mapResultSet(resultSet, rsMapper);
            }

            // --------- Read output parameters

            U object = null;
            if (outParamsTypes != null) {
                log.debug("reading output parameters");
                object = paramMapper.map(Result.of(null, statement, outParamIndex, INVALID_INDEX));
            }

            if (config.useStatusFields()) {
                log.debug("reading status parameters");
                short resultCode = statement.getShort(statusParamsIndex);
                if (resultCode != config.successCode()) {
                    String resultMsg = statement.getString(statusParamsIndex + 1);
                    throw new CallException(resultCode, resultMsg);
                }
            }

            result = new Tuple<T, U>(list, object);

        } catch (CallException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("[" + callString + "]", ex.getMessage());
            throw new CallException(ex.getMessage(), ex);
        } finally {
            logCall(startTime, callString, inParams, outParamsTypes, result);
            if (connection != null && connectionPropsBkup != null && connChanged){
                log.debug("setting the connection props back from the backed-up Props object");
                connectionPropsBkup.apply(connection);
            }
            Util.closeDBObjects(null, statement, resultSet);
            connectionManager.closeConnection(connection, dataSource);
        }
        return result;
    }

    private <T, U> void logCall(long startTime, String callableStmt, List<Param> inParams,
                                List<ParamType> outParamsTypes, Tuple<T, U> result) {
        if (log.isDebugEnabled()) {
            log.debug("\n>call sp: [{}] \n>\tIN Params: {}, \n>\tOUT Params Types: {}\n>\tResult: {}; \n>>\ttook: {} ms",
                    callableStmt, inParams != null ? Arrays.toString(inParams.toArray()) : "null",
                    outParamsTypes != null ? Arrays.toString(outParamsTypes.toArray()) : "null", result,
                    (System.currentTimeMillis() - startTime));
        } else {
            log.info(">call sp: [{}] took: {} ms", callableStmt, (System.currentTimeMillis() - startTime));
        }
    }

    private <T> List<T> mapResultSet(ResultSet resultSet, ResultSetMapper<T> resultSetMapper) throws SQLException {
        List<T> list = null;
        if (resultSetMapper != null) {
            list = new ArrayList<T>();
            log.debug("reading result set");
            int rowIndex = 0;
            while (resultSet.next()) {
                list.add(resultSetMapper.map(Result.of(resultSet, null, INVALID_INDEX, rowIndex++)));
            }
        }
        return list;
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
