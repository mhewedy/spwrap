package spwrap.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spwrap.Caller;
import spwrap.Tuple;
import spwrap.annotations.StoredProc;
import spwrap.proxy.MetaData.OutputParam;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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
        long start = System.currentTimeMillis();

        StoredProc storedProcAnnot = method.getAnnotation(StoredProc.class);
        if (storedProcAnnot != null) {

            MetaData metaData = new MetaData();

            Validator.preValidate(method);

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

            Validator.postValidate(method, metaData);

            log.debug("getMetaData on method: {} took: {} ms", method.getName(), (System.currentTimeMillis() - start));

            return metaData;
        } else {
            log.warn("method {} doesn't declare @StoredProc annotation, skipping.", method.getName());
            return null;
        }
    }
}
