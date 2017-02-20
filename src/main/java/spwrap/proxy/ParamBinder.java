package spwrap.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.Caller;
import spwrap.Caller.Param;

class ParamBinder implements Binder<List<Param>> {

	private static Logger log = LoggerFactory.getLogger(ParamBinder.class);

	public List<Param> bind(Method method, Object... args) {
		List<Param> params = null;

		Parameter[] parameters = method.getParameters();
		if (parameters.length > 0) {
			params = new ArrayList<Caller.Param>();
		}

		for (int i = 0; i < parameters.length; i++) {

			Parameter parameter = parameters[i];
			spwrap.annotations.Param paramAnnot = parameter.getDeclaredAnnotation(spwrap.annotations.Param.class);
			if (paramAnnot == null) {
				throw new IllegalArgumentException("missing @Param annotation on parameter: " + parameter.getName());
			} else {
				params.add(Caller.Param.of(args[i], paramAnnot.value()));
			}
		}

		log.debug("inParams are: {} for method: {}", params, method.getName());

		return params;
	}
}
