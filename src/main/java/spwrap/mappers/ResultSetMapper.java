package spwrap.mappers;

import spwrap.result.Result;

/**
 * Created by mhewedy on 2/24/17.
 */
public interface ResultSetMapper<T> extends Mapper<T> {
    /**
     * Use result.getXXX(1) to access the result of the first column in the
     * result set and result.getXXX(2) to access the second and so on.
     * <br />
     * <br />
     * NOTE: this method should return a new object.
     *
     * @param result A 1-based container for output parameter values
     * @return
     * @see Result
     */
    T map(Result<?> result);
}
