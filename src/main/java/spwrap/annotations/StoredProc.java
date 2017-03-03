package spwrap.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface StoredProc {

    /**
     * if not provided, the method name will be used as the stored procedure
     * name
     *
     * @return
     */
    String value() default "";
}
