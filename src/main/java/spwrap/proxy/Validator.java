package spwrap.proxy;

import spwrap.CallException;
import spwrap.annotations.AutoMapper;
import spwrap.annotations.Mapper;
import spwrap.annotations.Scalar;
import spwrap.mappers.ResultSetMapper;
import spwrap.mappers.TypedOutputParamMapper;

import java.lang.reflect.Method;

class Validator {

    static void preValidate(Method method) {
        Mapper mapperAnnot = method.getAnnotation(Mapper.class);
        AutoMapper resultSetAutoMapperAnnot = method.getAnnotation(AutoMapper.class);

        if (mapperAnnot != null && resultSetAutoMapperAnnot != null && method.getAnnotation(Scalar.class) != null) {
            throw new CallException("Only one of @Scalar, @Mapper or @ResultSetAutoMapper could be provided!");
        }

        preValidate(mapperAnnot);
    }

    static void postValidate(Method method, MetaData metadata) {
        if (metadata.rsMapper == null && metadata.outputParam.outputParamMapper == null
                && method.getReturnType() != void.class) {
            throw new CallException(
                    String.format("method %s return type is not void however no mapping provided!", method.getName()));
        }
    }

    private static void preValidate(Mapper mapperAnnot) {
        if (mapperAnnot != null) {
            Class<?>[] mapperClasses = mapperAnnot.value();

            if (mapperClasses.length > 2) {
                throw new CallException("only two mapper classes are allowed");
            }

            for (Class<?> clazz : mapperClasses) {
                if (!ResultSetMapper.class.isAssignableFrom(clazz)
                        && !TypedOutputParamMapper.class.isAssignableFrom(clazz)) {
                    throw new CallException(
                            "Mapper classes should implement either ResultSetMapper or TypedOutputParamMapper");
                }
            }
        }
    }
}
