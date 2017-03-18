package spwrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract class ConnectionManager {

    private static final boolean FOUND_SPRING_DATA_SOURCE_UTILS = Util.isClassPresent("org.springframework.jdbc.datasource.DataSourceUtils");

    private static Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    static ConnectionManager newInstance() {

        ConnectionManager connectionManager =
                FOUND_SPRING_DATA_SOURCE_UTILS ? new SpringConnectionManager() : new DefaultConnectionManager();

        log.debug("using ConnectionManager: {}", connectionManager);
        return connectionManager;
    }

    Connection getConnection(DataSource dataSource, String jdbcUrl, String username, String password) throws SQLException {
        if (dataSource != null) {
            return getConnection(dataSource);
        } else if (jdbcUrl != null) {
            return DriverManager.getConnection(jdbcUrl, username, password);
        } else {
            throw new CallException("both dataSource and jdbcUrl are nulls");
        }
    }

    abstract Connection getConnection(DataSource dataSource) throws SQLException;

    abstract void closeConnection(Connection connection, DataSource dataSource);

    // ---------

    private static class DefaultConnectionManager extends ConnectionManager {

        public Connection getConnection(DataSource dataSource) throws SQLException {
            return dataSource.getConnection();
        }

        public void closeConnection(Connection connection, DataSource dataSource) {
            Util.closeDBObjects(connection, null, null);
        }
    }

    private static class SpringConnectionManager extends ConnectionManager {

        Connection getConnection(DataSource dataSource) {
            return DataSourceUtils.getConnection(dataSource);
        }

        void closeConnection(Connection connection, DataSource dataSource) {
            if (dataSource != null) {
                DataSourceUtils.releaseConnection(connection, dataSource);
            } else {
                Util.closeDBObjects(connection, null, null);
            }
        }
    }
}
