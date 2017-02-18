# spwrap
Stored Procedure caller; simply execute stored procedure from java code.

compatible with `jdk` >= `1.5`, with only single dependency (`slf4j-api`)

## Step 0 (Create Stored Procedures):

Suppose you have 3 Stored Procedures to save customer to database, get customer by id and list all customer.

For example here's SP code using HSQL:
```sql
/* IN */
CREATE PROCEDURE create_customer(firstname VARCHAR(50), lastname VARCHAR(50), OUT custId INT, 
		OUT code SMALLINT, OUT msg VARCHAR(50))
   	MODIFIES SQL DATA DYNAMIC RESULT SETS 1
   	BEGIN ATOMIC
    	INSERT INTO CUSTOMERS VALUES (DEFAULT, firstname, lastname);
    	SET custId = IDENTITY();
    	SET code = 0 -- success;
	END
;;

/* IN, OUT */
CREATE PROCEDURE get_customer(IN custId INT, OUT firstname VARCHAR(50), OUT lastname VARCHAR(50), 
		OUT code SMALLINT, OUT msg VARCHAR(50)) 
	READS SQL DATA
	BEGIN ATOMIC
   		SELECT first_name, last_name INTO firstname, lastname FROM customers WHERE id = custId;
   		SET code = 0 -- success;
	END
;;

/* RS */
CREATE PROCEDURE list_customers(OUT code SMALLINT, OUT msg VARCHAR(50))
   	READS SQL DATA DYNAMIC RESULT SETS 1
   	BEGIN ATOMIC
    	DECLARE result CURSOR FOR SELECT * FROM CUSTOMERS;
     	OPEN result;
     	SET code = 0 -- success;
  	END
;;
```

>**NOTE**: Every Stored Procedure by default need to have 2 additional Output Parameters at the end of its parameter list. One of type `SMALLINT` and the other of type `VARCHAR` for result code and message respectively, where result code `0` means success. You can override the `0` value or remove this default behviour at all, [see the configuration wiki page](https://github.com/mhewedy/spwrap/wiki/spwrap-configurations).

>**NOTE**: When the Stored procedure have input and output parameters, input parameters should come first and then the output parameters and then the 2 additional output parameters of the status code and message.

## Step 1 (Create The Domain Object):
Here's the Java Domain class:

```java
public class Customer {

	private Integer id;
	private String firstName, lastName;

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
}
```

## Step 2 (Create The DAO interface):

Now you Need to create the DAO **interface**:
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

## Step 3 (Create Mappings):

Before start using the `CustomerDAO` interface, one last step is required, to *map* the result of the `get_customer` and `list_customers` stored procedures.

* `get_customer` stored procs returns the result as Output Parameters, so you need to have a class to implement `TypedOutputParamMapper` interface.
* `list_customers` stored proc returns the result as Result Set, so you need to have a class to implement `ResultSetMapper` interface.

Let's change Our customer class to implement both interfaces (for `getCustomer` and `listCustomers`):

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
	
	@Override
	public Customer map(Result<?> result) {
		if (result.isResultSet()) {// for ResultSetMapper
			return new Customer(result.getInt(1), result.getString(2), result.getString(3));
		} else { // for TypedOutputParamMapper
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
>**NOTE**: If your stored procedure returns a single **output parameter** with no result set, then you can use the `@Scalar` annotation and you will not need to provide a Mapper class yourself, the mapping will done for you. [see wiki page about scalars for more](https://github.com/mhewedy/spwrap/wiki/Scalar)

## Step 4 (Lets use it):

Now you can start using the interface to call the stored procedures:
```java
DAO dao = new DAO.Builder(dataSource).build();
CustomerDAO customerDao = dao.create(CustomerDAO.class);

customerDao.createCustomer("Abdullah", "Muhammad");
Customer abdullah = customerDao.getCustomer(0);
// ......
```
For full example and more, see Test cases and [wiki](https://github.com/mhewedy/spwrap/wiki).

## installation
 ```xml
 <repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
And in the dependecies section add:
```xml
<dependency>
	<groupId>com.github.mhewedy</groupId>
	<artifactId>spwrap</artifactId>
	<version>0.0.9</version>
</dependency>
```

for gradle and other tools see: https://jitpack.io/#mhewedy/spwrap/0.0.9

## More about Mapping:
[Read more about Mappers in the wiki](https://github.com/mhewedy/spwrap/wiki/Mappers)

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

* Your Stored procedure can return output parameter as well as 1 Result set in one call, to achieve this use `Tuple` return type:
```java
	@Mapper({CustomResultSetMapper.class, DateMapper.class})
	@StoredProc("list_customers_with_date")
	Tuple<Customer, Date> listCustomersWithDate();
```
[Read more about Mappers in the wiki](https://github.com/mhewedy/spwrap/wiki/Mappers)

##Limitations:
spwrap doesn't support INOUT parameters (yet!) (I don't need them so I didn't implement it, If you need it, [just open an issue for it](https://github.com/mhewedy/spwrap/issues/new))

spwrap doesn't support returning multi-result sets from the stored procedure.

Tested on MySQL, SQL Server and HSQL

##Qestions:
Q: Can `spwrap` do auto map for me for all fields?    
A: `spwrap` will not map the result from database into java for your, you have to deal with it via Mappers (`TypedOutputParamMapper` and `ResultSetMapper`). I did so because I intented to make this simple library as simple as possible, without going into SQL-to-Java types mapping details.

See [wiki page] (https://github.com/mhewedy/spwrap/wiki) for more info and test cases for more usage scenarios.
