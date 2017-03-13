package spwrap;

import spwrap.db.DBInfo;
import spwrap.db.HSQLInfo;
import spwrap.db.MySQLInfo;
import spwrap.db.SQLServerInfo;

public enum TestDB {
    HSQL(new HSQLInfo()),
    MYSQL(new MySQLInfo()),
    SQLServer(new SQLServerInfo());

    DBInfo dbInfo;

    TestDB(DBInfo dbInfo) {
        this.dbInfo = dbInfo;
    }
}
