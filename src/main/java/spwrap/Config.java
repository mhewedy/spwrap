package spwrap;

public class Config {

	public boolean useStatusFields() {
		return Boolean.parseBoolean(System.getProperty("spwarp.use_status_fields", "true"));
	}

	public short successCode() {
		return Short.parseShort(System.getProperty("spwarp.success_code", "0"));
	}
}
