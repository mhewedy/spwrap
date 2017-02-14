package spwrap.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.CallException;
import spwrap.Caller;
import spwrap.Caller.ParamType;
import spwrap.Caller.TypedOutputParamMapper;

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
			log.debug("TypedOutputParamMapper overridden from @Mapper is: {} for method: {}", outParamsInstance,
					method.getName());

			metadata.outParamTypes = new ArrayList<Caller.ParamType>();
			List<Integer> types = outParamsInstance.getTypes();

			for (Integer type : types) {
				metadata.outParamTypes.add(ParamType.of(type));
			}

			log.debug("TypedOutputParamMapper Types are: {} for method: {}", metadata.outParamTypes, method.getName());
		} catch (Exception e) {
			throw new CallException("cannot create outParams Mapper", e);
		}
	}

	static void setFromReturnType(Method method, Metadata metadata) {

		Class<?> returnType = method.getReturnType();

		if (TypedOutputParamMapper.class.isAssignableFrom(returnType)) {

			@SuppressWarnings("unchecked")
			Class<TypedOutputParamMapper<?>> outParamMapperClass = (Class<TypedOutputParamMapper<?>>) returnType;

			try {
				TypedOutputParamMapper<?> outParamsInstance = outParamMapperClass.newInstance();
				metadata.outputParamMapper = outParamsInstance;

				log.debug("TypedOutputParamMapper from Method return type is: {} for method: {}", outParamsInstance,
						method.getName());
			} catch (Exception e) {
				throw new CallException("cannot create outParams Mapper", e);
			}
		}
	}
}
