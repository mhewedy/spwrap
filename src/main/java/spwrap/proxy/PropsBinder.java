package spwrap.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spwrap.CallException;
import spwrap.props.ConnectionProps;
import spwrap.props.ResultSetProps;
import spwrap.props.StatementProps;

import java.lang.reflect.Method;

import static spwrap.proxy.MetaData.*;

class PropsBinder implements Binder<PropsWrapper> {

    private static Logger log = LoggerFactory.getLogger(PropsBinder.class);

    public PropsWrapper bind(Method method, Object... args) {

        spwrap.annotations.Props.Connection connectionAnnot = null;
        spwrap.annotations.Props.Statement statementAnnot = null;
        spwrap.annotations.Props.ResultSet resultSetAnnot = null;

        if (method.isAnnotationPresent(spwrap.annotations.Props.class)) {
            spwrap.annotations.Props propsAnnot = method.getAnnotation(spwrap.annotations.Props.class);
            connectionAnnot = propsAnnot.connection();
            statementAnnot = propsAnnot.statement();
            resultSetAnnot = propsAnnot.resultSet();
        } else {
            log.debug("Props annotation not found!");
        }

        if (connectionAnnot != null && method.isAnnotationPresent(spwrap.annotations.Props.Connection.class)) {
            throw new CallException("Connection Props is already supplied");
        } else {
            connectionAnnot = method.getAnnotation(spwrap.annotations.Props.Connection.class);
        }

        if (statementAnnot != null && method.isAnnotationPresent(spwrap.annotations.Props.Statement.class)) {
            throw new CallException("@Statement Props is already supplied");
        } else {
            statementAnnot = method.getAnnotation(spwrap.annotations.Props.Statement.class);
        }

        if (resultSetAnnot != null && method.isAnnotationPresent(spwrap.annotations.Props.ResultSet.class)) {
            throw new CallException("ResultSet Props is already supplied");
        } else {
            resultSetAnnot = method.getAnnotation(spwrap.annotations.Props.ResultSet.class);
        }

        PropsWrapper propsWrapper = new PropsWrapper();
        if (connectionAnnot != null) propsWrapper.connectionProps = bind(connectionAnnot);
        if (statementAnnot != null) propsWrapper.statementProps = bind(statementAnnot);
        if (resultSetAnnot != null) propsWrapper.resultSetProps = bind(resultSetAnnot);
        return propsWrapper;
    }

    private ConnectionProps bind(spwrap.annotations.Props.Connection annot) {
        return new ConnectionProps(annot.readOnly(), annot.transactionIsolation());
    }

    private StatementProps bind(spwrap.annotations.Props.Statement annot) {
        return new StatementProps(annot.queryTimeout());
    }

    private ResultSetProps bind(spwrap.annotations.Props.ResultSet annot) {
        return new ResultSetProps(annot.fetchDirection(), annot.fetchSize(), annot.maxFieldSize(), annot.maxRows()
                , annot.type(), annot.concurrency(), annot.holdability());
    }
}
