package spwrap;

import java.io.Serializable;
import java.util.List;

public class Tuple<T, U> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7519151467184899254L;

    private List<T> list;
    private U object;

    Tuple(List<T> list, U object) {
        this.list = list;
        this.object = object;
    }

    public List<T> list() {
        return list;
    }

    public U object() {
        return object;
    }

    @Override
    public String toString() {
        return "Tuple [list=" + Util.listToString(list) + ", object=" + object + "]";
    }
}
