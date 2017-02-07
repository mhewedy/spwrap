package spwrap;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All execute methods in this class expects 2 output parameters: <br />
 * code (of type BOOLEAN) and message (of type VARCHAR) <br />
 * and these 2 output parameters should be the last parameters in the SP
 * parameter list parameters. <br />
 * <br />
 * Note, you are freely to choose these 2 output parameters names <br />
 * <br />
 * This class is Thread-safe, It is good practice to reuse a single instance of
 * it.
 * 
 * <br />
 * <br />
 * 
 * This class consider the return value of parameter result code (first of the
 * two mandatory output parameters mentioned about), it consider it success if
 * it is value is returned as zero, false otherwise. <br />
 * <br />
 * You can override the success value by setting the system property <br />
 * {@code spwarp.success_code} to any short value.
 * 
 * @author mhewedy
 *
 */
public class Caller {

	private static Logger log = LoggerFactory.getLogger(Caller.class);

	private static final int JDBC_PARAM_OFFSIT = 1;
	private static final int COUNT_OF_RESULT_PARAMS = 2;

	private static final String SUCCESS_CODE_PROP = "spwarp.success_code";

	private static final short SUCCESS = System.getProperty(SUCCESS_CODE_PROP) != null
			? Short.parseShort(System.getProperty(SUCCESS_CODE_PROP)) : 0;

	private final DataSource dataSource;

	private final String jdbcUrl;
	private final String username;
	private final String password;

	public Caller(DataSource dataSource) {
		this.jdbcUrl = null;
		this.username = null;
		this.password = null;
		this.dataSource = dataSource;
	}

	/**
	 * Use {@link #Caller(DataSource)} when possible
	 * 
	 * @param jdbcUrl
	 * @param username
	 * @param password
	 */
	public Caller(String jdbcUrl, String username, String password) {
		super();
		this.dataSource = null;
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
	}

	/**
	 * execute SP with input parameters and result list of result from result
	 * 
	 * @param procName
	 * @param inParams
	 * @param rsMapper
	 * @return
	 * @see {@link #call(String, List, List, OutputParamMapper, ResultSetMapper)}
	 */
	public final <T> List<T> call(String procName, List<Param> inParams, ResultSetMapper<T> rsMapper) {
		return call(procName, inParams, null, null, rsMapper).list();
	}

	/**
	 * execute SP without input parameters and with result list
	 * 
	 * @param procName
	 * @param rsMapper
	 * @return
	 */
	public final <T> List<T> call(String procName, ResultSetMapper<T> rsMapper) {
		return call(procName, null, rsMapper);
	}

	/**
	 * execute SP with list of input parameters without returning any result
	 * 
	 * @param procName
	 * @param inParams
	 * @return
	 * @see {@link #call(String, List, List, OutputParamMapper, ResultSetMapper)}
	 */
	public final void call(String procName, List<Param> inParams) {
		call(procName, inParams, null, null);
	}

	/**
	 * execute SP with no input nor output parameters
	 * 
	 * @param procName
	 * @return
	 * @see {@link #call(String, List, List, OutputParamMapper, ResultSetMapper)}
	 */
	public final void call(String procName) {
		call(procName, null, null, null);
	}

	/**
	 * execute SP with no input parameters and return output parameters
	 * 
	 * @param procName
	 * @param outParamsTypes
	 *            (don't add types for result code and result message)
	 * @param paramMapper
	 * @return
	 * @see {@link #call(String, List, List, OutputParamMapper, ResultSetMapper)}
	 */
	public final <T> T call(String procName, List<ParamType> outParamsTypes, OutputParamMapper<T> paramMapper) {
		return call(procName, null, outParamsTypes, paramMapper);
	}

	/**
	 * execute SP with list of input parameters and return list of output
	 * parameters
	 * 
	 * @param procName
	 * @param inParams
	 * @param outParamsTypes
	 *            (don't add types for result code and result message)
	 * @param paramMapper
	 * @return
	 * @see {@link #call(String, List, List, OutputParamMapper, ResultSetMapper)}
	 */
	public final <T> T call(String procName, List<Param> inParams, List<ParamType> outParamsTypes,
			OutputParamMapper<T> paramMapper) {
		return call(procName, inParams, outParamsTypes, paramMapper, null).object();
	}

	/**
	 * Method that takes SP call string {call SP(?, ...)} and additional -
	 * optional - parameters (input parameters, output parameters, output
	 * parameters mapping function and result set mapping function), and execute
	 * the SP with input parameters (if not null) and return the output
	 * parameters through the output parameters mapping function (if both output
	 * parameter types and output parameter mapping functions are not null) and
	 * if there's a result set and the result set mapping function is not null,
	 * then it returns a list of results.
	 * 
	 * <br />
	 * <br />
	 * All other execute methods in this class are just a shortcut version of
	 * this method
	 * 
	 * @param procName
	 * @param inParams
	 * @param outParamsTypes
	 * @param paramMapper
	 * @param rsMapper
	 * @return
	 */
	public final <T, U> Result<T, U> call(String procName, List<Param> inParams, List<ParamType> outParamsTypes,
			OutputParamMapper<U> paramMapper, ResultSetMapper<T> rsMapper) {
		
		final long startTime = System.currentTimeMillis();

		Connection con = null;
		CallableStatement call = null;
		ResultSet rs = null;
		final String callableStmt = Util.createCallableString(procName, COUNT_OF_RESULT_PARAMS
				+ (inParams != null ? inParams.size() : 0) + (outParamsTypes != null ? outParamsTypes.size() : 0));

		Result<T, U> result = null;
		try {

			if (dataSource != null) {
				con = dataSource.getConnection();
			} else {
				con = DriverManager.getConnection(jdbcUrl, username, password);
			}

			call = con.prepareCall(callableStmt);

			int startOfOutParamCnt = 1;

			if (inParams != null) {
				for (int i = 0; i < inParams.size(); i++) {
					int jdbcParamId = i + JDBC_PARAM_OFFSIT;
					call.setObject(jdbcParamId, inParams.get(i).value, inParams.get(i).sqlType);
					startOfOutParamCnt = jdbcParamId + 1;
				}
			}

			if (outParamsTypes != null) {
				log.debug("setting input parameters");
				for (int i = 0; i < outParamsTypes.size(); i++) {
					call.registerOutParameter(i + startOfOutParamCnt, outParamsTypes.get(i).sqlType);
				}
			}

			int resultCodeIndex = (inParams != null ? inParams.size() : 0)
					+ (outParamsTypes != null ? outParamsTypes.size() : 0) + 1;
			call.registerOutParameter(resultCodeIndex, Types.BOOLEAN); // RESULT_CODE
			call.registerOutParameter(resultCodeIndex + 1, Types.VARCHAR); // RESULT_MSG

			boolean hasResult = call.execute();

			List<T> list = null;
			if (hasResult && rsMapper != null) {
				list = new ArrayList<T>();
				log.debug("reading result set");
				rs = call.getResultSet();
				while (rs.next()) {
					list.add(rsMapper.map(new spwrap.ResultSet(rs)));
				}
			}

			U object = null;
			if (outParamsTypes != null) {
				log.debug("reading output parameters");
				for (int i = 0; i < outParamsTypes.size(); i++) {
					object = paramMapper.map(call, startOfOutParamCnt);
				}
			}

			short resultCode = call.getShort(resultCodeIndex);
			if (resultCode != SUCCESS) {
				String resultMsg = call.getString(resultCodeIndex + 1);
				throw new CallException(
						"error code: " + resultCode + (resultMsg != null ? (", error msg: " + resultMsg) : ""));
			}
			
			result = new Result<T, U>(list, object);

		} catch (Exception ex) {
			log.error("[" + callableStmt + "]", ex.getMessage());
			throw new CallException(ex.getMessage(), ex);
		} finally {
			Util.closeDBObjects(con, call, rs);

			log.info(">call sp: [{}] \nInParams: {}, \nOutParams Types: {}\nResult: {}; \ntook: {}", callableStmt,
					inParams != null ? Arrays.toString(inParams.toArray()) : "null",
					outParamsTypes != null ? Arrays.toString(outParamsTypes.toArray()) : "null", result,
					(System.currentTimeMillis() - startTime) + " ms");
		}
		return result;
	}

	// --------------

	public static interface OutputParamMapper<T> {
		T map(CallableStatement call, int index) throws SQLException;
	}

	public static interface ResultSetMapper<T> {
		T map(spwrap.ResultSet rs);
	}

	// -------------

	private static class ParamType {
		protected int sqlType;

		@Override
		public String toString() {
			return sqlType + "";
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
			return "[value=" + value + ", sqlType=" + sqlType + "]";
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
		for (Param param : params) {
			pm.add(param);
		}
		return pm;
	}
}
