package spwrap.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import spwrap.Caller.ResultSetMapper;
import spwrap.Caller.TypedOutputParamMapper;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Mapper {

	/**
	 * classes that implement either {@link TypedOutputParamMapper} or
	 * {@link ResultSetMapper}
	 * 
	 * <br />
	 * Maximum 2 classes allowed, one for implements each interface
	 * 
	 * @return
	 */
	Class<?>[] value();
}
