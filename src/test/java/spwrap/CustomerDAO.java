package spwrap;

import static java.sql.Types.*;

import java.sql.Date;
import java.util.List;

import spwrap.CustomMappers.CustomParamsMapper;
import spwrap.CustomMappers.CustomResultSetMapper;
import spwrap.CustomMappers.DateMapper;
import spwrap.CustomMappers.TableNamesMapper;
import spwrap.Customer.NotMappedCustomer;
import spwrap.annotations.Mapper;
import spwrap.annotations.Param;
import spwrap.annotations.Scalar;
import spwrap.annotations.StoredProc;

public interface CustomerDAO {

	@StoredProc("create_customer0")
	void createCustomer0(@Param(VARCHAR) String firstName, @Param(VARCHAR) String lastName);

	@Scalar(INTEGER)
	@StoredProc("create_customer")
	Integer createCustomer(@Param(VARCHAR) String firstName, @Param(VARCHAR) String lastName);

	@StoredProc("get_customer")
	Customer getCustomer2(@Param(INTEGER) Integer id);

	@Mapper(CustomParamsMapper.class)
	@StoredProc("get_customer")
	Customer getCustomer1(@Param(INTEGER) Integer id);

	@Scalar(VARCHAR) // ignored see test cases
	@StoredProc("get_customer")
	Customer getCustomer3(@Param(INTEGER) Integer id);

	@Scalar(VARCHAR)
	@StoredProc("get_customer")
	NotMappedCustomer getCustomer4(@Param(INTEGER) Integer id);

	
	@Mapper({ CustomParamsMapper.class, DateMapper.class, TableNamesMapper.class })
	@StoredProc("get_customer")
	Customer getCustomer5(@Param(INTEGER) Integer id);

	@Mapper({ CustomParamsMapper.class, DateMapper.class })
	@StoredProc("get_customer")
	Customer getCustomer6(@Param(INTEGER) Integer id);
	

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
	
	
	@StoredProc("error_sp")
	void callStoredProcWithError();
	
	@Scalar(VARCHAR)
	@StoredProc("get_first_table_name_no_resultfields")
	String getFirstTableNameNoResultFields() ;
	
	@Mapper(TableNamesMapper.class)
	@StoredProc("list_tables")
	List<String> listTables();
	
	@Scalar(VARCHAR)
	@StoredProc("list_tables")
	List<String> listTables2();

	// missing params annotations
	@StoredProc("create_customer0")
	void createCustomer7(@Param(VARCHAR) String firstName, String lastName);
}
