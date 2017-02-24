package spwrap.mappers;

import java.util.List;

/**
 * Created by mhewedy on 2/24/17.
 */
public interface TypedOutputParamMapper<T> extends OutputParamMapper<T> {
    /**
     * @return list of Types as in {@link java.sql.Types}
     */
    List<Integer> getTypes();
}
