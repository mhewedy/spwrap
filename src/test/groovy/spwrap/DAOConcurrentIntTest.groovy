package spwrap

import ch.vorburger.mariadb4j.DB
import ch.vorburger.mariadb4j.DBConfigurationBuilder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import spwrap.proxy.DAOInvocationHandler
import uk.org.lidalia.slf4jtest.TestLoggerFactory

import java.sql.SQLException
import java.util.concurrent.Callable
import java.util.concurrent.Executors

import static uk.org.lidalia.slf4jext.Level.WARN

// integration test
@Unroll
class DAOConcurrentIntTest extends Specification{

    @Shared def mysqlDbRef;

	CustomerDAO customerDao

    def setupSpec() {
        def dbConfig = DBConfigurationBuilder.newBuilder()
        dbConfig.setPort(3307)
        mysqlDbRef = DB.newEmbeddedDB(dbConfig.build())
        mysqlDbRef.start();
    }

    def _setup(db) {
        TestUtils.install(db)
        customerDao = new DAO.Builder(db.dbInfo.dataSource()).build().create(CustomerDAO.class)
    }

    def cleanupSpec() {
        mysqlDbRef.stop()
    }

	def _cleanup(db) {
		TestUtils.rollback(db)
	}

    def "#testDB : Multithreading: multipe calls from different threads doesn't intersect \
                and returned objects are unique per each thread "(){
        given:
        _setup(testDB)
            def firstName = "Farida"
            def lastName = "Mohammad"
            def pool = Executors.newFixedThreadPool(5)
        when:

            def futureVoidList = pool.invokeAll(Arrays.asList(
                    getCallableForCreateCustomer(customerDao, firstName, lastName),
                    getCallableForCreateCustomer(customerDao, firstName, lastName),
                    getCallableForCreateCustomer(customerDao, firstName, lastName),
                    getCallableForCreateCustomer(customerDao, firstName, lastName),
                    getCallableForCreateCustomer(customerDao, firstName, lastName),
                    getCallableForCreateCustomer(customerDao, firstName, lastName),
                    getCallableForCreateCustomer(customerDao, firstName, lastName),
                    getCallableForCreateCustomer(customerDao, firstName, lastName)
            ));

            futureVoidList.collect { it.get() } // join

            def futureCustomerList = pool.invokeAll(Arrays.asList(
                    getCallableForListCustomers(customerDao),
                    getCallableForListCustomers(customerDao),
                    getCallableForListCustomers(customerDao)
            ));

            def customersLists = futureCustomerList.collect{it.get()} // join

        then:
            customersLists.each {
                it.collect {it.id()}.sort().max() == maxId
            }

            customersLists.collect { it.get(0) }.unique().size() == 3
            customersLists.collect { it.get(0) }.unique{a, b -> a.id() <=> b.id()}.size() == 1

            noExceptionThrown();
        cleanup:
            pool.shutdown()
            _cleanup(testDB)
        where:
            testDB       | maxId
            TestDB.HSQL  | 7
            TestDB.MYSQL | 8
    }

    private static def getCallableForCreateCustomer(customerDao, firstName, lastName){
        return new Callable<List<Void>>(){
            List<Void> call() throws Exception {
//                println "getCallableForCreateCustomer: ${Thread.currentThread()}"
                customerDao.createCustomer0(firstName, lastName)
                return null;
            }
        }
    }

    private static def getCallableForListCustomers(customerDao){
        return new Callable<List<Customer>>(){
            List<Customer> call() throws Exception {
//                println "getCallableForListCustomers: ${Thread.currentThread()}"
                return customerDao.listCustomers2()
            }
        }
    }
}
