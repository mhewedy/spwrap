package spwrap.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spwrap.CallException;
import spwrap.Caller.ParamType;
import spwrap.Tuple;
import spwrap.annotations.Mapper;
import spwrap.mappers.TypedOutputParamMapper;
import spwrap.proxy.MetaData.OutputParam;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

class OutputParamBinder extends MapperBinder<OutputParam> {

    private static final int SECOND_GENERIC_TYPE_INDEX = 1;
    private static Logger log = LoggerFactory.getLogger(OutputParamBinder.class);

    @SuppressWarnings("unchecked")
    OutputParam fromAnnotation(Method method) {

        Mapper mapperAnnotation = method.getAnnotation(Mapper.class);
        if (mapperAnnotation != null) {

            Class<TypedOutputParamMapper<?>> clazz = null;

            for (Class<?> c : mapperAnnotation.value()) {
                if (TypedOutputParamMapper.class.isAssignableFrom(c)) {
                    if (clazz != null) {
                        throw new CallException("TypedOutputParamMapper is already registered");
                    }
                    clazz = (Class<TypedOutputParamMapper<?>>) c;
                }
            }
            if (clazz != null) {
                OutputParam outputParam = fromClazz(clazz);
                log.debug("found annotation output param: {} and types: {} for method: {}",
                        outputParam.outputParamMapper.getClass(), outputParam.outParamTypes, method.getName());
                return outputParam;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    OutputParam fromReturnType(Method method) {

        OutputParam outputParam = null;

        Class<?> fromClazz = getReturnType(method);

        if (TypedOutputParamMapper.class.isAssignableFrom(fromClazz)) {

            outputParam = fromClazz((Class<TypedOutputParamMapper<?>>) fromClazz);
            log.debug("found return type output param: {} and types: {} for method: {}",
                    outputParam.outputParamMapper.getClass(), outputParam.outParamTypes, method.getName());
        }
        return outputParam;
    }

    OutputParam fromAutoMapper(Method method) {
        log.debug("ResultSetAutoMapper annotation found but outputParams are not supported");
        return null;
    }

    private OutputParam fromClazz(Class<TypedOutputParamMapper<?>> clazz) {
        OutputParam outputParam = new OutputParam();
        try {
            TypedOutputParamMapper<?> instance = clazz.newInstance();
            outputParam.outputParamMapper = clazz.newInstance();
            outputParam.outParamTypes = getOutParamTypes(instance);
        } catch (Exception e) {
            throw new CallException("cannot create outParams Mapper", e);
        }
        return outputParam;
    }

    private List<ParamType> getOutParamTypes(TypedOutputParamMapper<?> outParamsInstance) {

        List<ParamType> paramTypes = new ArrayList<ParamType>();
        List<Integer> types = outParamsInstance.getTypes();

        for (Integer type : types) {
            paramTypes.add(ParamType.of(type));
        }

        return paramTypes;
    }

    private Class<?> getReturnType(Method method) {
        if (Tuple.class.isAssignableFrom(method.getReturnType())) {
            ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
            return (Class<?>) type.getActualTypeArguments()[SECOND_GENERIC_TYPE_INDEX];
        }else{
            return method.getReturnType();
        }
    }
}
