package spwrap;

import static java.sql.Types.*;
import java.util.Arrays;
import java.util.List;

import spwrap.Caller.ResultSetMapper;
import spwrap.Caller.TypedOutputParamMapper;

public class Customer implements TypedOutputParamMapper<Customer>, ResultSetMapper<Customer> {

	private Integer id;
	private String firstName, lastName;

	// mandatory when implementing TypedOutputParamMapper or ResultSetMapper
	public Customer() {
	}

	public Customer(Integer id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Integer id() {
		return id;
	}

	public String firstName() {
		return firstName;
	}

	public String lastName() {
		return lastName;
	}

	@Override
	public Customer map(Result result, int index) {
		this.firstName = result.getString(index);
		this.lastName = result.getString(index + 1);
		return this;
	}

	@Override
	public List<Integer> getTypes() {
		return Arrays.asList(VARCHAR, VARCHAR);
	}

	@Override
	public Customer map(Result result) {
		return new Customer(result.getInt(1), result.getString(2), result.getString(3));
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}
