package spwrap.proxy;

import java.lang.reflect.Method;

import spwrap.CallException;
import spwrap.Caller.ResultSetMapper;
import spwrap.Caller.TypedOutputParamMapper;
import spwrap.annotations.Mapper;

class MapperBinder {

	@SuppressWarnings("unchecked")
	static void setMappers(Method method, Metadata metadata) {

		ResultSetMapperBinder.setFromReturnType(method, metadata);
		OutputParamMapperBinder.setFromReturnType(method, metadata);

		Mapper mapperAnnot = method.getDeclaredAnnotation(Mapper.class);
		if (mapperAnnot != null) {

			Class<ResultSetMapper<?>> rsMapperClass = null;
			Class<TypedOutputParamMapper<?>> outParamMapperClass = null;

			validate(mapperAnnot);

			for (Class<?> clazz : mapperAnnot.value()) {
				if (ResultSetMapper.class.isAssignableFrom(clazz)) {
					if (rsMapperClass != null) {
						throw new CallException("ResultSetMapper is already registred");
					}
					rsMapperClass = (Class<ResultSetMapper<?>>) clazz;
				}

				if (TypedOutputParamMapper.class.isAssignableFrom(clazz)) {
					if (outParamMapperClass != null) {
						throw new CallException("TypedOutputParamMapper is already registred");
					}
					outParamMapperClass = (Class<TypedOutputParamMapper<?>>) clazz;
				}
			}

			ResultSetMapperBinder.overrideFromAnnotation(method, metadata, rsMapperClass);
			OutputParamMapperBinder.overrideFromAnnotation(method, metadata, outParamMapperClass);
		}
	}

	private static void validate(Mapper mapperAnnot) {
		Class<?>[] mapperClasses = mapperAnnot.value();

		if (mapperClasses.length > 2) {
			throw new CallException("only two mapper classes are allowed");
		}

		for (Class<?> clazz : mapperClasses) {
			if (!ResultSetMapper.class.isAssignableFrom(clazz)
					&& !TypedOutputParamMapper.class.isAssignableFrom(clazz)) {
				throw new CallException(
						"Mapper classes should implement either ResultSetMapper or TypedOutputParamMapper");
			}
		}
	}

}
