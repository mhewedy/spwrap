package spwrap;

import static java.sql.Types.*;

import java.sql.Date;
import java.util.List;

import spwrap.CustomMappers.CustomParamsMapper;
import spwrap.CustomMappers.CustomResultSetMapper;
import spwrap.CustomMappers.DateMapper;
import spwrap.CustomMappers.GenericIdMapper;
import spwrap.CustomMappers.SingleStringMapper;
import spwrap.CustomMappers.TableNamesMapper;
import spwrap.annotations.Mapper;
import spwrap.annotations.Param;
import spwrap.annotations.StoredProc;

public interface CustomerDAO {

	@Mapper(GenericIdMapper.class)
	@StoredProc("create_customer")
	Integer createCustomer(@Param(VARCHAR) String firstName, @Param(VARCHAR) String lastName);

	@Mapper(CustomParamsMapper.class)
	@StoredProc("get_customer")
	Customer getCustomer1(@Param(INTEGER) Integer id);

	
	@StoredProc("get_customer")
	Customer getCustomer2(@Param(INTEGER) Integer id);

	
	@Mapper(CustomResultSetMapper.class)
	@StoredProc("list_customers")
	List<Customer> listCustomers1();
	
	
	@StoredProc("list_customers")
	List<Customer> listCustomers2();
	
	
	@Mapper(DateMapper.class)
	@StoredProc
	Tuple<Customer, Date> list_customers_with_date();
	
	
	@Mapper({CustomResultSetMapper.class, DateMapper.class})
	@StoredProc("list_customers_with_date")
	Tuple<Customer, Date> listCustomersWithDate();
	
	
	@Mapper(TableNamesMapper.class)
	@StoredProc("list_tables")
	List<String> listTables();
	
	
	@StoredProc("error_sp")
	void callStoredProcWithError();
	
	@Mapper(SingleStringMapper.class)
	@StoredProc("get_first_table_name_no_resultfields")
	String getFirstTableNameNoResultFields() ;

}
