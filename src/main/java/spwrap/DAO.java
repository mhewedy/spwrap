package spwrap;

import java.lang.reflect.Proxy;

import javax.sql.DataSource;

import spwrap.proxy.DAOInvocationHandler;

public class DAO {

	private final Caller caller;

	private DAO(Builder builder) {
		this.caller = builder.caller;
	}

	@SuppressWarnings("unchecked")
	public <T> T create(Class<T> dao) {
		return (T) Proxy.newProxyInstance(dao.getClassLoader(), new Class<?>[] { dao },
				new DAOInvocationHandler(caller));
	}

	public static class Builder {
		private final Caller caller;

        /**
         * <p>
         * This is the preferred way to  create instance of the DAO interface
         * <p>Try to use Pooled DataSources in production for achieve better performance.
         * @param dataSource dataSource to get connections from
         */
		public Builder(DataSource dataSource) {
			this.caller = new Caller(dataSource);
		}

		public Builder(String jdbcUrl, String username, String password) {
			this.caller = new Caller(jdbcUrl, username, password);
		}

		public Builder config(Config config) {
			this.caller.setConfig(config);
			return this;
		}

		public DAO build() {
			return new DAO(this);
		}
	}
}
