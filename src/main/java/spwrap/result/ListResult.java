package spwrap.result;

import java.util.List;

public class ListResult<T> extends Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4868875993458305018L;

	private List<T> list;

	public ListResult(boolean success, String message, List<T> list) {
		super(success, message);
		this.list = list;
	}

	public List<T> getList() {
		return list;
	}

	@Override
	public String toString() {
		return "[list=" + list + ", success=" + isSuccess() + ", message=" + getMessage() + "]";
	}
}
