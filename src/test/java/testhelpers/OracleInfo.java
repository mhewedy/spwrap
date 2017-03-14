package testhelpers;

import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

class OracleInfo implements DBInfo {

    public DataSource dataSource() {
        OracleDataSource dataSource;
        try {
            dataSource = new OracleDataSource();
            dataSource.setURL("jdbc:oracle:thin:@127.0.0.1:1522/xe");
            dataSource.setUser("system");
            dataSource.setPassword("oracle");
            return dataSource;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String installScript() {
        return "src/test/resources/sqlscripts/oracle/install.sql";
    }

    public String rollbackScript() {
        return "src/test/resources/sqlscripts/oracle/rollback.sql";
    }
}
