package spwrap.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.CallException;
import spwrap.Caller;
import spwrap.mappers.ResultSetMapper;
import spwrap.mappers.TypedOutputParamMapper;
import spwrap.Tuple;
import spwrap.annotations.Mapper;
import spwrap.annotations.Scalar;
import spwrap.annotations.StoredProc;
import spwrap.proxy.MetaData.OutputParam;

public class DAOInvocationHandler implements InvocationHandler {

    private static Logger log = LoggerFactory.getLogger(DAOInvocationHandler.class);

    private final Caller caller;

    public DAOInvocationHandler(Caller caller) {
        this.caller = caller;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        MetaData metadata = getMetaData(method, args);

        if (metadata != null) {
            Tuple<?, ?> call = caller.call(metadata.storedProcName, metadata.inParams, metadata.outputParam.outParamTypes,
                    metadata.outputParam.outputParamMapper, metadata.rsMapper);

            if (metadata.outputParam.outputParamMapper == null) {
                return call.list();
            }

            if (metadata.rsMapper == null) {
                return call.object();
            }
            return call;
        } else {
            return null;
        }
    }

    private MetaData getMetaData(Method method, Object[] args) {

        StoredProc storedProcAnnot = method.getAnnotation(StoredProc.class);
        if (storedProcAnnot != null) {

            MetaData metaData = new MetaData();

            preValidate(method);

            String storedProc = storedProcAnnot.value();
            if (storedProc.trim().length() == 0) {
                storedProc = method.getName();
            }
            metaData.storedProcName = storedProc;
            log.debug("storedProcName name is: {} for method: {}", storedProc, method.getName());

            metaData.inParams = new ParamBinder().bind(method, args);
            metaData.rsMapper = new ResultSetMapperBinder().bind(method, args);

            OutputParam outputParam = new OutputParamBinder().bind(method, args);
            if (outputParam != null) {
                metaData.outputParam = outputParam;
            } else if ((outputParam = new ScalarBinder().bind(method, args)) != null) {
                metaData.outputParam = outputParam;
            }

            postValidate(method, metaData);

            return metaData;
        } else {
            log.warn("method {} doesn't declare @StoredProc annotation, skipping.", method.getName());
            return null;
        }
    }

    private void preValidate(Method method) {
        Mapper mapperAnnot = method.getAnnotation(Mapper.class);

        if (mapperAnnot != null && method.getAnnotation(Scalar.class) != null) {
            throw new CallException("either @Scalar or @Mapper could be provided, Not Both!");
        }

        if (mapperAnnot != null) {
            Class<?>[] mapperClasses = mapperAnnot.value();

            if (mapperClasses.length > 2) {
                throw new CallException("only two mapper classes are allowed");
            }

            for (Class<?> clazz : mapperClasses) {
                if (!ResultSetMapper.class.isAssignableFrom(clazz)
                        && !TypedOutputParamMapper.class.isAssignableFrom(clazz)) {
                    throw new CallException(
                            "Mapper classes should implement either ResultSetMapper or TypedOutputParamMapper");
                }
            }
        }
    }

    private void postValidate(Method method, MetaData metadata) {
        if (metadata.rsMapper == null && metadata.outputParam.outputParamMapper == null
                && method.getReturnType() != void.class) {
            throw new CallException(
                    String.format("method %s return type is not void however no mapping provided!", method.getName()));
        }
    }
}
