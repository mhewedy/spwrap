package spwrap

import ch.vorburger.mariadb4j.DB
import ch.vorburger.mariadb4j.DBConfigurationBuilder
import testhelpers.TestDB
import testhelpers.TestUtils
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static java.sql.Types.INTEGER
import static java.sql.Types.VARCHAR
import static spwrap.Caller.Param.of
import static spwrap.Caller.paramTypes
import static spwrap.Caller.params

// integration test
@Unroll
class CallerIntTest extends Specification{

    @Shared def mysqlDbRef;

    Caller caller

    def setupSpec() {
        def dbConfig = DBConfigurationBuilder.newBuilder()
        dbConfig.setPort(3307)
        mysqlDbRef = DB.newEmbeddedDB(dbConfig.build())
        mysqlDbRef.start();
    }

    def _setup(db){
        TestUtils.install(db)
        caller = new Caller(db.dbInfo.dataSource())
    }

    def _cleanup(db){
        TestUtils.rollback(db)
    }

    def cleanupSpec() {
        mysqlDbRef.stop()
    }

	def "#testDB : create customer using the caller interface"(){
		setup:
            _setup(testDB)
			def firstName = "Abdullah"
			def lastName = "Mohammad"
		when:
			def custId = caller.call("create_customer", params(of(firstName, VARCHAR), of(lastName, VARCHAR)),
				paramTypes(INTEGER), {it.getInt(1)});
		then:
			custId == expectedCustId
        cleanup:
            _cleanup(testDB)
        where:
            testDB       | expectedCustId
            TestDB.HSQL  | 0
            TestDB.MYSQL | 1
	}

	def "#testDB : create customer using the caller interface and Persistable"(){
		given:
            _setup(testDB)
		    def customer  = new Customer2("Abdullah", "Mohammad")
		when:
		    def custId = caller.call("create_customer", customer.toInputParams(), paramTypes(INTEGER), {it.getInt(1)});

		then:
		    custId == expectedCustId
        cleanup:
            _cleanup(testDB)
        where:
            testDB       | expectedCustId
            TestDB.HSQL  | 0
            TestDB.MYSQL | 1
	}
}