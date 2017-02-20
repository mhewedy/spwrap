package spwrap.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.CallException;
import spwrap.Caller.ParamType;
import spwrap.Caller.TypedOutputParamMapper;
import spwrap.Tuple;
import spwrap.annotations.Mapper;
import spwrap.proxy.MetaData.OutputParam;

class OutputParamBinder extends MapperBinder<OutputParam, Class<TypedOutputParamMapper<?>>> {

	private static final int SECOND_GENERIC_TYPE_INDEX = 1;
	private static Logger log = LoggerFactory.getLogger(OutputParamBinder.class);

	@SuppressWarnings("unchecked")
	public OutputParam fromAnnotation(Method method) {

		Mapper mapperAnnot = method.getAnnotation(Mapper.class);
		if (mapperAnnot != null) {

			Class<TypedOutputParamMapper<?>> clazz = null;

			for (Class<?> c : mapperAnnot.value()) {
				if (TypedOutputParamMapper.class.isAssignableFrom(c)) {
					if (clazz != null) {
						throw new CallException("TypedOutputParamMapper is already registred");
					}
					clazz = (Class<TypedOutputParamMapper<?>>) c;
				}
			}
			if (clazz != null) {
				OutputParam outputParam = fromClazz(method, clazz);
				log.debug("found annotation output param: {} and types: {} for method: {}",
						outputParam.outputParamMapper.getClass(), outputParam.outParamTypes, method.getName());
				return outputParam;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public OutputParam fromReturnType(Method method) {

		OutputParam outputParam = null;

		Class<?> returnType = method.getReturnType();

		if (TypedOutputParamMapper.class.isAssignableFrom(returnType)) {

			outputParam = fromClazz(method, (Class<TypedOutputParamMapper<?>>) returnType);

		} else if (Tuple.class.isAssignableFrom(returnType)) {

			ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
			Class<?> paramClass = (Class<?>) type.getActualTypeArguments()[SECOND_GENERIC_TYPE_INDEX];

			if (TypedOutputParamMapper.class.isAssignableFrom(paramClass)) {
				outputParam = fromClazz(method, (Class<TypedOutputParamMapper<?>>) paramClass);
			}
		}

		if (outputParam != null) {
			log.debug("found return type output param: {} and types: {} for method: {}",
					outputParam.outputParamMapper.getClass(), outputParam.outParamTypes, method.getName());
		}
		return outputParam;
	}

	private OutputParam fromClazz(Method method, Class<TypedOutputParamMapper<?>> clazz) {
		OutputParam outputParam = new OutputParam();
		try {
			TypedOutputParamMapper<?> instance = clazz.newInstance();
			outputParam.outputParamMapper = clazz.newInstance();
			outputParam.outParamTypes = getOutParamTypes(method, instance);
		} catch (Exception e) {
			throw new CallException("cannot create outParams Mapper", e);
		}
		return outputParam;
	}

	private List<ParamType> getOutParamTypes(Method method, TypedOutputParamMapper<?> outParamsInstance) {

		List<ParamType> paramTypes = new ArrayList<ParamType>();
		List<Integer> types = outParamsInstance.getTypes();

		for (Integer type : types) {
			paramTypes.add(ParamType.of(type));
		}

		return paramTypes;
	}
}
