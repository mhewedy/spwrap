package spwrap;

import java.util.Arrays;
import java.util.List;

import static java.sql.Types.INTEGER;
import static java.sql.Types.VARCHAR;
import static spwrap.Caller.Param.of;

/**
 * Created by mhewedy on 2/24/17.
 */
public class Customer2 implements Persistable {

    private Integer id;
    private String firstName, lastName;

    public Customer2(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public List<Caller.Param> toInputParams() {
        return Arrays.asList(of(firstName, VARCHAR), of(lastName, VARCHAR));
    }
}
