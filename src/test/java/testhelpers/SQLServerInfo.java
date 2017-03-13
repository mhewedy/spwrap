package testhelpers;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;

class SQLServerInfo implements DBInfo {

    public DataSource dataSource() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setURL("jdbc:sqlserver://localhost:1433;DatabaseName=model");
        dataSource.setUser("sa");
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
