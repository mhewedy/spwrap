package spwrap

import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Specification
import spock.lang.Unroll

import javax.sql.DataSource

import static testhelpers.TestDB.MYSQL
import static testhelpers.TestUtils.install
import static testhelpers.TestUtils.rollback

// integration test
@Unroll
class SpringTransactionsIntTest extends Specification{

    SpringTransactionsService service
    def supplierDAO

    def setupSpringBeans(String springConfigXml){
        install(MYSQL)

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(springConfigXml);
        service = context.getBean(SpringTransactionsService)
        supplierDAO = new DAO.Builder(context.getBean(DataSource))
                .config(new Config().useStatusFields(false))
                .build()
                .create(SupplierDAO.class)
    }
	
	def "using @Transactional on method will rollback the effect of the stored proc from the database"(){
		given:
            setupSpringBeans("config/spring-transactions-int-test.xml")
        when:
            service.setSupplierDAO(supplierDAO)
            service.insert2SuppliersTheJdbcTemplateFails()
		then:
            thrown(DataIntegrityViolationException)
			supplierDAO.getSupplierCount() == 0
        cleanup:
            rollback(MYSQL)
	}

    def "using @Transactional on method (while not enabling spring annotation-driven transactions) will NOT rollback the effect of the stored proc from the database"(){
        given:
            setupSpringBeans("config/spring-transactions-int-test-missing-tx-annotation-driven.xml")
        when:
            service.setSupplierDAO(supplierDAO)
            service.insert2SuppliersTheJdbcTemplateFails()
        then:
            thrown(DataIntegrityViolationException)
            supplierDAO.getSupplierCount() == 1
        cleanup:
            rollback(MYSQL)
    }

    def "using @Transactional on method will rollback the effect of the stored proc from the database - if the stored proc fails, it will throw CallException"(){
        given:
            setupSpringBeans("config/spring-transactions-int-test.xml")
        when:
            service.setSupplierDAO(supplierDAO)
            service.insert2SuppliersTheStoredProcFails()
        then:
            thrown(CallException)
            supplierDAO.getSupplierCount() == 0
        cleanup:
            rollback(MYSQL)
    }
}
