package spwrap;

import spwrap.CallException;

import java.sql.Connection;
import java.sql.SQLException;

import static spwrap.proxy.Props.ConnectionProps;

// TODO continue work on this method, may be move it out
// To be used by Caller to apply Props on the JDBC Objects
class PropsOverrider {

    public ConnectionProps override(Connection connection, ConnectionProps props){

        if (props == null){
            return null;
        }

        ConnectionProps backup = new ConnectionProps();
        try {
            // backup
            backup.holdability = spwrap.annotations.Props.ResultSetHoldability.of(connection.getHoldability());
            backup.readOnly = connection.isReadOnly();
            backup.transactionIsolation = spwrap.annotations.Props.TransactionIsolation.of(connection.getTransactionIsolation());
            backup.resultSetHoldability = spwrap.annotations.Props.ResultSetHoldability.of(connection.getMetaData().getResultSetHoldability());

            // override
            if (isValid(props.holdability.getValue())) {
                connection.setHoldability(props.holdability.getValue());
            }
            connection.setReadOnly(props.readOnly);
            if (isValid(props.transactionIsolation.getValue())){
                connection.setTransactionIsolation(props.transactionIsolation.getValue());
            }
        } catch (SQLException e) {
            throw new CallException(e.getMessage());
        }
        return backup;
    }

    private static boolean isValid(int value){
        return value != -1;
    }
}
