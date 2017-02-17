package spwrap.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.Caller;

class ParamsBinder {

	private static Logger log = LoggerFactory.getLogger(ParamsBinder.class);

	static void setInParams(Method method, Object[] args, MetaData metadata) {
		Parameter[] parameters = method.getParameters();
		if (parameters.length > 0) {
			metadata.inParams = new ArrayList<Caller.Param>();
		}

		for (int i = 0; i < parameters.length; i++) {

			Parameter parameter = parameters[i];
			spwrap.annotations.Param paramAnnot = parameter.getDeclaredAnnotation(spwrap.annotations.Param.class);
			if (paramAnnot == null) {
				throw new IllegalArgumentException("missing @Param annotation on parameter: " + parameter.getName());
			} else {
				metadata.inParams.add(Caller.Param.of(args[i], paramAnnot.value()));
			}
		}

		log.debug("inParams are: {} for method: {}", metadata.inParams, method.getName());
	}
}
