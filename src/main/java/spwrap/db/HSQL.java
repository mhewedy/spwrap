package spwrap.db;

import java.sql.CallableStatement;
import java.sql.SQLException;

class HSQL extends GenericDatabase {

    @Override
    public boolean executeCall(CallableStatement call) throws SQLException {
        super.executeCall(call);
        return call.getMoreResults();
    }
}
