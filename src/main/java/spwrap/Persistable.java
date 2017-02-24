package spwrap;

import java.util.List;

import spwrap.Caller.Param;

/**
 * <p>
 * useful interface when using the Caller API.
 *
 * You can make your domain model implements this interface to make it easy passing your object as input parameterss.
 *
 *	<p>
 *
 *	example:
 *  <pre>
 * {@code
 *	public class Customer implements Persistable {
 *
 *		private Integer id;
 *		private String firstName, lastName;
 *
 *		public List<Caller.Param> toInputParams() {
 *			return Arrays.asList(of(firstName, VARCHAR), of(lastName, VARCHAR));
 *		}
 *	}
 *}
 * </pre>
 * then the caller API can be used like this:
 * <pre>
 * {@code
 * caller.call("create_customer", customer.toInputParams(), paramTypes(INTEGER), {it.getInt(1)});
 * }
 * </pre>
 *
 *
 *
 */
public interface Persistable {

	List<Param> toInputParams();
}