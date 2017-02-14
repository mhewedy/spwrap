package spwrap.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.CallException;
import spwrap.Caller;
import spwrap.Caller.ParamType;
import spwrap.Caller.TypedOutputParamMapper;
import spwrap.Tuple;

class OutputParamMapperBinder {

	private static Logger log = LoggerFactory.getLogger(OutputParamMapperBinder.class);

	static void overrideFromAnnotation(Method method, Metadata metadata,
			Class<TypedOutputParamMapper<?>> outParamMapperClass) {

		if (outParamMapperClass == null) {
			return;
		}

		try {
			TypedOutputParamMapper<?> outParamsInstance = outParamMapperClass.newInstance();
			metadata.outputParamMapper = outParamsInstance;

			log.debug("overrideFromAnnotation:: TypedOutputParamMapper overridden from @Mapper is: {} for method: {}",
					outParamsInstance, method.getName());

			setOutParamTypes(method, metadata, outParamsInstance);
		} catch (Exception e) {
			throw new CallException("cannot create outParams Mapper", e);
		}
	}

	@SuppressWarnings("unchecked")
	static void setFromReturnType(Method method, Metadata metadata) {

		Class<?> returnType = method.getReturnType();

		if (TypedOutputParamMapper.class.isAssignableFrom(returnType)) {

			Class<TypedOutputParamMapper<?>> outParamMapperClass = (Class<TypedOutputParamMapper<?>>) returnType;
			_steFromReturnType(method, metadata, outParamMapperClass);

		} else if (Tuple.class.isAssignableFrom(returnType)) {

			ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
			Class<?> listClass = (Class<?>) type.getActualTypeArguments()[1];

			_steFromReturnType(method, metadata, (Class<TypedOutputParamMapper<?>>) listClass);
		}
	}

	private static void _steFromReturnType(Method method, Metadata metadata,
			Class<TypedOutputParamMapper<?>> outParamMapperClass) {
		try {
			TypedOutputParamMapper<?> outParamsInstance = outParamMapperClass.newInstance();
			metadata.outputParamMapper = outParamsInstance;

			log.debug("setFromReturnType:: TypedOutputParamMapper from Method return type is: {} for method: {}",
					outParamsInstance, method.getName());

			setOutParamTypes(method, metadata, outParamsInstance);
		} catch (Exception e) {
			throw new CallException("cannot create outParams Mapper", e);
		}
	}

	private static void setOutParamTypes(Method method, Metadata metadata,
			TypedOutputParamMapper<?> outParamsInstance) {

		metadata.outParamTypes = new ArrayList<Caller.ParamType>();
		List<Integer> types = outParamsInstance.getTypes();

		for (Integer type : types) {
			metadata.outParamTypes.add(ParamType.of(type));
		}

		log.debug("TypedOutputParamMapper Types are: {} for method: {}", metadata.outParamTypes, method.getName());
	}
}
