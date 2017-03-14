package testhelpers;

import com.microsoft.sqlserver.jdbc.SQLServerConnectionPoolDataSource;

import javax.sql.DataSource;

class SQLServerInfo implements DBInfo {

    public DataSource dataSource() {
        SQLServerConnectionPoolDataSource dataSource = new SQLServerConnectionPoolDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("sa");
        dataSource.setPortNumber(1434);
        dataSource.setPassword("yourStrong(!)Password");
        return dataSource;
    }

    public String installScript() {
        return "src/test/resources/sqlscripts/sqlserver/install.sql";
    }

    public String rollbackScript() {
        return "src/test/resources/sqlscripts/sqlserver/rollback.sql";
    }
}
