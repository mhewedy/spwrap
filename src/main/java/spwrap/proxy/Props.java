package spwrap.proxy;

import static spwrap.annotations.Props.*;

public class
/* struct */ Props {

    public ConnectionProps connectionProps;
    public StatementProps statementProps;
    public ResultSetProps resultSetProps;

    public static class
    /* struct */ ConnectionProps {
        public boolean readOnly;
        public TransactionIsolation transactionIsolation;
    }

    public static class
    /* struct */ StatementProps {
        public FetchDirection fetchDirection;
        public int fetchSize;
        public int maxFieldSize;
        public int maxRows;
        public int queryTimeout;
    }

    public static class
    /* struct */ ResultSetProps {
        public ResultSetType type;
        public ResultSetConcurrency concurrency;
        public ResultSetHoldability holdability;
    }
}
