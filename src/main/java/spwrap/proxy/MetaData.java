package spwrap.proxy;

import spwrap.Caller.Param;
import spwrap.Caller.ParamType;
import spwrap.mappers.OutputParamMapper;
import spwrap.mappers.ResultSetMapper;

import java.util.List;

class
/* struct */ MetaData {

    String storedProcName;

    List<Param> inParams;
    ResultSetMapper<?> rsMapper;

    OutputParam outputParam = new OutputParam();
    Props props = new Props();

    static class
    /* struct */ OutputParam {
        List<ParamType> outParamTypes;
        OutputParamMapper<?> outputParamMapper;
    }
}
