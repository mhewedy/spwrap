package spwrap.db;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import javax.sql.DataSource;

public class MySQLInterface implements DBInterface {

    public DataSource dataSource() {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3307/test");
        dataSource.setUser("root");
        return dataSource;
    }

    public String installScript() {
        return "src/test/resources/mysql/install.sql";
    }

    public String rollbackScript() {
        return "src/test/resources/mysql/rollback.sql";
    }
}
