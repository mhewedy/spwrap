package spwrap.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 *     Special case of {@link Mapper} that map single output parameter automatically
 *     to the method return type.
 *
 * <p>
 *     The mapping done using {@link java.sql.CallableStatement#getObject(int)} method, meaning that
 *     if the result wasNull and the DAO method returns a primitive value, then NullPointerException will thrown
 *     by java trying to cast null into primitive.
 *
 * <p>
 *     So, always when using this annotation, make the return type of the DAO method as
 *     Java Wrapper object (Integer, Short ,etc..) instead of  (int, short, etc... primitive).
 *
 * @author mhewedy
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
