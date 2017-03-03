package spwrap.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface Param {

    /**
     * {@link java.sql.Types} static fields
     *
     * @return
     */
    int value();
}
