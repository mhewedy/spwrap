package spwrap.props

import spock.lang.Specification
import spock.lang.Unroll
import spwrap.CallException

import java.sql.CallableStatement
import java.sql.SQLException

@Unroll
class StatementPropsTest extends Specification {


    def "test calling StatementProps.apply and throw exception when setReadOnly throws Exception" (){
        given:
            def StatementProps props = new StatementProps(10)
            def stmtMock = Mock(CallableStatement)
        when:
            stmtMock.setQueryTimeout(_ as Integer) >> {throw new SQLException()}
            props.apply(stmtMock)
        then:
            thrown(CallException)
    }

    def "test calling creating StatementProps then call toString will print the proper property values" (){
        given:
            def StatementProps props = new StatementProps(value)
        when:
            def toString = props.toString()
        then:
            toString.contains("queryTimeout=" + value)
        where:
            value << [10]
    }
}
