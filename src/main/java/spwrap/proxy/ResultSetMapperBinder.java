package spwrap.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spwrap.CallException;
import spwrap.Tuple;
import spwrap.annotations.Mapper;
import spwrap.mappers.ResultSetMapper;
import spwrap.optional.automappers.ResultSetAutoMapper;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

class ResultSetMapperBinder extends MapperBinder<ResultSetMapper<?>> {

    private static final int FIRST_GENERIC_TYPE_INDEX = 0;
    private static Logger log = LoggerFactory.getLogger(ResultSetMapperBinder.class);

    @SuppressWarnings("unchecked")
    ResultSetMapper<?> fromAnnotation(Method method) {

        Mapper mapperAnnotation = method.getAnnotation(Mapper.class);
        if (mapperAnnotation != null) {

            Class<ResultSetMapper<?>> clazz = null;

            for (Class<?> c : mapperAnnotation.value()) {
                if (ResultSetMapper.class.isAssignableFrom(c)) {
                    if (clazz != null) {
                        throw new CallException("ResultSetMapper is already registered");
                    }
                    clazz = (Class<ResultSetMapper<?>>) c;
                }
            }

            if (clazz != null) {
                ResultSetMapper<?> instance = newInstance(clazz);
                log.debug("found annotation result set: {} for method: {}", instance.getClass(), method.getName());
                return instance;
            }
        }
        return null;
    }

    ResultSetMapper<?> fromReturnType(Method method) {
        ResultSetMapper<?> resultSetMapper = null;

        if (List.class.isAssignableFrom(method.getReturnType())
                || Tuple.class.isAssignableFrom(method.getReturnType())) {

            Class<?> clazz = getReturnType(method);

            if (ResultSetMapper.class.isAssignableFrom(clazz)) {
                resultSetMapper = newInstance(clazz);
                log.debug("found return type result set: {} for method: {}", resultSetMapper.getClass(),
                        method.getName());
            }
        }
        return resultSetMapper;
    }

    ResultSetMapper<?> fromAutoMapper(Method method) {
        return ResultSetAutoMapper.newInstance(getReturnType(method));
    }

    private ResultSetMapper<?> newInstance(Class<?> clazz) {
        try {
            return (ResultSetMapper<?>) clazz.newInstance();
        } catch (Exception e) {
            throw new CallException("cannot create resultSet Mapper", e);
        }
    }

    private Class<?> getReturnType(Method method) {
        ParameterizedType listType = (ParameterizedType) method.getGenericReturnType();
        return (Class<?>) listType.getActualTypeArguments()[FIRST_GENERIC_TYPE_INDEX];
    }
}
