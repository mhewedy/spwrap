package spwrap.props

import spock.lang.Specification
import spock.lang.Unroll
import spwrap.CallException

import java.sql.Connection
import java.sql.SQLException

import static spwrap.annotations.Props.TransactionIsolation.*

@Unroll
class ConnectionPropsTest extends Specification {


    def "test calling ConnectionProps.apply and throw exception when setReadOnly throws Exception" (){
        given:
            def ConnectionProps props = new ConnectionProps(true, DEFAULT)
            def connectionMock = Mock(Connection)
        when:
            connectionMock.setReadOnly(_ as Boolean) >> {throw new SQLException()}
            props.apply(connectionMock)
        then:
            thrown(CallException)
    }

    def "test calling ConnectionProps.from and throw exception when connection.isReadOnly throws Exception" (){
        given:
            def connectionMock = Mock(Connection)
        when:
            connectionMock.isReadOnly() >> {throw new SQLException()}
            ConnectionProps.from(connectionMock)
        then:
            thrown(CallException)
    }

    def "test calling ConnectionProps.from and connection isReadOnly return #readOnlyValue" (){
        given:
            def connectionMock = Mock(Connection)
        when:
            connectionMock.isReadOnly() >> {readOnlyValue}
            ConnectionProps props = ConnectionProps.from(connectionMock)
        then:
            props.readOnly == readOnlyValue
        where:
            readOnlyValue << [true, false]
    }

    def "test calling ConnectionProps.from and connection isReadOnly return #readOnlyValue, check toString" (){
        given:
            def connectionMock = Mock(Connection)
        when:
            connectionMock.isReadOnly() >> {readOnlyValue}
            ConnectionProps props = ConnectionProps.from(connectionMock)
        then:
            props.toString().contains("readOnly=" + readOnlyValue)
        where:
            readOnlyValue << [true, false]
    }
}
