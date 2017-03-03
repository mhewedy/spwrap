package spwrap.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import spwrap.CallException;

public class GenericDatabase implements Database {

    public static Database from(Connection connection) {
        try {
            if (connection.getMetaData().getDatabaseProductName().contains("HSQL")) {
                return new HSQL();
            } else {
                return new GenericDatabase();
            }
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    public boolean executeCall(CallableStatement call) throws SQLException {
        return call.execute();
    }

}
