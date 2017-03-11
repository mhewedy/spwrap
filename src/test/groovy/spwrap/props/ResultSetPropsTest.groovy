package spwrap.props

import spock.lang.Specification
import spock.lang.Unroll

import java.sql.CallableStatement
import java.sql.Connection

import static spwrap.annotations.Props.ResultSetConcurrency.CONCUR_READ_ONLY
import static spwrap.annotations.Props.ResultSetHoldability.CLOSE_CURSORS_AT_COMMIT
import static spwrap.annotations.Props.ResultSetHoldability.HOLD_CURSORS_OVER_COMMIT
import static spwrap.annotations.Props.ResultSetType.TYPE_FORWARD_ONLY

@Unroll
class ResultSetPropsTest extends Specification {

    def "test calling ResultSetProps.apply on ResultSetProps with some holdability affects the returned Statement holdability " (){
        given:
            def props = new ResultSetProps(spwrap.annotations.Props.FetchDirection.FETCH_FORWARD, 0, 0, 0
                    , TYPE_FORWARD_ONLY, CONCUR_READ_ONLY, h)
            def connectionMock = Mock(Connection)
            def callStmt = Mock(CallableStatement)
        when:
            connectionMock.prepareCall(_ as String, _ as Integer, _ as Integer, h.getValue()) >> callStmt
            callStmt.getResultSetHoldability() >> {h.getValue()}
            def call = props.apply(connectionMock, "{ call my_sp ? ? ?}")
        then:
            call.getResultSetHoldability() == h.getValue()
        where:
        h << [CLOSE_CURSORS_AT_COMMIT, HOLD_CURSORS_OVER_COMMIT]
    }

    def "test calling ResultSetProps.apply on ResultSetProps with some holdability affects the toString " (){
        given:
            def props = new ResultSetProps(spwrap.annotations.Props.FetchDirection.FETCH_FORWARD, 0, 0, 0
                    , TYPE_FORWARD_ONLY, CONCUR_READ_ONLY, h)
            def connectionMock = Mock(Connection)
            def callStmt = Mock(CallableStatement)
        when:
            connectionMock.prepareCall(_ as String, _ as Integer, _ as Integer, h.getValue()) >> callStmt
            callStmt.getResultSetHoldability() >> {h.getValue()}
            def call = props.apply(connectionMock, "{ call my_sp ? ? ?}")
        then:
            props.toString().contains("holdability=" + hStr)
        where:
            h                               | hStr
            CLOSE_CURSORS_AT_COMMIT         | CLOSE_CURSORS_AT_COMMIT.toString()
            HOLD_CURSORS_OVER_COMMIT        | HOLD_CURSORS_OVER_COMMIT.toString()
    }
}
