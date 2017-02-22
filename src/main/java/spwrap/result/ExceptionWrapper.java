package spwrap.result;

import spwrap.CallException;

import java.sql.SQLException;

/*
The main cause of this class is to make there's one and only one place to contain the exception path of calling getXXX methods.
This should affect the test coverage ratio
 */
class ExceptionWrapper {

    static <T> T call(Caller<T> caller){
        try {
            return caller.call();
        } catch (SQLException e) {
            throw new CallException(e);
        }
    }

    static interface Caller<T>{
        T call() throws SQLException;
    }
}
