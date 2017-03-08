package spwrap;

import java.sql.Connection;
import java.sql.SQLException;

import static spwrap.annotations.Props.*;
import static spwrap.proxy.Props.ConnectionProps;

class PropsOverrider {

    private static boolean isValid(int value) {
        return value != -1;
    }

    public ConnectionProps override(Connection connection, ConnectionProps props, boolean returnBackup) {

        ConnectionProps backup = null;

        if (returnBackup) {
            backup = new ConnectionProps();
            try {
                backup.readOnly = connection.isReadOnly();
                backup.transactionIsolation = TransactionIsolation.of(connection.getTransactionIsolation());
            } catch (SQLException e) {
                throw new CallException(e.getMessage());
            }
        }

        if (props != null){
            try {
                connection.setReadOnly(props.readOnly);
                int ti = props.transactionIsolation.getValue();
                if (isValid(ti)) {
                    connection.setTransactionIsolation(ti);
                }
            } catch (SQLException e) {
                throw new CallException(e.getMessage());
            }
        }

        return backup;
    }
}
