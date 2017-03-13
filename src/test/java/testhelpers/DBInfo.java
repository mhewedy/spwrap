package testhelpers;

import javax.sql.DataSource;

interface DBInfo {

    /**
     * fully configured dataSource
     */
    DataSource dataSource();

    String installScript();

    String rollbackScript();
}
