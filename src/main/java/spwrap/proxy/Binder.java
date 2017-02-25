package spwrap.proxy;

import spwrap.mappers.OutputParamMapper;
import spwrap.mappers.ResultSetMapper;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>
 * Bind the information arround the DAO methods into {@link MetaData} to be used when calling the {@link spwrap.Caller#call(String, List, List, OutputParamMapper, ResultSetMapper)}
 * @param <T> parts of {@link MetaData} object
 */
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
