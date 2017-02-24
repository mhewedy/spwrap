package spwrap.mappers;

import spwrap.result.Result;

/**
 * <p>
 * Base interface for mappers, it is a Marker interface, it doesn't have to have any methods inside it.
 *</p>
 * Created by mhewedy on 2/24/17.
 */
public interface Mapper<T> {
    T map(Result<?> result);
}
