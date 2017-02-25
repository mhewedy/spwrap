package spwrap;

import spwrap.db.DBInterface;
import spwrap.db.HSQLInterface;
import spwrap.db.MySQLInterface;

public enum TestDB {
    HSQL(new HSQLInterface()),
    MYSQL(new MySQLInterface());

    DBInterface dbInterface;

    TestDB(DBInterface dbInterface) {
        this.dbInterface = dbInterface;
    }
}
