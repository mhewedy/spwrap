package spwrap.proxy;

import spwrap.annotations.AutoMapper;
import spwrap.annotations.Mapper;

import java.lang.reflect.Method;

abstract class MapperBinder<T> implements Binder<T> {

    public final T bind(Method method, Object... args) {

        T outputParam = null;
        if (method.isAnnotationPresent(Mapper.class)){
            outputParam = fromAnnotation(method);;
        }

        if (outputParam == null) {
            outputParam = fromReturnType(method);
        }

        if (outputParam == null && method.isAnnotationPresent(AutoMapper.class)){
            outputParam = fromAutoMapper(method);
        }

        return outputParam;
    }

    abstract T fromAnnotation(Method method);

    abstract T fromReturnType(Method method);

    abstract T fromAutoMapper(Method method);
}
