package spwrap.proxy;

import java.lang.reflect.Method;

interface Binder<T> {

	/**
	 * return <code>null</code> in case of binding failed due missing
	 * information.
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	T bind(Method method, Object... args);
}
