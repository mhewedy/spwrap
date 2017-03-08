package spwrap.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static spwrap.annotations.Props.FetchDirection.FETCH_FORWARD;
import static spwrap.annotations.Props.ResultSetConcurrency.CONCUR_READ_ONLY;
import static spwrap.annotations.Props.ResultSetHoldability.DEFAULT_HOLDABILITY;
import static spwrap.annotations.Props.ResultSetType.TYPE_FORWARD_ONLY;
import static spwrap.annotations.Props.TransactionIsolation.DEFAULT_ISOLATION;

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

        public static FetchDirection of(int fetchDireection) {
            switch (fetchDireection) {
                case java.sql.ResultSet.FETCH_FORWARD:
                    return FETCH_FORWARD;
                case java.sql.ResultSet.FETCH_REVERSE:
                    return FETCH_REVERSE;
                case java.sql.ResultSet.FETCH_UNKNOWN:
                    return FETCH_UNKNOWN;
                default:
                    throw new IllegalArgumentException("FetchDirection, " + fetchDireection);
            }
        }

        public int getValue() {
            return value;
        }
    }

    enum TransactionIsolation {
        DEFAULT_ISOLATION(-1),    // implementation dependent
        NONE(java.sql.Connection.TRANSACTION_NONE),
        READ_UNCOMMITTED(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED),
        READ_COMMITTED(java.sql.Connection.TRANSACTION_READ_COMMITTED),
        REPEATABLE_READ(java.sql.Connection.TRANSACTION_REPEATABLE_READ),
        SERIALIZABLE(java.sql.Connection.TRANSACTION_SERIALIZABLE);

        int value;

        TransactionIsolation(int value) {
            this.value = value;
        }

        public static TransactionIsolation of(int txIsolation) {
            switch (txIsolation) {
                case -1:
                    return DEFAULT_ISOLATION;
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
                    throw new IllegalArgumentException("TransactionIsolation, " + txIsolation);
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

        public static ResultSetType of(int rsType) {
            switch (rsType) {
                case java.sql.ResultSet.TYPE_FORWARD_ONLY:
                    return TYPE_FORWARD_ONLY;
                case java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE:
                    return TYPE_SCROLL_INSENSITIVE;
                case java.sql.ResultSet.TYPE_SCROLL_SENSITIVE:
                    return TYPE_SCROLL_SENSITIVE;
                default:
                    throw new IllegalArgumentException("ResultSetType, " + rsType);
            }
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

        public static ResultSetConcurrency of(int rsConcurrency) {
            switch (rsConcurrency) {
                case java.sql.ResultSet.CONCUR_READ_ONLY:
                    return CONCUR_READ_ONLY;
                case java.sql.ResultSet.CONCUR_UPDATABLE:
                    return CONCUR_UPDATABLE;
                default:
                    throw new IllegalArgumentException("ResultSetConcurrency, " + rsConcurrency);
            }
        }

        public int getValue() {
            return value;
        }
    }

    enum ResultSetHoldability {
        DEFAULT_HOLDABILITY(-1),
        HOLD_CURSORS_OVER_COMMIT(java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT),
        CLOSE_CURSORS_AT_COMMIT(java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT);

        int value;

        ResultSetHoldability(int value) {
            this.value = value;
        }

        public static ResultSetHoldability of(int holdability) {
            switch (holdability) {
                case -1:
                    return DEFAULT_HOLDABILITY;
                case java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT:
                    return HOLD_CURSORS_OVER_COMMIT;
                case java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT:
                    return CLOSE_CURSORS_AT_COMMIT;
                default:
                    throw new IllegalArgumentException("ResultSetHoldability, " + holdability);
            }
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

        TransactionIsolation transactionIsolation() default DEFAULT_ISOLATION;
    }

    @Retention(RUNTIME)
    @Target(METHOD)
    @interface Statement {

        FetchDirection fetchDirection() default FETCH_FORWARD;

        int fetchSize() default 0;

        int maxFieldSize() default 0;

        int maxRows() default 0;

        int queryTimeout() default 0;
    }

    @Retention(RUNTIME)
    @Target(METHOD)
    @interface ResultSet {
        ResultSetType type() default TYPE_FORWARD_ONLY;

        ResultSetConcurrency concurrency() default CONCUR_READ_ONLY;

        ResultSetHoldability holdability() default DEFAULT_HOLDABILITY;
    }
}
