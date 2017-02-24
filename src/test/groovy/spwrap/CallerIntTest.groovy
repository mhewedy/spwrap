package spwrap

import static java.sql.Types.*
import static org.slf4j.impl.SimpleLogger.*;
import static spwrap.Caller.*;
import static spwrap.Caller.Param.*;

import spock.lang.*

// integration test
@Unroll
class CallerIntTest extends Specification{

	def caller
	
	def setup() {
		System.setProperty(DEFAULT_LOG_LEVEL_KEY, "TRACE")
		TestUtils.install()
		
		caller = new Caller(TestUtils.dataSource)
	}
	
	def cleanup() {
		TestUtils.rollback()
	}
	
	def "create customer using the caller interface"(){
		given:
			def firstName = "Abdullah"
			def lastName = "Mohammad"
		
		when:
			def custId = caller.call("create_customer", params(of(firstName, VARCHAR), of(lastName, VARCHAR)),
				paramTypes(INTEGER), {it.getInt(1)});
							
		then:
			custId == 0
	}

	def "create customer using the caller interface and Persistable"(){
		given:
		def customer  = new Customer2("Abdullah", "Mohammad")

		when:
		def custId = caller.call("create_customer", customer.toInputParams(), paramTypes(INTEGER), {it.getInt(1)});

		then:
		custId == 0
	}
}