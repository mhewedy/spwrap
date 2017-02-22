package spwrap.result

import spock.lang.Specification
import spock.lang.Unroll
import spwrap.CallException

import java.sql.ResultSet
import java.sql.SQLException

@Unroll
class ResultSetWrapperTest extends Specification {

    ResultSetWrapper resultSetWrapper
    ResultSet resultSetMock = Mock()

    void setup() {
        resultSetWrapper = new ResultSetWrapper(resultSetMock)
    }

    def "resultSet getString(int)"() {
        given:
            def someColumnIndex = 1
        when:
            resultSetWrapper.getString(someColumnIndex)
        then:
            1 * resultSetMock.getString(someColumnIndex)
    }

    def "resultSet getString(any integer value)"() {
        when:
            resultSetWrapper.getString(1)
        then:
            1 * resultSetMock.getString(_ as Integer)
    }

    def "resultSet getDouble(any integer value)"() {
        when:
            resultSetWrapper.getDouble(1)
        then:
            1 * resultSetMock.getDouble(_ as Integer)
    }


    def "resultSet getXXX(XXX) throws SQLException, then resultSetWapper.getXXX(XXX) should fails"(){
        given:
            def exceptionMsg = "I thrown an exception"
        when:
            resultSetMock./get.*/(_) >> {throw new SQLException(exceptionMsg)}
            resultSetWrapper.getString(1)
        then:
            def e = thrown(CallException)
            e.cause.class == SQLException
            e.cause.message == exceptionMsg
    }
}
