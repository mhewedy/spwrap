package spwrap.result

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import spwrap.CallException

import java.sql.CallableStatement
import java.sql.SQLException

@Unroll
class CallableStatementWrapperTest extends Specification {

    def callableStatementWrapper
    def callableStatementMock = Mock(CallableStatement)

    @Shared METHOD_NAMES = ["getString", "getBoolean", "getByte", "getShort", "getInt",
                            "getLong", "getFloat", "getDouble", "getBytes", "getDate", "getTime",
                            "getTimestamp", "getObject", "getBigDecimal", "getRef", "getBlob", "getClob",
                            "getArray", "getURL"];
    void setup() {
        callableStatementWrapper = new CallableStatementWrapper(callableStatementMock, 1)
    }

    def "calling #methodName(int) on CallableStatementWrapper calls the same method name on CallableStatement" (String methodName){
        when:
            callableStatementWrapper."$methodName"(1)
        then:
            1 * callableStatementMock./get.*/(_)
        where:
            methodName << METHOD_NAMES
    }

    def "calling #methodName(String) on CallableStatementWrapper calls the same method name on CallableStatement" (String methodName){
        when:
            callableStatementWrapper."$methodName"("some_column_name")
        then:
            1 * callableStatementMock./get.*/(_)
        where:
            methodName << METHOD_NAMES
    }

    def "calling #methodName(int) on CallableStatementWrapper throws exception when CallableStatement method throws exception"() {
        given:
            def exceptionMsg = "I throw an exception"
        when:
            callableStatementMock./get.*/(_) >> { throw new SQLException(exceptionMsg) }
            callableStatementWrapper."$methodName"(1)
        then:
            def e = thrown(CallException)
            e.cause.class == SQLException
            e.cause.message == exceptionMsg
        where:
            methodName << METHOD_NAMES
    }
}
