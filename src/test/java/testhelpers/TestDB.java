package testhelpers;

public enum TestDB {
    HSQL(new HSQLInfo()),
    MYSQL(new MySQLInfo()),
    SQLServer(new SQLServerInfo()),
    ORACLE(new OracleInfo());

    public DBInfo ref;

    TestDB(DBInfo ref) {
        this.ref = ref;
    }
}
