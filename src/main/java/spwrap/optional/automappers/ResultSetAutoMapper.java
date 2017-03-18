package spwrap.optional.automappers;

import spwrap.CallException;
import spwrap.Util;
import spwrap.mappers.ResultSetMapper;

public abstract class ResultSetAutoMapper<T> implements ResultSetMapper<T> {

    private static final boolean FOUND_SPRING_JDBC = Util.isClassPresent("org.springframework.jdbc.core.BeanPropertyRowMapper");
    private static final boolean FOUND_COMMONS_DBUTILS = Util.isClassPresent("org.apache.commons.dbutils.BeanProcessor");

    private Class<?> type;

    ResultSetAutoMapper(Class<?> type) {
        this.type = type;
    }

    public static <T> ResultSetAutoMapper newInstance(Class<T> type){

        if (FOUND_SPRING_JDBC){
            return new SpringResultSetAutoMapper<T>(type);
        }else if (FOUND_COMMONS_DBUTILS){
            return new DbUtilsResultSetAutoMapper<T>(type);
        }else{
            throw new CallException("cannot use auto-mapping unless either spring-jdbc or commons-dbutil found on classpath");
        }
    }

    @SuppressWarnings("unchecked")
    public Class<T> getType() {
        return (Class<T>) type;
    }
}
