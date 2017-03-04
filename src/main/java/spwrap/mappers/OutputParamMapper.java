package spwrap.mappers;

import spwrap.result.Result;

public interface OutputParamMapper<T> extends Mapper<T> {
    /**
     * Use result.getXXX(1) to access the result of the first output
     * parameter and result.getXXX(2) to access the second and so on.
     *
     * @param result A 1-based container for output parameter values
     * @return
     * @see Result
     */
    T map(Result<?> result);
}
