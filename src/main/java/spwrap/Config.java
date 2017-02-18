package spwrap;

public class Config {

	// default values
	private boolean useStatusFields = true;
	private short successCode = 0;

	// setter methods -------------------------

	public Config useStatusFields(boolean useStatusFields) {
		this.useStatusFields = useStatusFields;
		return this;
	}

	public Config successCode(short successCode) {
		this.successCode = successCode;
		return this;
	}

	// query method -------------------------

	short successCode() {
		return this.successCode;
	}

	boolean useStatusFields() {
		return this.useStatusFields;
	}
}
