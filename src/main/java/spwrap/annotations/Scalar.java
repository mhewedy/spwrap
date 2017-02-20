package spwrap.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Special case of {@link Mapper} that map single output parameter automatically
 * to the method return type.
 * 
 * @author mhewedy
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Scalar {

	/**
	 * {@link java.sql.Types} static fields to represent the type of the only
	 * output parameter of the stored procedure
	 * 
	 * @return
	 */
	int value();
}
