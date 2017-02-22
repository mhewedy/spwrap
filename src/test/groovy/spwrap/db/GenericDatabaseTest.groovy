package spwrap.db

import spock.lang.Specification
import spwrap.CallException

import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException

/**
 * Created by mhewedy on 2/22/2017.
 */
class GenericDatabaseTest extends Specification {

    def "CallException is thrown when Connection.getMetaData throws SQLException"() {
        given:
            def connectionMock = Mock(Connection)
        when:
            connectionMock.getMetaData() >> {throw new SQLFeatureNotSupportedException()}
            GenericDatabase.from(connectionMock)
        then:
            def ex = thrown(CallException)
            SQLException.isAssignableFrom(ex.cause.class)
    }

    def "Exception is thrown when Connection.getMetaData throws SQLException"() {
        given:
            def connectionMock = Mock(Connection)
        when:
            connectionMock.getMetaData() >> {throw new UnsupportedOperationException()}
            GenericDatabase.from(connectionMock)
        then:
            thrown(UnsupportedOperationException)
    }

    def "Database used in test is not HSQL, it is MySQL" (){
        given:
            def databaseMetaDataMock = Mock(DatabaseMetaData)
            def connectionMock = Mock(Connection)
        when:
            databaseMetaDataMock.getDatabaseProductName() >> {"XX MySQL XX"}
            connectionMock.getMetaData() >> databaseMetaDataMock
            def database = GenericDatabase.from(connectionMock)
        then:
            database.class == GenericDatabase
    }

    def "Database used in test is HSQL" (){
        given:
            def databaseMetaDataMock = Mock(DatabaseMetaData)
            def connectionMock = Mock(Connection)
        when:
            databaseMetaDataMock.getDatabaseProductName() >> {"XX HSQL XX"}
            connectionMock.getMetaData() >> databaseMetaDataMock
            def database = GenericDatabase.from(connectionMock)
        then:
            database.class == HSQL
    }
}

