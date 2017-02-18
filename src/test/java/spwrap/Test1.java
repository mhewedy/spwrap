package spwrap;

import java.sql.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test1 {

	static CustomerDAO customerDao;

	@BeforeClass
	public static void setup() {

		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");

		TestUtils.loadData();
		
		DAO dao = new DAO.Builder(TestUtils.ds).build();
		customerDao = dao.create(CustomerDAO.class);
	}

	@Test
	public void _a_testCreateCustomer() {
		Integer id1 = customerDao.createCustomer("Abdullah", "Muhammad");
		Integer id2 = customerDao.createCustomer("Farida", "Muhammad");

		Assert.assertTrue(0 == id1);
		Assert.assertTrue(1 == id2);
	}

	@Test
	public void _b_testGetCustomer1() {
		Customer abdullah = customerDao.getCustomer1(0);
		Customer farida = customerDao.getCustomer1(1);

		Assert.assertEquals("Abdullah", abdullah.firstName());
		Assert.assertEquals("Muhammad", abdullah.lastName());

		Assert.assertEquals("Farida", farida.firstName());
		Assert.assertEquals("Muhammad", farida.lastName());
	}

	@Test
	public void _c_testGetCustomer2() {
		Customer abdullah = customerDao.getCustomer2(0);
		Customer farida = customerDao.getCustomer2(1);

		Assert.assertEquals("Abdullah", abdullah.firstName());
		Assert.assertEquals("Muhammad", abdullah.lastName());

		Assert.assertEquals("Farida", farida.firstName());
		Assert.assertEquals("Muhammad", farida.lastName());
	}

	@Test
	public void _d_testListCustomers1() {
		List<Customer> list = customerDao.listCustomers1();

		Assert.assertTrue(2 == list.size());

		Customer abdullah = list.get(0);
		Assert.assertTrue(0 == abdullah.id());
		Assert.assertEquals("Abdullah", abdullah.firstName());
		Assert.assertEquals("Muhammad", abdullah.lastName());

		Customer farida = list.get(1);
		Assert.assertTrue(1 == farida.id());
		Assert.assertEquals("Farida", farida.firstName());
		Assert.assertEquals("Muhammad", farida.lastName());
	}

	@Test
	public void _e_testListCustomers2() {
		List<Customer> list = customerDao.listCustomers2();

		Assert.assertTrue(2 == list.size());

		Customer abdullah = list.get(0);
		Assert.assertTrue(0 == abdullah.id());
		Assert.assertEquals("Abdullah", abdullah.firstName());
		Assert.assertEquals("Muhammad", abdullah.lastName());

		Customer farida = list.get(1);
		Assert.assertTrue(1 == farida.id());
		Assert.assertEquals("Farida", farida.firstName());
		Assert.assertEquals("Muhammad", farida.lastName());
	}

	@Test
	public void _f_testListCustomersWithDate() {
		Tuple<Customer, Date> tuple = customerDao.list_customers_with_date();

		Assert.assertTrue(2 == tuple.list().size());

		Customer abdullah = tuple.list().get(0);
		Assert.assertTrue(0 == abdullah.id());
		Assert.assertEquals("Abdullah", abdullah.firstName());
		Assert.assertEquals("Muhammad", abdullah.lastName());

		Customer farida = tuple.list().get(1);
		Assert.assertTrue(1 == farida.id());
		Assert.assertEquals("Farida", farida.firstName());
		Assert.assertEquals("Muhammad", farida.lastName());

		Assert.assertNotNull(tuple.object());
	}

	@Test
	public void _g_testListCustomersWithDate() {
		Tuple<Customer, Date> tuple = customerDao.listCustomersWithDate();

		Assert.assertTrue(2 == tuple.list().size());

		Customer abdullah = tuple.list().get(0);
		Assert.assertTrue(0 == abdullah.id());
		Assert.assertEquals("Abdullah", abdullah.firstName());
		Assert.assertEquals("Muhammad", abdullah.lastName());

		Customer farida = tuple.list().get(1);
		Assert.assertTrue(1 == farida.id());
		Assert.assertEquals("Farida", farida.firstName());
		Assert.assertEquals("Muhammad", farida.lastName());

		Assert.assertNotNull(tuple.object());
	}

	@Test
	public void _h_testListTables() {
		List<String> listTables = customerDao.listTables();

		Assert.assertTrue(listTables.size() > 2);
		Assert.assertTrue(listTables.contains("ROUTINES"));
		Assert.assertTrue(listTables.contains("CUSTOMERS"));
	}

	@Test(expected = CallException.class)
	public void _i_testListTables() {
		customerDao.callStoredProcWithError();
	}

	@Test
	public void _j_testGetCustomerWithNoErrorCodeOrMessage() {
		
		DAO dao = new DAO.Builder(TestUtils.ds)
				.config(new Config().useStatusFields(false))
				.build();
		
		CustomerDAO customerDAO2 = dao.create(CustomerDAO.class);
		Assert.assertNotNull(customerDAO2.getFirstTableNameNoResultFields());
	}
	
	@Test
	public void _k_changeSuccessValue() {
		
		DAO dao = new DAO.Builder(TestUtils.ds)
				.config(new Config().successCode((short) 1))
				.build();
		
		CustomerDAO customerDAO2 = dao.create(CustomerDAO.class);
		customerDAO2.callStoredProcWithError();
	}

}
