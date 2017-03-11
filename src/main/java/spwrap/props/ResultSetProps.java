package spwrap.props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spwrap.CallException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import static spwrap.annotations.Props.*;

public class ResultSetProps implements Props<Connection, CallableStatement> {

    private static Logger log = LoggerFactory.getLogger(ResultSetProps.class);

    private boolean skip = false;

    private FetchDirection fetchDirection;
    private int fetchSize;
    private int maxFieldSize;
    private int maxRows;
    private ResultSetType type;
    private ResultSetConcurrency concurrency;
    private ResultSetHoldability holdability;

    public ResultSetProps() {
        skip = true;
    }

    public ResultSetProps(FetchDirection fetchDirection, int fetchSize, int maxFieldSize, int maxRows,
                          ResultSetType type, ResultSetConcurrency concurrency, ResultSetHoldability holdability) {
        this.fetchDirection = fetchDirection;
        this.fetchSize = fetchSize;
        this.maxFieldSize = maxFieldSize;
        this.maxRows = maxRows;
        this.type = type;
        this.concurrency = concurrency;
        this.holdability = holdability;
    }

    public CallableStatement apply(Connection input, Object... args) {
        try {
            CallableStatement call;

            if (!skip) {
                log.debug("applying {} on input Connection", this);

                if (holdability == ResultSetHoldability.DEFAULT) {
                    call = input.prepareCall((String) args[0], type.getValue(), concurrency.getValue());
                } else {
                    call = input.prepareCall((String) args[0], type.getValue(), concurrency.getValue(), holdability.getValue());
                }

                call.setFetchDirection(fetchDirection.getValue());
                call.setFetchSize(fetchSize);
                call.setMaxFieldSize(maxFieldSize);
                call.setMaxRows(maxRows);
            } else {
                call = input.prepareCall((String) args[0]);
            }
            return call;
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public String toString() {
        return "ResultSetProps{" +
                "skip=" + skip +
                ", fetchDirection=" + fetchDirection +
                ", fetchSize=" + fetchSize +
                ", maxFieldSize=" + maxFieldSize +
                ", maxRows=" + maxRows +
                ", type=" + type +
                ", concurrency=" + concurrency +
                ", holdability=" + holdability +
                '}';
    }
}
