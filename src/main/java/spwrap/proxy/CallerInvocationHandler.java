package spwrap.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.Caller;
import spwrap.Tuple;
import spwrap.annotations.StoredProc;

public class CallerInvocationHandler implements InvocationHandler {

	private static Logger log = LoggerFactory.getLogger(CallerInvocationHandler.class);

	private final Caller caller;

	public CallerInvocationHandler(Caller caller) {
		this.caller = caller;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		MetaData metadata = getMetaData(method, args);

		Tuple<?, ?> call = caller.call(metadata.storedProcName, metadata.inParams, metadata.outParamTypes,
				metadata.outputParamMapper, metadata.rsMapper);

		if (metadata.outputParamMapper == null) {
			return call.list();
		}

		if (metadata.rsMapper == null) {
			return call.object();
		}

		return call;
	}

	private MetaData getMetaData(Method method, Object[] args) {
		MetaData metadata = new MetaData();

		StoredProc storedProcAnnot = method.getDeclaredAnnotation(StoredProc.class);
		if (storedProcAnnot != null) {

			String storedProc = storedProcAnnot.value();
			if (storedProc == null || storedProc.trim().isEmpty()) {
				storedProc = method.getName();
			}
			metadata.storedProcName = storedProc;
			log.debug("storedProcName name is: {} for method: {}", storedProc, method.getName());

			ParamsBinder.setInParams(method, args, metadata);
			MapperBinder.setMappers(method, metadata);

		} else {
			log.warn("method {} doesn't declare @StoredProc annotation, skipping.", method.getName());
		}

		return metadata;
	}

}
