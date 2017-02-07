package spwrap.result;

public class ObjectResult<T> extends Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private T object;

	public ObjectResult(boolean success, String message, T object) {
		super(success, message);
		this.object = object;
	}

	public T getObject() {
		return object;
	}
	
	public Result toResult() {
		return (Result) this;
	}

	@Override
	public String toString() {
		return "[object=" + object + ", success=" + isSuccess() + ", message=" + getMessage() + "]";
	}

}
