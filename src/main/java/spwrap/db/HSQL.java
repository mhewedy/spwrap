package spwrap.db;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class HSQL extends DefaultDatabase {

	@Override
	public boolean executeCall(CallableStatement call) throws SQLException {
		super.executeCall(call);
		return call.getMoreResults();
	}
}
