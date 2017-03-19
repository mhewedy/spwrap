# spwrap
Stored Procedure caller; simply execute stored procedure from java code.    
*Compatible with `jdk` >= `1.5`, with only single dependency (`slf4j-api`)*

[![Build Status](https://travis-ci.org/mhewedy/spwrap.svg?branch=master)](https://travis-ci.org/mhewedy/spwrap)
[![Coverage Status](https://codecov.io/github/mhewedy/spwrap/coverage.svg?branch=master)](https://codecov.io/github/mhewedy/spwrap?branch=master)

## Step 0: Create Stored Procedures:

Suppose you have 3 Stored Procedures to save customer to database, get customer by id and list all customer.

For example here's SP code using HSQL:
```sql
CREATE PROCEDURE create_customer(firstname VARCHAR(50), lastname VARCHAR(50), OUT custId INT, 
        OUT code SMALLINT, OUT msg VARCHAR(50))
    MODIFIES SQL DATA DYNAMIC RESULT SETS 1
    BEGIN ATOMIC
        INSERT INTO CUSTOMERS VALUES (DEFAULT, firstname, lastname);
        SET custId = IDENTITY();
        SET code = 0 -- success;
    END

CREATE PROCEDURE get_customer(IN custId INT, OUT firstname VARCHAR(50), OUT lastname VARCHAR(50), 
        OUT code SMALLINT, OUT msg VARCHAR(50)) 
    READS SQL DATA
    BEGIN ATOMIC
        SELECT first_name, last_name INTO firstname, lastname FROM customers WHERE id = custId;
        SET code = 0 -- success;
    END

CREATE PROCEDURE list_customers(OUT code SMALLINT, OUT msg VARCHAR(50))
    READS SQL DATA DYNAMIC RESULT SETS 1
    BEGIN ATOMIC
        DECLARE result CURSOR FOR SELECT id, first_name firstname, last_name lastname FROM CUSTOMERS;
        OPEN result;
        SET code = 0 -- success;
    END
```
>**NOTE**: Every Stored Procedure by default need to have 2 additional Output Parameters at the end of its parameter list. One of type `SMALLINT` and the other of type `VARCHAR` for result code and message respectively, where result code `0` means success. You can override the `0` value or remove this default behviour at all, [see the configuration wiki page](https://github.com/mhewedy/spwrap/wiki/Configurations).

## Step 1: Create The DAO interface:
```java
public interface CustomerDAO {

    @StoredProc("create_customer")
    void createCustomer(@Param(VARCHAR) String firstName, @Param(VARCHAR) String lastName);

    @StoredProc("get_customer")
    Customer getCustomer(@Param(INTEGER) Integer id);	
	
    @StoredProc("list_customers")
    List<Customer> listCustomers();
}
```

## Step 2: Map Output parameters and Result set (if any):

Before start using the `CustomerDAO` interface, one last step is required, to *map* the result of the `get_customer` and `list_customers` stored procedures.

* `get_customer` stored procs returns the result as Output Parameters, so you need to have a class to implement `TypedOutputParamMapper` interface.
* `list_customers` stored proc returns the result as Result Set, so you need to have a class to implement `ResultSetMapper` interface.

Let's create Customer class to implement both interfaces (for `getCustomer` and `listCustomers`):

```java
public class Customer implements TypedOutputParamMapper<Customer>, ResultSetMapper<Customer> {

	private Integer id;
	private String firstName, lastName;

	public Customer() {
	}

	public Customer(Integer id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Integer id() {
		return id;
	}

	public String firstName() {
		return firstName;
	}

	public String lastName() {
		return lastName;
	}
	
	 //You can acess result set columns/output parameters by name as well
	@Override
	public Customer map(Result<?> result) {
		if (result.isResultSet()) {
			return new Customer(result.getInt(1), result.getString(2), result.getString(3));
		} else {
			return new Customer(null, result.getString(1), result.getString(2));
		}
	}

	// for TypedOutputParamMapper
	@Override
	public List<Integer> getTypes() {
		return Arrays.asList(VARCHAR, VARCHAR);
	}
}
```
See more examples on [spwrap-examples](https://github.com/mhewedy/spwrap-examples) github project and read more about [Mappers](https://github.com/mhewedy/spwrap/wiki/Mappers) in the wiki.

>**NOTE**: If your stored procedure returns a single **output parameter** with no result set, then you can use the `@Scalar` annotation and you will not need to provide a Mapper class yourself, the mapping will done for you. [see wiki page about scalars for more](https://github.com/mhewedy/spwrap/wiki/Scalar)

>**NOTE**: You can use [`@AutoMapper`s](https://github.com/mhewedy/spwrap/wiki/AutoMappers) to do the mapping for you instead of Mapping the Result object into your domain object yourself.

## Step 3: Using the DAO interface:

Now you can start using the interface to call the stored procedures:
```java
DataSource dataSource = ...
DAO dao = new DAO.Builder(dataSource).build();
CustomerDAO customerDao = dao.create(CustomerDAO.class);

customerDao.createCustomer("Abdullah", "Muhammad");
Customer customer = customerDao.getCustomer1(1);
Assert.assertEquals("Abdullah", customer.firstName());
```
## installation
Gradle:
```gradle
compile group: 'com.github.mhewedy', name: 'spwrap', version: '0.0.19'
```
Maven:
```xml
<dependency>
    <groupId>com.github.mhewedy</groupId>
    <artifactId>spwrap</artifactId>
    <version>0.0.19</version>
</dependency>
```
## Additional staff:

* If you don't supply the stored procedure name to `@StoredProc`, it will use the method name by default.

* `@Param` annotation should used for *ALL* method parameters and accepts the SQL Type per `java.sql.Types`.

* If you don't want to tie your Domain Object with `spwrap` as of step 3 above, you can have another class to implement the Mapper interfaces (`TypedOutputParamMapper` and `ResultSetMapper`) and pass it to the annotaion `@Mapper` like:
```java
@Mapper(CustomResultSetMapper.class)
@StoredProc("list_customers")
List<Customer> listCustomers();
```
* `@Mapper` annotation overrides the mapping specified by the return type object, i.e. `spwrap` extract Mapping infromation from the return type class, and then override it with the classes set by `@Mapper` annotation if found.

* Your Stored procedure can return output parameter as well as *One* Result set in one call, to achieve this use `Tuple` return type:
```java
@Mapper({MyResultSetMapper.class, MyOutputParameterMapper.class})
@StoredProc("list_customers_with_date")
Tuple<Customer, Date> listCustomersWithDate();
```
## Limitations:
* spwrap doesn't support INOUT parameters.

* spwrap doesn't support returning multi-result sets from the stored procedure.

* When the Stored procedure have input and output parameters, input parameters should come first and then the output parameters.

## Database Support:
Because `spwrap` is based on JDBC API, theoretically it should support any Database Management System with a JDBC Driver, *However* it is [tested](https://travis-ci.org/mhewedy/spwrap) on **HSQL**, **MySQL**, **SQL Server** and **Oracle** with jdk **1.6**~~, **1.7** and **1.8**~~ (1.7 and 1.8 are remove to reduce build time). (Plan to test againest: `Postgresql`, `Sybase`, `DB2`)

See [wiki page](https://github.com/mhewedy/spwrap/wiki) for more info and test cases/[spwrap-examples](https://github.com/mhewedy/spwrap-examples) for more usage scenarios.
