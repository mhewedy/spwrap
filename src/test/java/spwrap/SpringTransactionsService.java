package spwrap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

public class SpringTransactionsService {

    private SupplierDAO supplierDAO;
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    public void setSupplierDAO(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }

    @Transactional
    public void insert2SuppliersTheJdbcTemplateFails() {
        supplierDAO.createSupplier("TEST");
        jdbcTemplate.execute("insert into supplier VALUES (DEFAULT, null)");
    }

    @Transactional
    public void insert2SuppliersTheStoredProcFails() {
        jdbcTemplate.execute("insert into supplier VALUES (DEFAULT, 'Abdullah')");
        supplierDAO.createSupplier(null);
    }

}
