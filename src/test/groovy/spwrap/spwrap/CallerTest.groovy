package spwrap

import static java.sql.Types.*;
import static spwrap.Caller.*;
import static spwrap.Caller.Param.*;

import spock.lang.*

// integration test
@Unroll
class CallerTest extends Specification{

	def caller
	
	def setup() {
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE")
		TestUtils.install()
		
		caller = new Caller(TestUtils.ds)
	}
	
	def cleanup() {
		TestUtils.rollback()
	}
	
	def "create customer using the caller interface"(){
		given:
			def firstName = "Abdullah"
			def lastName = "Mohammad"
		
		when:
			def custId = caller.call("create_customer", params(of("abdullah", VARCHAR), of("mohammad", VARCHAR)),
				paramTypes(INTEGER), {it.getInt(1)});
							
		then:
			custId == 0
	}
}