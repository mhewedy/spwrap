# spwrap
Stored Procedure caller; simply execute stored procedure from java code.

compitable with `jdk` >= `1.6` (never tested with jdk 1.5), with 1 dependency on `slf4j-api`

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

	@Mapper(GenericIdMapper.class)
	@StoredProc("new_customer")
	Integer createCustomer(@Param(VARCHAR) String firstName, @Param(VARCHAR) String lastName);

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

Let's change Our customer class to implement both interfaces:

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

	//TypedOutputParamMapper
	@Override
	public Customer map(Result result, int index) {
		this.firstName = result.getString(index);
		this.lastName = result.getString(index + 1);
		return this;
	}

	//TypedOutputParamMapper
	@Override
	public List<Integer> getTypes() {
		return Arrays.asList(VARCHAR, VARCHAR);
	}

	//ResultSetMapper
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

And let's create GenericIdMapper:

```java
public static class GenericIdMapper implements TypedOutputParamMapper<Integer> {

	@Override
	public Integer map(Result result, int index) {
		return result.getInt(index);
	}

	@Override
	public List<Integer> getTypes() {
		return Arrays.asList(INTEGER);
	}
};
```


Now you can start using the interface to call the stored procedures:
```java
CustomerDAO customerDao = new Caller(dataSource).create(CustomerDAO.class);

customerDao.createCustomer("Abdullah", "Muhammad");
Customer abdullah = customerDao.getCustomer(0);
// ......
```

For full example and more, see Test cases.

## About Mapping strategies:
`spwrap` provides 2 mapping strategies:
 - The domain Object - that is used as a return type to the DAO method - to implement either `ResultSetMapper` or `TypedOutputParamMapper` or both.
 - Create a custom class that implement either interfaces and annotate the DAO method with `@Mapper(MyCustomMappingClass.clss)`.
 
*So, which strategy to follow?*

The answer is depends on you use case, if the Stored procedure returns the whole object (like in `getCustomer` and `listCustomers` method in the example above), then choose the first strategy by making your domain object impelements the mapping interfaces.

But if the Stored procedure return some output that is not domain-object related (like returning the newly created customer Id, in the example above of `createCustomer` method, then custom mapping strategy is better fit here.

**NOTE**: Custom mapping strategies could be reused across your domain objects, actually it is better to have a set of generic mapping strategies and use them across your system (like the `GenericIdMapper`).

**NOTE**: Mapping classes specified in `@Mapper` annotation overrides the mapping of the return object, in other words if your DAO method returns an object which implements a Mapping interface (either `ResultSetMapper` or `TypedOutputParamMapper`) and the method also annotated with `@Mapper` that points to an object implemeting a Mapping interface, then the mapping in the object specified in`@Mapper` will take precedence.

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

##Limitations:
spwrap doesn't support INOUT parameters (yet!) (I don't need them so I didn't implement it, If you need it, [just open an issue for it](https://github.com/mhewedy/spwrap/issues/new))

spwrap doesn't support returning multi-result sets from the stored procedure.

Tested on MySQL, SQL Server and HSQL

**NOTE**: `spwrap` will not map the result from database into java for your, you have to deal with it via Mappers (`TypedOutputParamMapper` and `ResultSetMapper`). I did so because I intented to make this simple library as simple as possible, without going into SQL-to-Java types mapping details.

See test cases for more usage scenarios.
