package spwrap;

import static java.sql.Types.*;
import java.util.Arrays;
import java.util.List;

import spwrap.Caller.ResultSetMapper;
import spwrap.Caller.TypedOutputParamMapper;
import spwrap.result.Result;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see spwrap.Caller.OutputParamMapper#map(spwrap.result.Result)
	 * 
	 * this method implement both interface TypedOutputParamMapper and
	 * ResultSetMapper, so to distinguish between both of them use #isResultSet
	 * or #isCallableStatement as below
	 */
	public Customer map(Result<?> result) {
		if (result.isResultSet()) {
			// ResultSetMapper
			// SAME order and types of result set of list_customers stored proc
			return new Customer(result.getInt(1), result.getString(2), result.getString(3));
		} else {
			// TypedOutputParamMapper
			// SAME order and types of output params of get_customer stored proc
			return new Customer(null, result.getString(1), result.getString(2));
		}
	}

	// TypedOutputParamMapper
	public List<Integer> getTypes() {
		return Arrays.asList(VARCHAR, VARCHAR);
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
}
