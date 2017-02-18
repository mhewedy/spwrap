package spwrap.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.CallException;
import spwrap.Caller;
import spwrap.Tuple;
import spwrap.annotations.Mapper;
import spwrap.annotations.Scalar;
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

		preValidate(method);

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
			ScalarBinder.setScalar(method, metadata);

			postValidate(method, metadata);

		} else {
			log.warn("method {} doesn't declare @StoredProc annotation, skipping.", method.getName());
		}

		return metadata;
	}

	private void postValidate(Method method, MetaData metadata) {
		if (metadata.rsMapper == null && metadata.outputParamMapper == null && method.getReturnType() != void.class) {
			throw new CallException(
					String.format("method %s return type is not void however no mapping provided!", method.getName()));
		}
	}

	private void preValidate(Method method) {
		if (method.getDeclaredAnnotation(Mapper.class) != null && method.getDeclaredAnnotation(Scalar.class) != null) {
			throw new CallException("either @Scalar or @Mapper could be provided, Not Both!");
		}
	}
}
