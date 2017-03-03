package spwrap.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import spwrap.mappers.ResultSetMapper;
import spwrap.mappers.TypedOutputParamMapper;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Mapper {

    /**
     * Classes that implement either {@link TypedOutputParamMapper} or
     * {@link ResultSetMapper}
     * <p>
     * <p>
     * Maximum 2 classes allowed, one that implements each interface.
     *
     * @return
     */
    Class<? extends spwrap.mappers.Mapper<?>>[] value();
}
