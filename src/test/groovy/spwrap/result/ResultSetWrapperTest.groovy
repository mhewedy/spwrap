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

    def "resultSet getString(columnIndex)"() {
        when:
        resultSetWrapper.getString(1)
        then:
        1 * resultSetMock.getString(_ as Integer)
    }

    def "resultSet getInteger(int)"() {
        given:
        def someColumnIndex = 1
        when:
        resultSetWrapper.getInteger(someColumnIndex)
        then:
        1 * resultSetMock.getInteger(someColumnIndex)
    }

    def "resultSet getDouble(int)"() {
        given:
        def someColumnIndex = 1
        when:
        resultSetWrapper.getDouble(someColumnIndex)
        then:
        1 * resultSetMock.getDouble(someColumnIndex)
    }

    def "resultSet getLong(int)"() {
        given:
        def someColumnIndex = 1
        when:
        resultSetWrapper.getLong(someColumnIndex)
        then:
        1 * resultSetMock.getLong(someColumnIndex)
    }

    def "resultSet getNString(int)"() {
        given:
        def someColumnIndex = 1
        when:
        resultSetWrapper.getNString(someColumnIndex)
        then:
        1 * resultSetMock.getNString(someColumnIndex)
    }

    def "resultSet getShort(int)"() {
        given:
        def someColumnIndex = 1
        when:
        resultSetWrapper.getShort(someColumnIndex)
        then:
        1 * resultSetMock.getShort(someColumnIndex)
    }


    def "resultSet getNClob(int)"() {
        given:
        def someColumnIndex = 1
        when:
        resultSetWrapper.getNClob(someColumnIndex)
        then:
        1 * resultSetMock.getNClob(someColumnIndex)
    }


    def "resultSet getSQLXML(int)"() {
        given:
        def someColumnIndex = 1
        when:
        resultSetWrapper.getSQLXML(someColumnIndex)
        then:
        1 * resultSetMock.getSQLXML(someColumnIndex)
    }


    def "resultSet getBigDecimal(int)"() {
        given:
        def someColumnIndex = 1
        when:
        resultSetWrapper.getBigDecimal(someColumnIndex)
        then:
        1 * resultSetMock.getBigDecimal(someColumnIndex)
    }


    def "resultSet getCharacterStream(int)"() {
        given:
        def someColumnIndex = 1
        when:
        resultSetWrapper.getCharacterStream(someColumnIndex)
        then:
        1 * resultSetMock.getCharacterStream(someColumnIndex)
    }


    def "resultSet getTimestamp(int)"() {
        given:
        def someColumnIndex = 1
        when:
        resultSetWrapper.getTimestamp(someColumnIndex)
        then:
        1 * resultSetMock.getTimestamp(someColumnIndex)
    }

    def "resultSet getgetBytes(int)"() {
        given:
        def someColumnIndex = 1
        when:
        resultSetWrapper.getgetBytes(someColumnIndex)
        then:
        1 * resultSetMock.getgetBytes(someColumnIndex)
    }

    def "resultSet getXXX(XXX) throws SQLException, then resultSetWapper.getXXX(XXX) should fails"() {
        given:
        def exceptionMsg = "I thrown an exception"
        when:
        resultSetMock./get.*/(_) >> { throw new SQLException(exceptionMsg) }
        resultSetWrapper.getString(1)
        then:
        def e = thrown(CallException)
        e.cause.class == SQLException
        e.cause.message == exceptionMsg
    }
}
