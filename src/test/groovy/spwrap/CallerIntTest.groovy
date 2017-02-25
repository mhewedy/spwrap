package spwrap

import ch.vorburger.mariadb4j.DB
import ch.vorburger.mariadb4j.DBConfigurationBuilder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static java.sql.Types.INTEGER
import static java.sql.Types.VARCHAR
import static org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY
import static spwrap.Caller.Param.of
import static spwrap.Caller.paramTypes
import static spwrap.Caller.params

// integration test
@Unroll
class CallerIntTest extends Specification{

    @Shared def db;

	def caller
    @Shared def testDBs = [TestUtils.TestDB.HSQL, TestUtils.TestDB.MYSQL];

    def setupSpec() {
        def dbConfig = DBConfigurationBuilder.newBuilder()
        dbConfig.setPort(3306)
        db = DB.newEmbeddedDB(dbConfig.build())
        db.start();
    }

    def cleanupSpec() {
        db.stop()
    }

	def "#testDB : create customer using the caller interface"(){
		given:
            TestUtils.install(testDB)
            caller = new Caller(testDB.dbInterface.dataSource())

			def firstName = "Abdullah"
			def lastName = "Mohammad"
		when:
			def custId = caller.call("create_customer", params(of(firstName, VARCHAR), of(lastName, VARCHAR)),
				paramTypes(INTEGER), {it.getInt(1)});
		then:
			custId == custIdFromDb
        cleanup:
            TestUtils.rollback(testDB)
        where:
            testDB << testDBs
            custIdFromDb << [0, 1]
	}

	def "#testDB : create customer using the caller interface and Persistable"(){
		given:
            TestUtils.install(testDB)
            caller = new Caller(testDB.dbInterface.dataSource())

		    def customer  = new Customer2("Abdullah", "Mohammad")

		when:
		    def custId = caller.call("create_customer", customer.toInputParams(), paramTypes(INTEGER), {it.getInt(1)});

		then:
		    custId == custIdFromDb
        cleanup:
            TestUtils.rollback(testDB)
        where:
            testDB << testDBs
            custIdFromDb << [0, 1]
	}
}