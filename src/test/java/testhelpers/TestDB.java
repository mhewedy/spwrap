package testhelpers;

public enum TestDB {
    HSQL(new HSQLInfo()),
    MYSQL(new MySQLInfo()),
    SQLServer(new SQLServerInfo());

    DBInfo dbInfo;

    TestDB(DBInfo dbInfo) {
        this.dbInfo = dbInfo;
    }
}
