package spwrap.props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spwrap.CallException;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class StatementProps implements Props<CallableStatement, Void> {

    private static Logger log = LoggerFactory.getLogger(StatementProps.class);

    private boolean skip = false;

    private int queryTimeout;

    public StatementProps() {
        skip = true;
    }

    public StatementProps(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public Void apply(CallableStatement input, Object ... args) {
        if (!skip){
            log.debug("applying {} on input CallableStatement", this);
            try {
                input.setQueryTimeout(this.queryTimeout);
            } catch (SQLException e) {
                throw new CallException(e);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "StatementProps{" +
                "skip=" + skip +
                ", queryTimeout=" + queryTimeout +
                '}';
    }
}
