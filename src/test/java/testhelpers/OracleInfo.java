package testhelpers;

import javax.sql.DataSource;

class OracleInfo implements DBInfo {

    public DataSource dataSource() {
//        SQLServerConnectionPoolDataSource dataSource = new SQLServerConnectionPoolDataSource();
//        dataSource.setURL("jdbc:oracle:thin:@localhost:1521/xe");
//        dataSource.setUser("system");
//        dataSource.setPassword("oracle");
//        return dataSource;
        return null;
    }

    public String installScript() {
        return "src/test/resources/sqlscripts/oracle/install.sql";
    }

    public String rollbackScript() {
        return "src/test/resources/sqlscripts/oracle/rollback.sql";
    }
}
