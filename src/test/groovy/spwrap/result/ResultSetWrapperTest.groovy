package spwrap.result

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import spwrap.CallException

import java.sql.ResultSet
import java.sql.SQLException

@Unroll
class ResultSetWrapperTest extends Specification {

    def resultSetWrapper
    def resultSetMock = Mock(ResultSet)

    @Shared METHOD_NAMES = ["getString", "getBoolean", "getByte", "getShort", "getInt",
                                      "getLong", "getFloat", "getDouble", "getBytes", "getDate", "getTime",
                                      "getTimestamp", "getObject", "getBigDecimal", "getRef", "getBlob", "getClob",
                                      "getArray", "getURL"];
    void setup() {
        resultSetWrapper = new ResultSetWrapper(resultSetMock)
    }

    def "calling #methodName(int) on ResultSetWrapper calls the same method name on ResultSet" (String methodName){
        when:
            resultSetWrapper."$methodName"(1)
        then:
            1 * resultSetMock./get.*/(_)
        where:
            methodName << METHOD_NAMES
    }

    def "calling #methodName(String) on ResultSetWrapper calls the same method name on ResultSet" (String methodName){
        when:
            resultSetWrapper."$methodName"("some_column_name")
        then:
            1 * resultSetMock./get.*/(_)
        where:
            methodName << METHOD_NAMES
    }

    def "calling #methodName(int) on ResultSetWrapper throws exception when ResultSet method throws exception"() {
        given:
            def exceptionMsg = "I throw an exception"
        when:
            resultSetMock./get.*/(_) >> { throw new SQLException(exceptionMsg) }
            resultSetWrapper."$methodName"(1)
        then:
            def e = thrown(CallException)
            e.cause.class == SQLException
            e.cause.message == exceptionMsg
        where:
            methodName << METHOD_NAMES
    }

    def "calling #methodName(String) on ResultSetWrapper throws exception when ResultSet method throws exception"() {
        given:
        def exceptionMsg = "I throw an exception"
        when:
        resultSetMock./get.*/(_) >> { throw new SQLException(exceptionMsg) }
        resultSetWrapper."$methodName"("some_column_name")
        then:
        def e = thrown(CallException)
        e.cause.class == SQLException
        e.cause.message == exceptionMsg
        where:
        methodName << METHOD_NAMES
    }
}
