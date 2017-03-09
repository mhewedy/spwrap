package spwrap.props;

import java.sql.ResultSet;

import static spwrap.annotations.Props.*;

public class ResultSetProps implements Props<ResultSet> {

    private boolean skip = false;

    private ResultSetType type;
    private ResultSetConcurrency concurrency;
    private ResultSetHoldability holdability;

    public ResultSetProps() {
        skip = true;
    }

    public ResultSetProps(ResultSetType type, ResultSetConcurrency concurrency, ResultSetHoldability holdability) {
        this.type = type;
        this.concurrency = concurrency;
        this.holdability = holdability;
    }

    public ResultSet apply(ResultSet input) {
        if (skip){
            return input;
        }else{
            // TODO apply here before return
            return input;
        }
    }
}
