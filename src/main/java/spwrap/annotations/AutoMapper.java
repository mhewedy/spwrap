package spwrap.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 *     Do auto mapping for Stored Proc result set based on third-party libraries.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AutoMapper {

}
