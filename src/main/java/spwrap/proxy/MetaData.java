package spwrap.proxy;

import spwrap.Caller.Param;
import spwrap.Caller.ParamType;
import spwrap.mappers.OutputParamMapper;
import spwrap.mappers.ResultSetMapper;
import spwrap.props.ConnectionProps;
import spwrap.props.ResultSetProps;
import spwrap.props.StatementProps;

import java.util.List;

class
/* struct */ MetaData {

    String storedProcName;

    List<Param> inParams;
    ResultSetMapper<?> rsMapper;

    OutputParam outputParam = new OutputParam();
    PropsWrapper propsWrapper = new PropsWrapper();

    static class
    /* struct */ OutputParam {
        List<ParamType> outParamTypes;
        OutputParamMapper<?> outputParamMapper;
    }

    public static class
    /* struct */ PropsWrapper {
        ConnectionProps connectionProps = new ConnectionProps();
        StatementProps statementProps = new StatementProps();
        ResultSetProps resultSetProps = new ResultSetProps();
    }
}
