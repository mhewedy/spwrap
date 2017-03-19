package spwrap.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static spwrap.annotations.Props.FetchDirection.FETCH_FORWARD;
import static spwrap.annotations.Props.ResultSetConcurrency.CONCUR_READ_ONLY;
import static spwrap.annotations.Props.ResultSetType.TYPE_FORWARD_ONLY;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Props {

    Connection connection() default @Connection;

    Statement statement() default @Statement;

    ResultSet resultSet() default @ResultSet;

    //-------------- Enums

    enum FetchDirection {
        FETCH_FORWARD(java.sql.ResultSet.FETCH_FORWARD),
        FETCH_REVERSE(java.sql.ResultSet.FETCH_REVERSE),
        FETCH_UNKNOWN(java.sql.ResultSet.FETCH_UNKNOWN);

        int value;

        FetchDirection(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum Isolation {
        DEFAULT(-1),    // implementation dependent
        NONE(java.sql.Connection.TRANSACTION_NONE),
        READ_UNCOMMITTED(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED),
        READ_COMMITTED(java.sql.Connection.TRANSACTION_READ_COMMITTED),
        REPEATABLE_READ(java.sql.Connection.TRANSACTION_REPEATABLE_READ),
        SERIALIZABLE(java.sql.Connection.TRANSACTION_SERIALIZABLE);

        int value;

        Isolation(int value) {
            this.value = value;
        }

        public static Isolation of(int txIsolation) {
            switch (txIsolation) {
                case -1:
                    return DEFAULT;
                case java.sql.Connection.TRANSACTION_NONE:
                    return NONE;
                case java.sql.Connection.TRANSACTION_READ_UNCOMMITTED:
                    return READ_UNCOMMITTED;
                case java.sql.Connection.TRANSACTION_READ_COMMITTED:
                    return READ_COMMITTED;
                case java.sql.Connection.TRANSACTION_REPEATABLE_READ:
                    return REPEATABLE_READ;
                case java.sql.Connection.TRANSACTION_SERIALIZABLE:
                    return SERIALIZABLE;
                default:
                    throw new IllegalArgumentException("Isolation, " + txIsolation);
            }
        }

        public int getValue() {
            return value;
        }
    }

    enum ResultSetType {
        TYPE_FORWARD_ONLY(java.sql.ResultSet.TYPE_FORWARD_ONLY),
        TYPE_SCROLL_INSENSITIVE(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE),
        TYPE_SCROLL_SENSITIVE(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE);

        int value;

        ResultSetType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum ResultSetConcurrency {
        CONCUR_READ_ONLY(java.sql.ResultSet.CONCUR_READ_ONLY),
        CONCUR_UPDATABLE(java.sql.ResultSet.CONCUR_UPDATABLE);

        int value;

        ResultSetConcurrency(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum ResultSetHoldability {
        DEFAULT(-1),
        HOLD_CURSORS_OVER_COMMIT(java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT),
        CLOSE_CURSORS_AT_COMMIT(java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT);

        int value;

        ResultSetHoldability(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    // ------------- Sub Annotations

    @Retention(RUNTIME)
    @Target(METHOD)
    @interface Connection {
        boolean readOnly() default false;

        Isolation isolation() default Isolation.DEFAULT;
    }

    @Retention(RUNTIME)
    @Target(METHOD)
    @interface Statement {
        int queryTimeout() default 0;
    }

    @Retention(RUNTIME)
    @Target(METHOD)
    @interface ResultSet {
        FetchDirection fetchDirection() default FETCH_FORWARD;

        int fetchSize() default 0;

        int maxFieldSize() default 0;

        int maxRows() default 0;

        ResultSetType type() default TYPE_FORWARD_ONLY;

        ResultSetConcurrency concurrency() default CONCUR_READ_ONLY;

        ResultSetHoldability holdability() default ResultSetHoldability.DEFAULT;
    }
}
