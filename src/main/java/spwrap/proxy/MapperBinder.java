package spwrap.proxy;

import java.lang.reflect.Method;

abstract class MapperBinder<T> implements Binder<T> {

    public final T bind(Method method, Object... args) {

        T outputParam = fromAnnotation(method);

        if (outputParam == null) {
            outputParam = fromReturnType(method);
        }
        return outputParam;
    }

    abstract T fromAnnotation(Method method);

    abstract T fromReturnType(Method method);
}
