package spwrap.db;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface Database {

    boolean executeCall(CallableStatement call) throws SQLException;

}
