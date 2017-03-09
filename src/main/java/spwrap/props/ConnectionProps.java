package spwrap.props;

import spwrap.CallException;

import java.sql.Connection;
import java.sql.SQLException;

import static spwrap.annotations.Props.TransactionIsolation;

public class ConnectionProps implements Props<Connection> {

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

    public Connection apply(Connection input) {
        if (skip){
            return input;
        }else{
            // TODO apply here before return
            return input;
        }
    }

    public static ConnectionProps from(Connection connection) {
        try {
            return new ConnectionProps(connection.isReadOnly(), TransactionIsolation.of(connection.getTransactionIsolation()));
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }
}
