package spwrap.proxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.Caller.ParamType;
import spwrap.Caller.TypedOutputParamMapper;
import spwrap.annotations.Scalar;
import spwrap.result.Result;

class ScalarBinder {

	private static Logger log = LoggerFactory.getLogger(ScalarBinder.class);

	static void setScalar(Method method, MetaData metadata) {

		Scalar scalarAnnot = method.getDeclaredAnnotation(Scalar.class);
		if (scalarAnnot != null) {

			log.debug("Scalar annotation exists, try auto-mapping");
			int sqlType = scalarAnnot.value();
			
			metadata.outputParamMapper = new ScalarTypedOutputParamMapper(sqlType);
			metadata.outParamTypes = Arrays.asList(ParamType.of(sqlType));
		}
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
			return Arrays.asList(sqlType);
		}
	}
}
