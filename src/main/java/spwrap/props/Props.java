package spwrap.props;

public interface Props<T, U> {

    U apply(T input, Object ... args);
}
