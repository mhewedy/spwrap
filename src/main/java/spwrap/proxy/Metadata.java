package spwrap.proxy;

import java.util.List;

import spwrap.Caller.OutputParamMapper;
import spwrap.Caller.Param;
import spwrap.Caller.ParamType;
import spwrap.Caller.ResultSetMapper;

public class Metadata {

	String storedProcName;
	List<Param> inParams;
	List<ParamType> outParamTypes;
	OutputParamMapper<?> outputParamMapper;
	ResultSetMapper<?> rsMapper;

}
