package spwrap.props;

public interface Props<T> {

    /**
     * @param input Object to apply props on
     * @return same input object, the return value here supplied for convenience
     */
    T apply(T input);
}
