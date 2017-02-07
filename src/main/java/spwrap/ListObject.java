package spwrap;

import java.io.Serializable;
import java.util.List;

public class ListObject<T, U> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7519151467184899254L;

	private List<T> list;
	private U object;

	public ListObject(List<T> list, U object) {
		this.list = list;
		this.object = object;
	}

	public List<T> getList() {
		return list;
	}

	public U getObject() {
		return object;
	}
}
