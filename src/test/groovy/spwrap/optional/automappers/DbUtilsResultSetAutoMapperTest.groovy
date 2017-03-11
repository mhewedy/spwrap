package spwrap.optional.automappers

import org.apache.commons.dbutils.GenerousBeanProcessor
import spock.lang.Specification
import spock.lang.Unroll
import spwrap.CallException
import spwrap.Customer
import spwrap.result.Result

import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException

@Unroll
class DbUtilsResultSetAutoMapperTest extends Specification {

    def "test call map while rs.getMetaData() throws SQLException makes the map throws CallException" (){
        given:
            DbUtilsResultSetAutoMapper autoMapper = new DbUtilsResultSetAutoMapper(Customer);
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

    def "test call map while rs.getMetaData().getColumnCount() returns 1 makes the map return the object as result of rs.getObject(1): #object" (){
        given:
            DbUtilsResultSetAutoMapper autoMapper = new DbUtilsResultSetAutoMapper(Customer);
            def resultSetMock = Mock(ResultSet)
            def resultMock = Mock(Result)
            def metaDataMock = Mock(ResultSetMetaData)
        when:
            metaDataMock.getColumnCount() >> {1}
            resultSetMock.getMetaData() >> metaDataMock
            resultSetMock.getObject(1) >> object
            resultMock.wrappedObject() >> resultSetMock
        then:
            autoMapper.map(resultMock) == object
        where:
            object << ["Hello", 30, 30.0f, new Date()]
    }

    def "test call map while rs.getMetaData().getColumnCount() returns 3 makes the map return the object as result of beanProcessor.toBean(): #object" (){
        given:
            DbUtilsResultSetAutoMapper autoMapper = new DbUtilsResultSetAutoMapper(Customer);
            def resultSetMock = Mock(ResultSet)
            def resultMock = Mock(Result)
            def metaDataMock = Mock(ResultSetMetaData)
            def beanProcessorMock = Mock(GenerousBeanProcessor)
        when:
            metaDataMock.getColumnCount() >> {3}
            resultSetMock.getMetaData() >> metaDataMock
            resultMock.wrappedObject() >> resultSetMock
            beanProcessorMock.toBean(*_) >> object
            autoMapper.beanProcessor = beanProcessorMock
        then:
            autoMapper.map(resultMock) == object
        where:
            object << [new Customer(1, "Abdullah", "Muhammad"), new Customer(1, "Farida", "Muhammad")]
    }

}
