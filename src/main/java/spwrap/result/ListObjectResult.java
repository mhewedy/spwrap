package spwrap.result;

import java.util.List;

public class ListObjectResult<T, U> extends Result {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7519151467184899254L;

	private List<T> list;
	private U object;

	public ListObjectResult(boolean success, String message, List<T> list, U object) {
		super(success, message);
		this.list = list;
		this.object = object;
	}

	public List<T> getList() {
		return list;
	}

	public U getObject() {
		return object;
	}

	public ListResult<T> toListResult() {
		return new ListResult<T>(this.isSuccess(), this.getMessage(), list);
	}

	public ObjectResult<U> toObjectResult() {
		return new ObjectResult<U>(this.isSuccess(), this.getMessage(), object);
	}

	@Override
	public String toString() {
		return "[list=" + list + ", object=" + object + ", success=" + isSuccess() + ", message=" + getMessage() + "]";
	}

}
