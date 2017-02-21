package spwrap.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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

		Annotation[][] annotList = method.getParameterAnnotations();

		if (annotList.length > 0) {
			params = new ArrayList<Caller.Param>();
		}

		for (int i = 0; i < annotList.length; i++) {
			Annotation[] annot = annotList[i];

			spwrap.annotations.Param paramAnnot = findParamAnnotation(annot);
			if (paramAnnot == null) {
				throw new IllegalArgumentException(
						"missing @Param annotation on parameters for method: " + method.getName());
			} else {
				params.add(Caller.Param.of(args[i], paramAnnot.value()));
			}
		}

		log.debug("inParams are: {} for method: {}", params, method.getName());

		return params;
	}

	private spwrap.annotations.Param findParamAnnotation(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			if (spwrap.annotations.Param.class.isAssignableFrom(annotation.getClass())) {
				return (spwrap.annotations.Param) annotation;
			}
		}
		return null;
	}
}
