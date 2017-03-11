package spwrap.optional.automappers

import spock.lang.Specification
import spock.lang.Unroll
import spwrap.CallException
import spwrap.Customer
import spwrap.result.Result

import java.sql.ResultSet
import java.sql.SQLException

@Unroll
class SpringResultSetAutoMapperTest extends Specification {

    def "test call map while rs.getMetaData() throws SQLException makes the map throws CallException" (){
        given:
            SpringResultSetAutoMapper autoMapper = new SpringResultSetAutoMapper(Customer);
            def resultSetMock = Mock(ResultSet)
            def resultMock = Mock(Result)
        when:
            resultSetMock.getMetaData() >> {throw new SQLException()}
            resultMock.wrappedObject() >> resultSetMock
            autoMapper.map(resultMock)
        then:
            def ex = thrown(CallException)
            ex.cause.class == SQLException
    }
}
