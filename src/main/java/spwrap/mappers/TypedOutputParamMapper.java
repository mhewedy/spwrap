package spwrap.mappers;

import java.util.List;

public interface TypedOutputParamMapper<T> extends OutputParamMapper<T> {
    /**
     * @return list of Types as in {@link java.sql.Types}
     */
    List<Integer> getTypes();
}
