package spwrap.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spwrap.CallException;
import spwrap.proxy.Props.ConnectionProps;
import spwrap.proxy.Props.ResultSetProps;

import java.lang.reflect.Method;

class PropsBinder implements Binder<Props> {

    private static Logger log = LoggerFactory.getLogger(PropsBinder.class);

    public Props bind(Method method, Object... args) {

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
            throw new CallException("@Connection is already defined");
        } else {
            connectionAnnot = method.getAnnotation(spwrap.annotations.Props.Connection.class);
        }

        if (statementAnnot != null && method.isAnnotationPresent(spwrap.annotations.Props.Statement.class)) {
            throw new CallException("@Statement is already defined");
        } else {
            statementAnnot = method.getAnnotation(spwrap.annotations.Props.Statement.class);
        }

        if (resultSetAnnot != null && method.isAnnotationPresent(spwrap.annotations.Props.ResultSet.class)) {
            throw new CallException("@ResultSet is already defined");
        } else {
            resultSetAnnot = method.getAnnotation(spwrap.annotations.Props.ResultSet.class);
        }

        Props props = new Props();
        props.connectionProps = connectionAnnot != null ? bind(connectionAnnot) : null;
        props.statementProps = statementAnnot != null ? bind(statementAnnot) : null;
        props.resultSetProps = resultSetAnnot != null ? bind(resultSetAnnot) : null;

        return props;

    }

    private ConnectionProps bind(spwrap.annotations.Props.Connection annot) {
        ConnectionProps props = new ConnectionProps();
        props.readOnly = annot.readOnly();
        props.transactionIsolation = annot.transactionIsolation();
        return props;
    }

    private Props.StatementProps bind(spwrap.annotations.Props.Statement annot) {
        Props.StatementProps props = new Props.StatementProps();
        props.fetchDirection = annot.fetchDirection();
        props.fetchSize = annot.fetchSize();
        props.maxFieldSize = annot.maxFieldSize();
        props.maxRows = annot.maxRows();
        props.queryTimeout = annot.queryTimeout();
        return props;
    }

    private ResultSetProps bind(spwrap.annotations.Props.ResultSet annot) {
        ResultSetProps props = new ResultSetProps();
        props.type = annot.type();
        props.concurrency = annot.concurrency();
        props.holdability = annot.holdability();
        return props;
    }
}
