package spwrap.proxy;

import static spwrap.annotations.Props.*;

public class
/* struct */ Props {

    public ConnectionProps connectionProps;
    public StatementProps statementProps;
    public ResultSetProps resultSetProps;

    public static class
    /* struct */ ConnectionProps {
        public ResultSetHoldability holdability;
        public boolean readOnly;
        public TransactionIsolation transactionIsolation;
        public ResultSetType resultSetType;
        public ResultSetConcurrency resultSetConcurrency;
        public ResultSetHoldability resultSetHoldability;
    }

    public static class
    /* struct */ StatementProps {
        public String cursorName;
        public FetchDirection fetchDirection;
        public int fetchSize;
        public int maxFieldSize;
        public int maxRows;
        public int queryTimeout;
    }

    public static class
    /* struct */ ResultSetProps {
        public FetchDirection fetchDirection;
        public int fetchSize;
    }
}
