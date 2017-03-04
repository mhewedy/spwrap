package spwrap.optional.automappers;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import spwrap.CallException;
import spwrap.result.Result;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Uses @{@link GenerousBeanProcessor}
 * @param <T>
 */
class DbUtilsResultSetAutoMapper<T> extends ResultSetAutoMapper<T> {

    private final BeanProcessor beanProcessor = new GenerousBeanProcessor();

    DbUtilsResultSetAutoMapper(Class<?> type) {
        super(type);
    }

    @SuppressWarnings("unchecked")
    public T map(Result<?> result) {

        ResultSet rs = (ResultSet) result.wrappedObject();

        try {
            if (rs.getMetaData().getColumnCount() == 1){
                // same code as org.apache.commons.dbutils.handlers.ScalarHandler
                // (cannot reuse the object as it calls rs.next())
                return (T) rs.getObject(1);
            }else {
                return (T) beanProcessor.toBean(rs, getType());
            }

        } catch (SQLException e) {
            throw new CallException(e.getMessage(), e);
        }
    }
}
