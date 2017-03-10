package spwrap.props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spwrap.CallException;

import java.sql.Connection;
import java.sql.SQLException;

import static spwrap.annotations.Props.TransactionIsolation;

public class ConnectionProps implements Props<Connection, Void> {

    private static Logger log = LoggerFactory.getLogger(ConnectionProps.class);

    private boolean skip = false;

    private boolean readOnly;
    private TransactionIsolation transactionIsolation;

    public ConnectionProps() {
        skip = true;
    }

    public ConnectionProps(boolean readOnly, TransactionIsolation transactionIsolation) {
        this.readOnly = readOnly;
        this.transactionIsolation = transactionIsolation;
    }

    public Void apply(Connection input, Object ... args) {
        if (!skip){
            log.debug("applying {} on input Connection", this);
            try {
                input.setReadOnly(this.readOnly);
                if (this.transactionIsolation != TransactionIsolation.DEFAULT){
                    input.setTransactionIsolation(this.transactionIsolation.getValue());
                }
            } catch (SQLException e) {
                throw new CallException(e);
            }
        }
        return null;
    }

    public static ConnectionProps from(Connection connection) {
        try {
            return new ConnectionProps(connection.isReadOnly(), TransactionIsolation.of(connection.getTransactionIsolation()));
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    @Override
    public String toString() {
        return "ConnectionProps{" +
                "skip=" + skip +
                ", readOnly=" + readOnly +
                ", transactionIsolation=" + transactionIsolation +
                '}';
    }
}
