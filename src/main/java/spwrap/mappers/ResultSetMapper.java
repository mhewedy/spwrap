package spwrap.mappers;

import spwrap.result.Result;

public interface ResultSetMapper<T> extends Mapper<T> {
    /**
     * Use result.getXXX(1) to access the result of the first column in the
     * result set and result.getXXX(2) to access the second and so on.
     * <p>
     * <p>
     * NOTE: this method should return a new object.
     *
     * @param result A 1-based container for output parameter values
     * @return a new Object represents the result set
     * @see Result
     */
    T map(Result<?> result);
}
