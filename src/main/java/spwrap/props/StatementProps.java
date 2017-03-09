package spwrap.props;

import java.sql.CallableStatement;
import java.sql.Statement;

import static spwrap.annotations.Props.FetchDirection;

public class StatementProps implements Props<CallableStatement> {

    private boolean skip = false;

    private FetchDirection fetchDirection;
    private int fetchSize;
    private int maxFieldSize;
    private int maxRows;
    private int queryTimeout;

    public StatementProps() {
        skip = true;
    }

    public StatementProps(FetchDirection fetchDirection, int fetchSize, int maxFieldSize, int maxRows, int queryTimeout) {
        this.fetchDirection = fetchDirection;
        this.fetchSize = fetchSize;
        this.maxFieldSize = maxFieldSize;
        this.maxRows = maxRows;
        this.queryTimeout = queryTimeout;

    }

    public CallableStatement apply(CallableStatement input) {
        if (skip){
            return input;
        }else{
            // TODO apply here before return
            return input;
        }
    }
}
