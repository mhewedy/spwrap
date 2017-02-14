package spwrap.annotations;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.Caller;
import spwrap.Caller.ParamType;
import spwrap.Caller.ResultSetMapper;
import spwrap.Result;
import spwrap.annotations.Mapper.NullResultSetMapper;
import spwrap.annotations.Mapper.NullTypedOutputParamMapper;
import spwrap.annotations.Mapper.TypedOutputParamMapper;

public class CallerInvocationHandler implements InvocationHandler {

	private static Logger log = LoggerFactory.getLogger(CallerInvocationHandler.class);

	private final DataSource dataSource;

	public CallerInvocationHandler(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Metadata metaData = getMetaData(method, args);

		Caller caller = new Caller(dataSource);
		Result<?, ?> call = caller.call(metaData.storedProcName, metaData.inParams, metaData.outParamTypes,
				metaData.outputParamMapper, metaData.rsMapper);
		return call;
	}

	private Metadata getMetaData(Method method, Object[] args) {
		Metadata metadata = new Metadata();

		StoredProc storedProcAnnot = method.getDeclaredAnnotation(StoredProc.class);
		if (storedProcAnnot != null) {

			String storedProc = storedProcAnnot.value();
			if (storedProc == null || storedProc.trim().isEmpty()) {
				storedProc = method.getName();
			}
			metadata.storedProcName = storedProc;
			log.debug("storedProcName name is: {} for method: {}", storedProc, method.getName());

			setInParams(method, args, metadata);

			Mapper mapperAnnot = method.getDeclaredAnnotation(Mapper.class);
			if (mapperAnnot != null) {
				setOutputParamsAndMapper(method, metadata, mapperAnnot);
				setResultSetMapper(method, metadata, mapperAnnot);
			}

		} else {
			log.warn("method {} doesn't declare @StoredProc annotation, skipping.", method.getName());
		}

		return metadata;
	}

	private void setResultSetMapper(Method method, Metadata metadata, Mapper mapperAnnot) {
		Class<? extends ResultSetMapper<?>> resultSet = mapperAnnot.resultSet();
		if (resultSet != NullResultSetMapper.class) {

			try {
				ResultSetMapper<?> resultSetMapper = resultSet.newInstance();
				metadata.rsMapper = resultSetMapper;
				log.debug("resultSetMapper is: {} for method: {}", resultSetMapper, method.getName());

			} catch (Exception e) {
				throw new RuntimeException("cannot create resultSet Mapper", e);
			}
		}
	}

	private void setOutputParamsAndMapper(Method method, Metadata metadata, Mapper mapperAnnot) {
		Class<? extends TypedOutputParamMapper<?>> outParams = mapperAnnot.outParams();
		if (outParams != NullTypedOutputParamMapper.class) {

			try {

				TypedOutputParamMapper<?> outParamsInstance = outParams.newInstance();
				metadata.outputParamMapper = outParamsInstance;
				log.debug("outputParamMapper is: {} for method: {}", outParamsInstance, method.getName());

				metadata.outParamTypes = new ArrayList<Caller.ParamType>();
				List<Integer> types = outParamsInstance.getSQLTypes();

				for (Integer type : types) {
					metadata.outParamTypes.add(ParamType.of(type));
				}

				log.debug("outParamTypes are: {} for method: {}", metadata.outParamTypes, method.getName());

			} catch (Exception e) {
				throw new RuntimeException("cannot create outParams Mapper", e);
			}
		}
	}

	private void setInParams(Method method, Object[] args, Metadata metadata) {
		Parameter[] parameters = method.getParameters();
		if (parameters.length > 0) {
			metadata.inParams = new ArrayList<Caller.Param>();
		}

		for (int i = 0; i < parameters.length; i++) {

			Parameter parameter = parameters[i];
			spwrap.annotations.Param paramAnnot = parameter.getDeclaredAnnotation(spwrap.annotations.Param.class);
			if (paramAnnot == null) {
				throw new IllegalArgumentException("missing @Param annotation on parameter: " + parameter.getName());
			} else {
				metadata.inParams.add(Caller.Param.of(args[i], paramAnnot.value()));
			}
		}

		log.debug("inParams are: {} for method: {}", metadata.inParams, method.getName());
	}

}
