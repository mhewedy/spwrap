package spwrap

import ch.vorburger.mariadb4j.DB
import ch.vorburger.mariadb4j.DBConfigurationBuilder
import testhelpers.TestDB
import testhelpers.TestUtils
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import spwrap.component.ComponentDAO

// integration test
@Unroll
class ComponentDAOTest extends Specification{

    @Shared def mysqlDbRef
    @Shared ComponentDAO componentDAO

    def setupSpec() {
        def dbConfig = DBConfigurationBuilder.newBuilder()
        dbConfig.setPort(3307)
        mysqlDbRef = DB.newEmbeddedDB(dbConfig.build())
        mysqlDbRef.start();


        TestUtils.install(TestDB.MYSQL)
        componentDAO = new DAO.Builder(TestDB.MYSQL.dbInfo.dataSource())
                .config(new Config().useStatusFields(false))
                .build()
                .create(ComponentDAO.class)
    }

    def cleanupSpec() {
        TestUtils.rollback(TestDB.MYSQL)
        mysqlDbRef.stop()
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
