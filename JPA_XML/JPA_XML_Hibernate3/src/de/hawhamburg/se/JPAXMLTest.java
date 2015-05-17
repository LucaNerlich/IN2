package de.hawhamburg.se;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vor dem Test muss die Datenbank mit dem Skript sql/createdb.sql erzeugt
 * worden sein.
 * @author Ulrike Steffens.
 * @author Bernd Kahlbrandt, streamlined parts (at least in my optinion), externalized Strings, made changes for Java 8 and
 * added tests.
 * 
 */
public class JPAXMLTest {

	// Die folgenden drei Konstanten m�ssen jeweils angepasst werden:
	private static final String DB_URL = Messages.getString("INP2.0"); //$NON-NLS-1$
    //private static final String DB_USER = Messages.getString("INP2.1"); //$NON-NLS-1$
    private static final String DB_USER = getUsername();
    //		Description	Resource	Path	Location	Type
    //Verdeckte Eingabemoeglichkeit schaffen!
    //private static final String DB_PASSWORD = Messages.getString("INP2.2"); //$NON-NLS-1$
    private static final String DB_PASSWORD = getPassword();
	// Ende

	private static final Logger LOG = LoggerFactory.getLogger(JPAXMLTest.class);

	private static final String SURNAME_1 = Messages.getString("INP2.3"); //$NON-NLS-1$
	private static final String NAME_1 = Messages.getString("INP2.4"); //$NON-NLS-1$
	private static final String SURNAME_2 = Messages.getString("INP2.5"); //$NON-NLS-1$
	private static final String NAME_2 = Messages.getString("INP2.6"); //$NON-NLS-1$

	private static TransactionManager transactionManager;

	private EntityManagerFactory emf = null;
	private EntityManager em = null;

	@BeforeClass
	public static void setUpClass() throws SQLException {
		transactionManager = new TransactionManager(DB_URL);
		transactionManager.connect(DB_USER, DB_PASSWORD);
	}

	@AfterClass
	public static void tearDownClass() {
		transactionManager.disconnect();
		transactionManager = null;
	}

	@Before
	public void setUp() throws SQLException {
		transactionManager.executeSQLDeleteOrUpdate("delete from CUSTOMER",
				TransactionManager.EMPTY_PARAMETERS);
		transactionManager.commit();
	}

	@After
	public void tearDown() throws SQLException {
		if (em != null) {
			try {
				em.close();
			} catch (final Throwable ex) {
				LOG.warn("while closing entity manager", ex);
			} finally {
				em = null;
			}
		}
		if (emf != null) {
			try {
				emf.close();
			} catch (final Throwable ex) {
				LOG.warn("while closing entity manager factory", ex);
			} finally {
				emf = null;
			}
		}
	}

	private void createEntityManager() {
		emf = Persistence.createEntityManagerFactory("haw_demo");
		em = emf.createEntityManager();
	}

	@Test
	public void testEntityManager() throws SQLException {
		createEntityManager();
		em.getTransaction().begin();
		em.getTransaction().commit();
	}

	@Test
	public void testCreateCustomer() throws SQLException {
		createEntityManager();
		final Customer customer = new Customer(SURNAME_1, NAME_1);
		customer.setId(insertCustomerInDB(customer.getSurname(), customer.getName()));
		assertTrue(isCustomerOnDB(customer.getId(), SURNAME_1, NAME_1));
	}

	@Test
	public void testFindCustomer() throws SQLException {
		final long id = insertCustomerInDB(SURNAME_1, NAME_1);
		Customer customer = new Customer();
		customer.setId(id);
		customer.setName(NAME_1);
		customer.setSurname(SURNAME_1);
		assertNotNull(customer);
		assertEquals(id, customer.getId());
		assertEquals(SURNAME_1, customer.getSurname());
		assertEquals(NAME_1, customer.getName());
	}

	@Test
	public void testQueryCustomer() throws SQLException {
		final long id = insertCustomerInDB(SURNAME_1, NAME_1);
		createEntityManager();
		em.getTransaction().begin();
		final List<Customer> customers = em.createQuery("from Customer",
				Customer.class).getResultList();
		assertEquals(1, customers.size());
		final Customer customer = customers.get(0);
		assertEquals(id, customer.getId());
		assertEquals(SURNAME_1, customer.getSurname());
		assertEquals(NAME_1, customer.getName());
	}
	
	private long insertCustomerInDB(final String surname, final String name)
			throws SQLException {
		final long id = getNextCustomerId();
		final List<Object> parameters = new ArrayList<>();
		parameters.add(new Long(id));
		parameters.add(surname);
		parameters.add(name);
		transactionManager.executeSQLInsert(
				"insert into CUSTOMER (ID, SURNAME, NAME) values (?, ?, ?)",
				parameters);
		transactionManager.commit();
		return id;
	}


	@Test
	public void testUpdateCustomer() throws SQLException {
		final long id = insertCustomerInDB(SURNAME_1, NAME_1);
		assertFalse(isCustomerOnDB(id, SURNAME_2, NAME_2));
		createEntityManager();
		em.getTransaction().begin();
		final Customer customer = em.find(Customer.class, new Long(id));
		customer.setName(NAME_2);
		customer.setSurname(SURNAME_2);
		em.persist(customer);
		assertFalse(isCustomerOnDB(id, SURNAME_2, NAME_2));
		em.flush();
		assertFalse(isCustomerOnDB(id, SURNAME_2, NAME_2));
		em.getTransaction().commit();
		assertTrue(isCustomerOnDB(id, SURNAME_2, NAME_2));
	}

	@Test
	public void testDeleteCustomer() throws SQLException {
		final long id = insertCustomerInDB(SURNAME_1, NAME_1);
		assertTrue(isCustomerOnDB(id, SURNAME_1, NAME_1));
		createEntityManager();
		em.getTransaction().begin();
		final Customer customer = em.find(Customer.class, new Long(id));
		em.remove(customer);
		em.getTransaction().commit();
		assertFalse(isCustomerOnDB(id, SURNAME_1, NAME_1));
	}

	private boolean isCustomerOnDB(final long id, final String surname,
			final String name) throws SQLException {
		final List<Object> parameters = new ArrayList<>();
		parameters.add(new Long(id));
		parameters.add(surname);
		parameters.add(name);
		return BigDecimal.ONE
				.equals(transactionManager
						.executeSQLQuerySingleResult(
								"select count(*) from CUSTOMER where ID = ? and SURNAME = ? and NAME = ?",
								parameters));
	}

	public long getNextCustomerId() throws SQLException {
		final Object result = transactionManager.executeSQLQuerySingleResult(
				"select CUSTOMERSEQ.NEXTVAL from DUAL",
				TransactionManager.EMPTY_PARAMETERS);
		assert result != null;
		assert result instanceof BigDecimal : "Is: " + result.getClass();
		return ((BigDecimal) result).longValue();
	}

    private static String getUsername() {
    /* Benutzername abfragen */
        String user = javax.swing.JOptionPane
                .showInputDialog("Enter Username");

        return user;
    }

    private static String getPassword() {
    /* Passwort abfragen */

        JPasswordField passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*');
        JOptionPane.showMessageDialog(null, passwordField,
                "Enter password", JOptionPane.OK_OPTION);
        char[] pw = passwordField.getPassword();
        String password = String.valueOf(pw);

        return password;
    }
}
