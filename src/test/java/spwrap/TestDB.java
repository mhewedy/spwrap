package spwrap;

import spwrap.db.DBInfo;
import spwrap.db.HSQLInfo;
import spwrap.db.MySQLInfo;

public enum TestDB {
    HSQL(new HSQLInfo()),
    MYSQL(new MySQLInfo());

    DBInfo dbInfo;

    TestDB(DBInfo dbInfo) {
        this.dbInfo = dbInfo;
    }
}
