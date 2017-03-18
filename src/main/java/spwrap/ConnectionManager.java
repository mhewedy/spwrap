package spwrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract class ConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    private static final boolean FOUND_SPRING_DATA_SOURCE_UTILS = Util.isClassPresent("org.springframework.jdbc.datasource.DataSourceUtils");

    private static final ConnectionManager DEFAULT_CONNECTION_MANAGER = new ConnectionManager() {

        public Connection getConnection(DataSource dataSource) throws SQLException {
            return dataSource.getConnection();
        }

        public void closeConnection(Connection connection, DataSource dataSource) {
            Util.closeDBObjects(connection, null, null);
        }
    };

    private static final ConnectionManager SPRING_CONNECTION_MANAGER = new ConnectionManager() {

        Connection getConnection(DataSource dataSource) {
            return DataSourceUtils.getConnection(dataSource);
        }

        void closeConnection(Connection connection, DataSource dataSource) {
            if (connection != null && dataSource != null) {
                DataSourceUtils.releaseConnection(connection, dataSource);
            } else {
                Util.closeDBObjects(connection, null, null);
            }
        }
    };

    private static /*final*/ ConnectionManager INSTANCE; // commented the final flag for testing!

    static {
        INSTANCE = FOUND_SPRING_DATA_SOURCE_UTILS ? SPRING_CONNECTION_MANAGER : DEFAULT_CONNECTION_MANAGER;
    }

    static ConnectionManager instance() {
        log.debug("using ConnectionManager: {}", INSTANCE.getClass());  // $1 for default, $2 for spring
        return INSTANCE;
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

    // ---------

    abstract Connection getConnection(DataSource dataSource) throws SQLException;

    abstract void closeConnection(Connection connection, DataSource dataSource);
}
