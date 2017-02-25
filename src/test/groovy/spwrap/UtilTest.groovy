package spwrap

import spock.lang.Specification
import uk.org.lidalia.slf4jtest.TestLogger
import uk.org.lidalia.slf4jtest.TestLoggerFactory

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class UtilTest extends Specification {

    TestLogger logger = TestLoggerFactory.getTestLogger(Util)

    def cleanup() {
        TestLoggerFactory.clear()
    }

    def "test listToString"() {
        given:
            def list = Arrays.asList("HELLO", "WORLD")
        when:
            def listToString = Util.listToString(list)
        then:
            listToString == "HELLO\nWORLD\n"
    }

    def "test listToString when list is null"() {
        given:
            def list = null
        when:
            def listToString = Util.listToString(list)
        then:
            listToString == ""
    }

    def "closeDBObjects when result set throws SQLException"() {
        given:
            def resultSetMock = Mock(ResultSet)
        when:
            resultSetMock.close() >> { throw new SQLException("xxxx") }
            Util.closeDBObjects(null, null, resultSetMock)
        then:
            logger.loggingEvents.get(0).getMessage() == "Could not close ResultSet"
            logger.loggingEvents.get(0).throwable.get().class == SQLException
    }

    def "closeDBObjects when result set throws RuntimeException"() {
        given:
            def resultSetMock = Mock(ResultSet)
        when:
            resultSetMock.close() >> { throw new RuntimeException("xxxx") }
            Util.closeDBObjects(null, null, resultSetMock)
        then:
            logger.loggingEvents.get(0).getMessage() == "Unexpected exception on closing ResultSet"
            logger.loggingEvents.get(0).throwable.get().class == RuntimeException
    }

    def "closeDBObjects when statement throws SQLException"() {
        given:
            def statementMock = Mock(Statement)
        when:
            statementMock.close() >> { throw new SQLException("xxxx") }
            Util.closeDBObjects(null, statementMock, null)
        then:
            logger.loggingEvents.get(0).getMessage() == "Could not close Statement"
            logger.loggingEvents.get(0).throwable.get().class == SQLException
    }

    def "closeDBObjects when statement throws RuntimeException"() {
        given:
            def statementMock = Mock(Statement)
        when:
            statementMock.close() >> { throw new RuntimeException("xxxx") }
            Util.closeDBObjects(null, statementMock, null)
        then:
            logger.loggingEvents.get(0).getMessage() == "Unexpected exception on closing Statement"
            logger.loggingEvents.get(0).throwable.get().class == RuntimeException
    }

    def "closeDBObjects when connection throws SQLException"() {
        given:
            def connectionMock = Mock(Connection)
        when:
            connectionMock.close() >> { throw new SQLException("xxxx") }
            Util.closeDBObjects(connectionMock, null, null)
        then:
            logger.loggingEvents.get(0).getMessage() == "Could not close Connection"
            logger.loggingEvents.get(0).throwable.get().class == SQLException
    }

    def "closeDBObjects when connection throws RuntimeException"() {
        given:
            def connectionMock = Mock(Connection)
        when:
            connectionMock.close() >> { throw new RuntimeException("xxxx") }
            Util.closeDBObjects(connectionMock, null, null)
        then:
            logger.loggingEvents.get(0).getMessage() == "Unexpected exception on closing Connection"
            logger.loggingEvents.get(0).throwable.get().class == RuntimeException
    }

    def "getAsString returns the correct value"(){
        when:
            def string = Util.getAsString(-7)
        then:
            string == "BIT"
    }
}
