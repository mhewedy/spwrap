package spwrap.props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spwrap.CallException;

import java.sql.Connection;
import java.sql.SQLException;

import static spwrap.annotations.Props.Isolation;

public class ConnectionProps implements Props<Connection, Boolean> {

    private static Logger log = LoggerFactory.getLogger(ConnectionProps.class);

    private boolean skip = false;

    private boolean readOnly;
    private Isolation isolation;

    public ConnectionProps() {
        skip = true;
    }

    public ConnectionProps(boolean readOnly, Isolation isolation) {
        this.readOnly = readOnly;
        this.isolation = isolation;
    }

    public Boolean apply(Connection input, Object ... args) {
        if (!skip){
            log.debug("applying {} on input Connection", this);
            try {
                input.setReadOnly(this.readOnly);
                if (this.isolation != Isolation.DEFAULT){
                    input.setTransactionIsolation(this.isolation.getValue());
                }
            } catch (SQLException e) {
                throw new CallException(e);
            }
            return true;
        }
        return false;
    }

    public static ConnectionProps from(Connection connection) {
        try {
            return new ConnectionProps(connection.isReadOnly(), Isolation.of(connection.getTransactionIsolation()));
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public String toString() {
        return "ConnectionProps{" +
                "skip=" + skip +
                ", readOnly=" + readOnly +
                ", isolation=" + isolation +
                '}';
    }
}
