# spwrap
Stored Procedure caller 

Wrap Stored Procedures exeuction code.

compitable with jdk >= 1.6 (never tested with jdk 1.5, but I think it is compitable as well)


##Usage
 
 Add the following to your pom.xml:
 
 ```
 <repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```

and in the dependecies section add:
```
<dependency>
	<groupId>com.github.mhewedy</groupId>
	<artifactId>spwrap</artifactId>
	<version>0.0.5</version>
</dependency>
```

for gradle and others see: https://jitpack.io/#mhewedy/spwrap/0.0.3

Note: All stored procedures should return 2 output parameters at least, result code and result message, that looks like:
```
CREATE PROCEDURE [dbo].[SOME_SP]
  @SUCCESS bit OUTPUT, 
  @MESSAGE varchar(50) OUTPUT
AS
-- .....
--- .....

SET @SUCCESS = 'true' -- or 'false' in case of business-logic error (this will be reported as CallException)
SET @MESSAGE = 'some output message'

GO
```
##Examples

```
DataSource datasource = // .....
Caller caller = new Caller(datasource);

// call sp without input or output parameters
caller.call("STORED_RROC_WITH_NO_INPUT_OR_OUTPUT_PARAMETERS")

// call sp with input-only parameters
caller.call("STORED_RROC_WITH_INPUT_PARAMETERS", params(of("input", Types.VARCHAR)));


// return output parameters
// this is Java 8 syntax, you can use anonymous inner class as well.
DateHolder result = caller.call("STORED_RROC_WITH_OUTPUT_PARAMETERS",
	paramTypes(Types.VARCHAR, Types.VARCHAR, Types.BIGINT), (call, index) -> {
		DateHolder holder = new DateHolder();
		holder.s1 = call.getString(index);
		holder.s2 = call.getString(index + 1);
		holder.l1 = call.getLong(index + 2);
		return holder;
	});


// can return output parameters and result set as well (and can also take input paremters)
ListObject<SPInfo, DateHolder> result = caller.call("OUTPUT_WITH_RS", null,
		paramTypes(Types.VARCHAR, Types.VARCHAR, Types.BIGINT), (call, index) -> {
			DateHolder holder = new DateHolder();
			holder.s1 = call.getString(index);
			holder.s2 = call.getString(index + 1);
			holder.l1 = call.getLong(index + 2);
			return holder;
		}, SPInfo::new);


See test cases for more usage scenarios.

```
