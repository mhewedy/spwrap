# spwrap
Stored Procedure caller 

Wrap Stored Procedures exeuction code.

compitable with jdk >= 1.6 (never tested with jdk 1.5, but I think it is compitable as well)


## Usage
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
	<version>0.0.8</version>
</dependency>
```

for gradle and other tools see: https://jitpack.io/#mhewedy/spwrap/0.0.8

## Step 0 (Create Store Procedures):

Suppose you have 3 Stored Procedures to save customer to database, get customer by id and list all customer.

For example here's SP code using HSQL:
```sql
/* IN */
CREATE PROCEDURE new_customer(firstname VARCHAR(50), lastname VARCHAR(50), OUT code SMALLINT, OUT msg VARCHAR(50))
   	MODIFIES SQL DATA DYNAMIC RESULT SETS 1
   	BEGIN ATOMIC
    	INSERT INTO CUSTOMERS VALUES (DEFAULT, firstname, lastname);
    	SET code = 0;
	END
;;

/* IN, OUT */
CREATE PROCEDURE get_customer(IN pid INT, OUT firstname VARCHAR(50), OUT lastname VARCHAR(50), OUT code SMALLINT, OUT msg VARCHAR(50)) 
	READS SQL DATA
	BEGIN ATOMIC
   		SELECT first_name, last_name INTO firstname, lastname FROM customers WHERE id = pid;
   		SET code = 0;
	END
;;

/* RS */
CREATE PROCEDURE list_customers(OUT code SMALLINT, OUT msg VARCHAR(50))
   	READS SQL DATA DYNAMIC RESULT SETS 1
   	BEGIN ATOMIC
    	DECLARE result CURSOR FOR SELECT * FROM CUSTOMERS;
     	OPEN result;
     	SET code = 0;
  	END
;;
```

**Note**: Every Stored Procedure should have additional 2 Output Parameters at the end of its parameter list. One of type SMALLINT and the other of type VARCHAR for result code and message respectively, where result code `0` means success, and fail otherwise.

**Note**: You can override the result code default success value setting system property `spwarp.success_code` to any `short` value.

Note: All stored procedures should return 2 output parameters at least, result code and result message, that looks like:

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

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
}
```

## Step 2 (Create The DAO interface):

Now you Need to create the DAO **interface**:
```java
public interface CustomerDAO {

	@StoredProc("new_customer")
	void createCustomer(@Param(VARCHAR) String firstName, @Param(VARCHAR) String lastName);

	@StoredProc("get_customer")
	Customer getCustomer(@Param(INTEGER) Integer id);	
	
	@StoredProc("list_customers")
	List<Customer> listCustomers();
}
```

## Step 3 (Create Mappings):

Before start using the `CustomerDAO` interface, one last step is required, to *map* the result of the `get_customer` and `list_customers` stored procedures.

* `get_customer` stored proc returns the result as Output Parameters, so you need to have a class to implement `TypedOutputParamMapper` interface.
* `list_customers` stored proc returns the result as Result Set, so you need to have a class to implement `ResultSetMapper` interface.

Let's change Out customer class to implement both interfaces:

```java
public class Customer implements TypedOutputParamMapper<Customer>, ResultSetMapper<Customer> {

	private Integer id;
	private String firstName, lastName;

	// mandatory when implementing TypedOutputParamMapper or ResultSetMapper
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
	public Customer map(Result result, int index) {
		this.firstName = result.getString(index);
		this.lastName = result.getString(index + 1);
		return this;
	}

	@Override
	public List<Integer> getTypes() {
		return Arrays.asList(VARCHAR, VARCHAR);
	}

	@Override
	public Customer map(Result result) {
	        // When you impelement ResultSetMapper, you need always to return a new Object
		return new Customer(result.getInt(1), result.getString(2), result.getString(3));
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
}
```


Now you can start using the interface to call the stored procedures:
```java
CustomerDAO customerDao = new Caller(dataSource).create(CustomerDAO.class);

customerDao.createCustomer("Abdullah", "Muhammad");
Customer abdullah = customerDao.getCustomer1(0);
// ......
```

For full example and more, see Test cases.

##




##Limitations:
spwrap doesn't support INOUT parameters (yet!) (I don't need them so I didn't implement it, If you need it, [just open an issue for it](https://github.com/mhewedy/spwrap/issues/new))

spwrap doesn't support returning multi-result sets from the stored procedure.

See test cases for more usage scenarios.
