package spwrap.db;

import javax.sql.DataSource;

public interface DBInfo {

    /**
     * fully configured dataSource
     */
    DataSource dataSource();

    String installScript();

    String rollbackScript();
}
