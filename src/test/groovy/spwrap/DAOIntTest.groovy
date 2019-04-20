package spwrap

import spock.lang.Specification
import spock.lang.Unroll
import spwrap.proxy.DAOInvocationHandler
import testhelpers.TestUtils
import uk.org.lidalia.slf4jtest.TestLoggerFactory

import java.sql.SQLException

import static testhelpers.TestDB.*
import static uk.org.lidalia.slf4jext.Level.WARN

// integration test
@Unroll
class DAOIntTest extends Specification{

    CustomerDAO customerDao

    def _setup(db) {
        TestUtils.install(db)
        customerDao = new DAO.Builder(db.ref.dataSource()).build().create(CustomerDAO.class)
    }

    def _cleanup(db) {
        TestUtils.rollback(db)
    }

    def "#testDB : using the @Param to pass input parameters"(){
        given:
            _setup(testDB)
            def firstName = "Abdullah"
            def lastName = "Mohammad"
        when:
            customerDao.createCustomer0(firstName, lastName)
        then:
            noExceptionThrown()
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : using @Scalar annotation to map single output parameter"(){
        given:
            _setup(testDB)
            def firstName = "Abdullah"
            def lastName = "Mohammad"
        when:
            def custId = customerDao.createCustomer(firstName, lastName)
        then:
            custId == expectedCustId
        cleanup:
            _cleanup(testDB)
        where:
            testDB           | expectedCustId
            HSQL             | 0
            MYSQL            | 1
            SQLServer        | 1
            ORACLE           | 1
    }

    def "#testDB : using @Scalar annotation to map single output parameter - invoking the Stored Proc multiple times"(){
        given:
            _setup(testDB)
            def firstName = "Abdullah"
            def lastName = "Mohammad"
        when:
            def custId0 = customerDao.createCustomer(firstName, lastName)
            def custId1 = customerDao.createCustomer(firstName, lastName)
            def custId2 = customerDao.createCustomer(firstName, lastName)
            def custId3 = customerDao.createCustomer(firstName, lastName)
        then:
            custId0 == expectedCustId0
            custId1 == expectedCustId1
            custId2 == expectedCustId2
            custId3 == expectedCustId3
        cleanup:
            _cleanup(testDB)
        where:
            testDB    | expectedCustId0  | expectedCustId1 | expectedCustId2 | expectedCustId3
            HSQL      |      0           |       1         |      2          |       3
            MYSQL     |      1           |       2         |      3          |       4
            SQLServer |      1           |       2         |      3          |       4
            ORACLE    |      1           |       2         |      3          |       4
    }

    def "#testDB : using return type as a mapper for output parameters"(){
        given:
            _setup(testDB)
            def firstName = "Abdullah"
            def lastName = "Mohammad"
        when:
            customerDao.createCustomer0(firstName, lastName)
            def customer = customerDao.getCustomer2(custId);
        then:
            customer.firstName() == firstName
            customer.lastName() == lastName
        cleanup:
            _cleanup(testDB)
        where:
            testDB       | custId
            HSQL         | 0
            MYSQL        | 1
            SQLServer    | 1
            ORACLE       | 1
    }


    def "#testDB : override the return type Mapper using a custom mapping through the annotation @Mapper"(){
        given:
            _setup(testDB)
            def firstName = "Abdullah"
            def lastName = "Mohammad"
        when:
            customerDao.createCustomer0(firstName, lastName)
            def customer = customerDao.getCustomer1(custId);
        then:
            customer.firstName() == firstName + ".CustomParamsMapper"
            customer.lastName() == lastName + ".CustomParamsMapper"
        cleanup:
            _cleanup(testDB)
        where:
            testDB       | custId
            HSQL         | 0
            MYSQL        | 1
            SQLServer    | 1
            ORACLE       | 1
    }


    def "#testDB : Mappers (through return type -implicit- or @Mapper - explicit- ) will override @Scalar annotation"(){
        given:
            _setup(testDB)
            def firstName = "Farida"
            def lastName = "Mohammad"
        when:
            customerDao.createCustomer0(firstName, lastName)
            def customer = customerDao.getCustomer3(custId);
        then:
            customer.firstName() == firstName
            customer.lastName() == lastName
        cleanup:
            _cleanup(testDB)
        where:
            testDB       | custId
            HSQL         | 0
            MYSQL        | 1
            SQLServer    | 1
            ORACLE       | 1
    }


    def "#testDB : @Scalar annotation will cause CallException if no mappers, and the stored proc return more than 1 out params"(){
        given:
            _setup(testDB)
            def firstName = "Farida"
            def lastName = "Mohammad"
        when:
            customerDao.createCustomer0(firstName, lastName)
            customerDao.getCustomer4(custId);
        then:
            thrown(CallException)
        cleanup:
            _cleanup(testDB)
        where:
            testDB       | custId
            HSQL         | 0
            MYSQL        | 1
            SQLServer    | 1
            ORACLE       | 1
    }

    def "#testDB : only 2 explicit Mappers are allowed"(){
        given:
            _setup(testDB)
            def firstName = "Farida"
            def lastName = "Mohammad"
        when:
            customerDao.createCustomer0(firstName, lastName)
            customerDao.getCustomer5(custId);
        then:
            thrown(CallException)
        cleanup:
            _cleanup(testDB)
        where:
            testDB       | custId
            HSQL         | 0
            MYSQL        | 1
            SQLServer    | 1
            ORACLE       | 1
    }


    def "#testDB : only 2 explicit Mappers are allowed, one of each type (ResultSetMapper and TypedOutputParamMapper)"(){
        given:
            _setup(testDB)
            def firstName = "Farida"
            def lastName = "Mohammad"
        when:
            customerDao.createCustomer0(firstName, lastName)
            customerDao.getCustomer6(custId);
        then:
            thrown(CallException)
        cleanup:
            _cleanup(testDB)
        where:
            testDB       | custId
            HSQL         | 0
            MYSQL        | 1
            SQLServer    | 1
            ORACLE       | 1
    }

    def "#testDB : using implicit ResultSet Mapper to map result set"(){
        given:
            _setup(testDB)
            def firstName1 = "Farida"
            def lastName1 = "Mohammad"
            def firstName2 = "Abdullah"
            def lastName2 = "Mohammad"
        when:
            customerDao.createCustomer0(firstName1, lastName1)
            customerDao.createCustomer0(firstName2, lastName2)
            def list = customerDao.listCustomers2();
        then:
            2 == list.size()
            with(list.get(0)){
                firstName1 == firstName()
                lastName1 == lastName()
            }

            with(list.get(1)){
                firstName2 == firstName()
                lastName2 == lastName()
            }
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : using explicit ResultSet Mapper to map result set"(){
        given:
            _setup(testDB)
            def firstName1 = "Farida"
            def lastName1 = "Mohammad"
            def firstName2 = "Abdullah"
            def lastName2 = "Mohammad"
        when:
            customerDao.createCustomer0(firstName1, lastName1)
            customerDao.createCustomer0(firstName2, lastName2)
            def list = customerDao.listCustomers1();
        then:
            2 == list.size()
            with(list.get(0)){
                firstName1 == firstName()
                lastName1 == lastName()
            }

            with(list.get(1)){
                firstName2 == firstName()
                lastName2 == lastName()
            }
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : returning a Tuple as a result of stored proc return both result set and output parameters" (){
        given:
            _setup(testDB)
            def firstName1 = "Farida"
            def lastName1 = "Mohammad"
            def firstName2 = "Abdullah"
            def lastName2 = "Mohammad"
        when:
            customerDao.createCustomer0(firstName1, lastName1)
            customerDao.createCustomer0(firstName2, lastName2)
            def tuple = customerDao.listCustomersWithDate();
        then:
            tuple.object() != null

            2 == tuple.list().size()
            with(tuple.list().get(0)){
                firstName1 == firstName()
                lastName1 == lastName()
            }

            with(tuple.list().get(1)){
                firstName2 == firstName()
                lastName2 == lastName()
            }
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : StoredProc annotation will use method name as stored procedure name if no one is specified" (){
        given:
            _setup(testDB)
            def firstName1 = "Farida"
            def lastName1 = "Mohammad"
            def firstName2 = "Abdullah"
            def lastName2 = "Mohammad"
        when:
            customerDao.createCustomer0(firstName1, lastName1)
            customerDao.createCustomer0(firstName2, lastName2)
            def tuple = customerDao.list_customers_with_date();
        then:
            tuple.object() != null

            2 == tuple.list().size()
            with(tuple.list().get(0)){
                firstName1 == firstName()
                lastName1 == lastName()
            }

            with(tuple.list().get(1)){
                firstName2 == firstName()
                lastName2 == lastName()
            }
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : stored procedure will return error code from database" (){
        when:
            _setup(testDB)
            customerDao.callStoredProcWithError();
        then:
            def e = thrown(CallException)
            e.message.startsWith("Stored Procedure returns error code 1")
            e.errorCode == (short)1
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : choose to disable the feature of result code and message, only we care about business related parameters" (){
        given:
            _setup(testDB)
            def dao = new DAO.Builder(testDB.ref.dataSource()).config(new Config().useStatusFields(false)).build()
            def customerDao2 = dao.create(CustomerDAO.class)
        when:
            customerDao2.getFirstTableNameNoResultFields()
        then:
            noExceptionThrown()
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]

    }

    def "#testDB : we can even change the success value in the default result fields" (){
        given:
            _setup(testDB)
            def dao = new DAO.Builder(testDB.ref.dataSource()).config(new Config().successCode((short) 1)).build()
            def customerDao2 = dao.create(CustomerDAO.class);
        when:
            customerDao2.callStoredProcWithError()
        then:
            noExceptionThrown()
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : yet another test case for result set mapper" (){
        when:
            _setup(testDB)
            def listTables = customerDao.listTables()
        then:
            listTables.size() > 1
            listTables*.toLowerCase().contains("customers")
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : you can use @Scalar with Output parameters only mapper" (){
        when:
            _setup(testDB)
            customerDao.listTables2();
        then:
            thrown(CallException)
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : when stored procedure have missing @Param annotations on the parameters, IllegalArgumentException will be thrown" (){
        when:
            _setup(testDB)
            customerDao.createCustomer7("Farida", "Muhammad");
        then:
            thrown(IllegalArgumentException)
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : using Result.getXXX(String OutputParameterName)"(){
        given:
            _setup(testDB)
            def firstName = "Abdullah"
            def lastName = "Mohammad"
        when:
            customerDao.createCustomer0(firstName, lastName)
            def customer = customerDao.getCustomer7(custId);
        then:
            customer.firstName() == firstName
            customer.lastName() == lastName
        cleanup:
            _cleanup(testDB)
        where:
            testDB       | custId
            HSQL         | 0
            MYSQL        | 1
            SQLServer    | 1
            ORACLE       | 1
    }

    def "#testDB : using Result.getXXX(String InvalidOutputParameterName)"(){
        given:
            _setup(testDB)
            def firstName = "Abdullah"
            def lastName = "Mohammad"
        when:
            customerDao.createCustomer0(firstName, lastName)
            customerDao.getCustomer8(custId);
        then:
            thrown(CallException)
        cleanup:
            _cleanup(testDB)
        where:
            testDB       | custId
            HSQL         | 0
            MYSQL        | 1
            SQLServer    | 1
            ORACLE       | 1
    }

    def "#testDB : using @Scalar annotation to map single output parameter - connection using jdbc URL"(){
        given:
            TestUtils.install(testDB)
            def customerDAOJdbc = new DAO.Builder(jdbcUrl, username, password).build().create(CustomerDAO)
            def firstName = "Abdullah"
            def lastName = "Mohammad"
        when:
            def custId = customerDAOJdbc.createCustomer(firstName, lastName)
        then:
            custId == expectedCustId
        cleanup:
            _cleanup(testDB)
        where:
            testDB    | jdbcUrl                                | username    | password                 | expectedCustId
            HSQL      | "jdbc:hsqldb:mem:customers"            | "sa"        | ""                       | 0
            MYSQL     | "jdbc:mysql://localhost:3307/test"     | "root"      | ""                       | 1
            SQLServer | "jdbc:sqlserver://localhost:1434"      | "sa"        | "yourStrong(!)Password"  | 1
            ORACLE    | "jdbc:oracle:thin:@localhost:1522/xe"  | "system"    | "oracle"                 | 1
    }

    def "test methodWithNoAnnotation"(){
        given:
            def logger = TestLoggerFactory.getTestLogger(DAOInvocationHandler)
        when:
            new DAO.Builder(HSQL.ref.dataSource()).build().create(CustomerDAO).methodWithNoAnnotation();
        then:
            noExceptionThrown();
        then:
            with(logger.loggingEvents.get(logger.loggingEvents.size() -  1)){
                level == WARN
                message.contains("doesn't declare @StoredProc annotation, skipping.")
            }
        cleanup:
            TestLoggerFactory.clear()
    }

    def "#testDB : using optional-automappers to map result set"(){
        given:
            _setup(testDB)
            def firstName1 = "Farida"
            def lastName1 = "Mohammad"
            def firstName2 = "Abdullah"
            def lastName2 = "Mohammad"
        when:
            customerDao.createCustomer0(firstName1, lastName1)
            customerDao.createCustomer0(firstName2, lastName2)
            def list = customerDao.listCustomers3();
        then:
            2 == list.size()
            with(list.get(0)){
                firstName1 == getFirstname()
                lastName1 == getLastname()
            }

            with(list.get(1)){
                firstName2 == getFirstname()
                lastName2 == getLastname()
            }
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : using optional-automappers - yet another test case for result set mapper" (){
        when:
            _setup(testDB)
            def listTables = customerDao.listTables3()
        then:
            listTables.size() > 1
            listTables*.toLowerCase().contains("customers")
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : test @Props1" (){
        when:
            _setup(testDB)
            customerDao.testProps1();
        then:
            def ex = thrown(CallException)
            ex.message.contains('Connection')
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : test @Props2" (){
        when:
            _setup(testDB)
            customerDao.testProps2();
        then:
            noExceptionThrown();
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : Fetch only 3 rows" (){
        when:
            _setup(testDB)
            def listTables = customerDao.listFirst3Tables()
        then:
            listTables.size() == 3
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [MYSQL, SQLServer] // HSQL, ORACLE ignores the fetchSize
    }

    def "#testDB : test @Prop3" (){
        when:
            _setup(testDB)
            customerDao.testProps3();
        then:
            def ex = thrown(CallException)
            ex.message.contains('Statement')
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

    def "#testDB : test @Props4" (){
        when:
            _setup(testDB)
            customerDao.testProps4();
        then:
            def ex = thrown(CallException)
            ex.message.contains('ResultSet')
        cleanup:
            _cleanup(testDB)
        where:
            testDB << [HSQL, MYSQL, SQLServer, ORACLE]
    }

}
