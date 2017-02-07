package spwrap;

public class CallException extends RuntimeException {

	private static final long serialVersionUID = 5343255880584362785L;

	public CallException() {
		super();
	}

	public CallException(String message, Throwable cause) {
		super(message, cause);
	}

	public CallException(String message) {
		super(message);
	}

	public CallException(Throwable cause) {
		super(cause);
	}

}
