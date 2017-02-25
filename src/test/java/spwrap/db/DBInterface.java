package spwrap.db;

import javax.sql.DataSource;

public interface DBInterface {

    /**
     * fully configured dataSource
     */
    DataSource dataSource();

    String installScript();

    String rollbackScript();
}
