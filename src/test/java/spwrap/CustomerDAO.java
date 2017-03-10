package spwrap;

import spwrap.CustomMappers.CustomParamsMapper;
import spwrap.CustomMappers.CustomResultSetMapper;
import spwrap.CustomMappers.DateMapper;
import spwrap.CustomMappers.TableNamesMapper;
import spwrap.Customer.NotMappedCustomer;
import spwrap.annotations.*;

import java.sql.Date;
import java.util.List;

import static java.sql.Types.INTEGER;
import static java.sql.Types.VARCHAR;
import static spwrap.annotations.Props.*;
import static spwrap.annotations.Props.Connection;
import static spwrap.annotations.Props.TransactionIsolation.READ_COMMITTED;

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

    @AutoMapper
    @StoredProc("list_customers")
    List<AutoCustomer> listCustomers3();
	
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
	List<String> listTables2(); // Invalid will throw exception; @scalar supported with output parameters only

    @AutoMapper
    @StoredProc("list_tables")
    List<String> listTables3();

	// missing params annotations
	@StoredProc("create_customer0")
	void createCustomer7(@Param(VARCHAR) String firstName, String lastName);

    @Mapper(CustomMappers.CustomParamsMapperByName.class)
    @StoredProc("get_customer")
    Customer getCustomer7(@Param(INTEGER) Integer id);

    @Mapper(CustomMappers.CustomParamsMapperByInvalidName.class)
    @StoredProc("get_customer")
    Customer getCustomer8(@Param(INTEGER) Integer id);

    void methodWithNoAnnotation();

    @Props(connection = @Connection(readOnly = true))
    @Connection(readOnly = true)
    @StoredProc
    void testProps1();  // throws exception @Connection is already defined

    @Connection(transactionIsolation = READ_COMMITTED)
    @StoredProc("list_customers")
    List<Customer> testProps2();


    @ResultSet(maxRows = 3)
    @AutoMapper
    @StoredProc("list_tables")
    List<String> listFirst3Tables();
}
