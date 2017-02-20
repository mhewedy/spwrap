package spwrap.proxy;

import java.lang.reflect.Method;

interface MapperBinder<T, C> extends Binder<T> {

	T fromAnnotation(Method method);

	T fromReturnType(Method method);
}
