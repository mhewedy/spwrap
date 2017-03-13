package spwrap

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import spwrap.component.ComponentDAO
import testhelpers.TestDB
import testhelpers.TestUtils

// integration test
@Unroll
class ComponentDAOTest extends Specification{

    @Shared ComponentDAO componentDAO

    def setupSpec() {
        TestUtils.install(TestDB.MYSQL)
        componentDAO = new DAO.Builder(TestDB.MYSQL.dbInfo.dataSource())
                .config(new Config().useStatusFields(false))
                .build()
                .create(ComponentDAO.class)
    }

    def cleanupSpec() {
        TestUtils.rollback(TestDB.MYSQL)
    }

	def "inserting many rows"(){
		when:
            componentDAO.fillComponent()
		then:
			noExceptionThrown()
	}

    def "retrieve many rows" (){
        when:
            def list = componentDAO.listComponents()
        then:
            list.size() == 500
    }
}
