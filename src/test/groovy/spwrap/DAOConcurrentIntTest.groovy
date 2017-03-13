package spwrap

import spock.lang.Specification
import spock.lang.Unroll
import testhelpers.TestDB
import testhelpers.TestUtils

import java.util.concurrent.Callable
import java.util.concurrent.Executors

// integration test
@Unroll
class DAOConcurrentIntTest extends Specification{

	CustomerDAO customerDao

    def _setup(db) {
        TestUtils.install(db)
        customerDao = new DAO.Builder(db.dbInfo.dataSource()).build().create(CustomerDAO.class)
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

            def futureCustomersList = pool.invokeAll(Arrays.asList(
                    getCallableForListCustomers(customerDao),
                    getCallableForListCustomers(customerDao),
                    getCallableForListCustomers(customerDao)
            ));

            List<List<Customer>> customersLists = futureCustomersList.collect{it.get()} // join

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
            testDB           | maxId
            TestDB.HSQL      | 7
            TestDB.MYSQL     | 8
            TestDB.SQLServer | 8
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
