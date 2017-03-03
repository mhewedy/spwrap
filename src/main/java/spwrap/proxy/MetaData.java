package spwrap.proxy;

import java.util.List;

import spwrap.mappers.OutputParamMapper;
import spwrap.Caller.Param;
import spwrap.Caller.ParamType;
import spwrap.mappers.ResultSetMapper;

class
/* struct */ MetaData {

    String storedProcName;

    List<Param> inParams;
    ResultSetMapper<?> rsMapper;

    OutputParam outputParam = new OutputParam();

    static class
    /* struct */ OutputParam {
        List<ParamType> outParamTypes;
        OutputParamMapper<?> outputParamMapper;
    }
}
