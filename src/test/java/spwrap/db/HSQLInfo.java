package spwrap.db;

import org.hsqldb.jdbc.JDBCPool;

import javax.sql.DataSource;

public class HSQLInfo implements DBInfo {

    public DataSource dataSource(){
        JDBCPool jdbcPool = new JDBCPool();
        jdbcPool.setUrl("jdbc:hsqldb:mem:customers");
        return jdbcPool;
    }

    public String installScript(){
        return "src/test/resources/hsql/install.sql";
    }

    public String rollbackScript(){
        return "src/test/resources/hsql/rollback.sql";
    }
}
