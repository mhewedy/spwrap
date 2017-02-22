package spwrap;

public class CallException extends RuntimeException {

	private static final long serialVersionUID = 5343255880584362785L;

	private short errorCode;

	public CallException() {
		super();
	}

	public CallException(String message, Throwable cause) {
		super(message, cause);
	}

	public CallException(String message) {
		super(message);
	}

	public CallException(short errorCode, String message) {
		super(String.format("Stored Procedure returns error code %s and error message %s", errorCode, message));
		this.errorCode = errorCode;
	}

	public CallException(Throwable cause) {
		super(cause);
	}

	public short getErrorCode() {
		return errorCode;
	}

}
