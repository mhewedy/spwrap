package spwrap;

import spwrap.annotations.Param;
import spwrap.annotations.Scalar;
import spwrap.annotations.StoredProc;

import static java.sql.Types.INTEGER;
import static java.sql.Types.VARCHAR;

public interface SupplierDAO {

    @StoredProc("create_supplier")
    void createSupplier(@Param(VARCHAR) String firstName);

    @Scalar(INTEGER)
    @StoredProc("get_supplier_count")
    Integer getSupplierCount();
}
