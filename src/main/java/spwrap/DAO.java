package spwrap;

import java.lang.reflect.Proxy;

import javax.sql.DataSource;

import spwrap.proxy.DAOInvocationHandler;

/**
 * <p>
 * Used to create new instance of DAO classes
 * <pre>
 * {@code
 *Config optionalConfig = //
 *   DAO dao = new DAO.Builder(dataSource).config(optionalConfig).build();
 *   MyDAO myDao = dao.create(MyDAO.class);
 * }
 * </pre>
 * DAO objects created by {@link #create(Class)} method are thread-safe and can be cached.
 * <p>
 * For example, in spring framework you can create spring @Bean for each of your DAOs.
 * <p>
 */
public class DAO {

    private final Caller caller;

    private DAO(Builder builder) {
        this.caller = builder.caller;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> dao) {
        return (T) Proxy.newProxyInstance(dao.getClassLoader(), new Class<?>[]{dao},
                new DAOInvocationHandler(caller));
    }

    public static class Builder {
        private final Caller caller;

        /**
         * <p>
         * This is the preferred way to  create instance of the DAO interface
         * <p>Try to use Pooled DataSources in production for achieve better performance.
         *
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
