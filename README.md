# spwrap
Stored Procedure caller 

Wrap Stored Procedures exeuction code.

compitable with jdk >= 1.6 (never tested with jdk 1.5, but I think it is compitable as well)


##Usage
 
 Add the following to your pom.xml:
 
 ```xml
 <repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```

and in the dependecies section add:
```xml
<dependency>
	<groupId>com.github.mhewedy</groupId>
	<artifactId>spwrap</artifactId>
	<version>0.0.6</version>
</dependency>
```

for gradle and tools see: https://jitpack.io/#mhewedy/spwrap/0.0.6

Note: All stored procedures should return 2 output parameters at least, result code and result message, that looks like:

```sql
CREATE PROCEDURE [dbo].[SOME_SP]
  @SUCCESS SMALLINT OUTPUT, 
  @MESSAGE varchar(50) OUTPUT
AS
-- .....
--- .....

SET @SUCCESS = 0 -- 0 means success, otherwise is false, if you want to override the success value, set the property spwarp.success_code to any short value
SET @MESSAGE = 'some output message'

GO
```
##Examples

```java
DataSource datasource = // .....
Caller caller = new Caller(datasource);

// call sp without input or output parameters
caller.call("STORED_RROC_WITH_NO_INPUT_OR_OUTPUT_PARAMETERS") // again, the result code and result message is not counted here, they should be added to any Stored Procedure

// call sp with input-only parameters
caller.call("STORED_RROC_WITH_INPUT_PARAMETERS", params(of("input", VARCHAR)));


// return output parameters
// this is Java 8 syntax, you can use anonymous inner class as well.
MyDataObject result = caller.call("STORED_RROC_WITH_OUTPUT_PARAMETERS",
	paramTypes(VARCHAR, VARCHAR, BIGINT), (call, index) -> {
		MyDataObject holder = new MyDataObject();
		holder.s1 = call.getString(index);
		holder.s2 = call.getString(index + 1);
		holder.l1 = call.getLong(index + 2);
		return holder;
	});


// can return output parameters and result set as well (and can also take input parameters)
Result<SPInfo, DateHolder> result = caller.call("OUTPUT_WITH_RS", null,
		paramTypes(VARCHAR, VARCHAR, BIGINT), (call, index) -> {
			DateHolder holder = new DateHolder();
			holder.s1 = call.getString(index);
			holder.s2 = call.getString(index + 1);
			holder.l1 = call.getLong(index + 2);
			return holder;
		}, SPInfo::new);

System.out.println(result.object()); // print the object that wraps output parameters
System.out.println(result.list());   // print the list of objects that wrap the result set 

```

##Not implemented features:
spwrap doesn't support INOUT parameters (yet!)

See test cases for more usage scenarios.
