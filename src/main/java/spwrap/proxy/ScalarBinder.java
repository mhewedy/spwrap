package spwrap.proxy;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.Caller.ParamType;
import spwrap.mappers.TypedOutputParamMapper;
import spwrap.annotations.Scalar;
import spwrap.proxy.MetaData.OutputParam;
import spwrap.result.Result;

class ScalarBinder implements Binder<OutputParam> {

	private static Logger log = LoggerFactory.getLogger(ScalarBinder.class);

	public OutputParam bind(Method method, Object... args) {
		OutputParam outputParam = null;

		Scalar scalarAnnot = method.getAnnotation(Scalar.class);
		if (scalarAnnot != null) {

			log.debug("Scalar annotation exists, try auto-mapping");
			int sqlType = scalarAnnot.value();

			outputParam = new OutputParam();
			outputParam.outputParamMapper = new ScalarTypedOutputParamMapper(sqlType);
			outputParam.outParamTypes = Collections.singletonList(ParamType.of(sqlType));
		}

		return outputParam;
	}

	@SuppressWarnings("rawtypes")
	private static class ScalarTypedOutputParamMapper implements TypedOutputParamMapper {
		private int sqlType;

		public ScalarTypedOutputParamMapper(int sqlType) {
			this.sqlType = sqlType;
		}

		public Object map(Result result) {
			return result.getObject(1);
		}

		public List getTypes() {
			return Collections.singletonList(sqlType);
		}
	}

}
