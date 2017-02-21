package spwrap.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spwrap.CallException;
import spwrap.Caller.ResultSetMapper;
import spwrap.Tuple;
import spwrap.annotations.Mapper;

class ResultSetMapperBinder extends MapperBinder<ResultSetMapper<?>, Class<ResultSetMapper<?>>> {

	private static final int FIRST_GENERIC_TYPE_INDEX = 0;
	private static Logger log = LoggerFactory.getLogger(ResultSetMapperBinder.class);

	@SuppressWarnings("unchecked")
	public ResultSetMapper<?> fromAnnotation(Method method) {

		Mapper mapperAnnot = method.getAnnotation(Mapper.class);
		if (mapperAnnot != null) {

			Class<ResultSetMapper<?>> clazz = null;

			for (Class<?> c : mapperAnnot.value()) {
				if (ResultSetMapper.class.isAssignableFrom(c)) {
					if (clazz != null) {
						throw new CallException("ResultSetMapper is already registred");
					}
					clazz = (Class<ResultSetMapper<?>>) c;
				}
			}

			if (clazz != null) {
				ResultSetMapper<?> instance = fromClazz(method, clazz);
				log.debug("found annotation result set: {} for method: {}", instance.getClass(), method.getName());
				return instance;
			}
		}
		return null;
	}

	public ResultSetMapper<?> fromReturnType(Method method) {
		ResultSetMapper<?> resultSetMapper = null;

		if (List.class.isAssignableFrom(method.getReturnType())
				|| Tuple.class.isAssignableFrom(method.getReturnType())) {

			ParameterizedType listType = (ParameterizedType) method.getGenericReturnType();
			Class<?> clazz = (Class<?>) listType.getActualTypeArguments()[FIRST_GENERIC_TYPE_INDEX];

			if (ResultSetMapper.class.isAssignableFrom(clazz)) {
				resultSetMapper = fromClazz(method, clazz);
				log.debug("found return type result set: {} for method: {}", resultSetMapper.getClass(),
						method.getName());
			}
		}
		return resultSetMapper;
	}

	private ResultSetMapper<?> fromClazz(Method method, Class<?> clazz) {
		try {
			ResultSetMapper<?> instance = (ResultSetMapper<?>) clazz.newInstance();
			return instance;
		} catch (Exception e) {
			throw new CallException("cannot create resultSet Mapper", e);
		}
	}
}
