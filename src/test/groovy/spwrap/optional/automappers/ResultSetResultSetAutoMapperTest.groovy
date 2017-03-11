package spwrap.optional.automappers

import spock.lang.Specification

class ResultSetResultSetAutoMapperTest extends Specification {


    def "Test use spring instance when both on classpath" (){
        when:
            def newInstance = ResultSetAutoMapper.newInstance(Object.class);
        then:
            SpringResultSetAutoMapper.class == newInstance.class

    }
}
