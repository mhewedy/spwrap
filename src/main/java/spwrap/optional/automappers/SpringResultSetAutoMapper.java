package spwrap.optional.automappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.util.ReflectionUtils;
import spwrap.CallException;
import spwrap.result.Result;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 *     Uses {@link BeanPropertyRowMapper} and {@link SingleColumnRowMapper}
 */
class SpringResultSetAutoMapper<T> extends ResultSetAutoMapper<T> {

    private final RowMapper<T> beanPropertyRowMapper = BeanPropertyRowMapper.newInstance(getType());
    private final RowMapper<T> singleColumnRowMapper = SingleColumnRowMapper.newInstance(getType());

    SpringResultSetAutoMapper(Class<?> type) {
        super(type);
    }

    public T map(Result<?> result) {

        ResultSet rs = (ResultSet) result.wrappedObject();
        RowMapper<T> rowMapper;

        try {
            rowMapper = (rs.getMetaData().getColumnCount() == 1) ? singleColumnRowMapper : beanPropertyRowMapper;
            return rowMapper.mapRow(rs, getRowIndex(result));
        } catch (SQLException e) {
            throw new CallException(e.getMessage(), e);
        }
    }

    private Integer getRowIndex(Result<?> result) {
        Field rowIndexField = ReflectionUtils.findField(result.getClass(), "rowIndex");
        rowIndexField.setAccessible(true);
        return (Integer) ReflectionUtils.getField(rowIndexField, result);
    }
}
