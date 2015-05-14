package de.hawhamburg.se;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
 * 
 * Das SQL-Skript muss auch angepasst werden, um diesen Test zum Laufen zu
 * bringen!
 * 
 * I provided some potenzially usefull methods. Feel free to enhance them.
 */
public class JPALockingTest {

	// Die folgenden drei Konstanten müssen jeweils angepasst werden:
	private static final String DB_URL = Messages.getString("INP2.0"); //$NON-NLS-1$
	private static final String DB_USER = Messages.getString("INP2.1"); //$NON-NLS-1$
	private static final String DB_PASSWORD = Messages.getString("INP2.2"); //$NON-NLS-1$
	// Ende

	private static final Logger LOG = LoggerFactory
			.getLogger(JPALockingTest.class);
//Add more, if you need or like.
	private static final String SURNAME_1 = Messages.getString("INP2.3"); //$NON-NLS-1$
	private static final String NAME_1 = Messages.getString("INP2.4"); //$NON-NLS-1$
	private static final String SURNAME_2 = Messages.getString("INP2.5"); //$NON-NLS-1$
	private static final String NAME_2 = Messages.getString("INP2.6"); //$NON-NLS-1$

	private static TransactionManager transactionManager;

	private EntityManagerFactory emf = null;
	private EntityManager em1 = null;
	private EntityManager em2 = null;

	@BeforeClass
	public static void setUpClass() throws SQLException, IOException {
		transactionManager = new TransactionManager(DB_URL);
		transactionManager.connect(DB_USER, DB_PASSWORD);
		System.setErr(new PrintStream("err.out"));
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
		closeEM(em1);
		em1 = null;
		closeEM(em2);
		em2 = null;
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

	private void closeEM(final EntityManager em) {
		if (em != null) {
			try {
				em.close();
			} catch (final Throwable ex) {
				LOG.warn("while closing entity manager", ex);
			}
		}
	}
// Add Entitymanagers for concurrent modifications.
	private void createEntityManagers() {
		emf = Persistence.createEntityManagerFactory("haw_demo");
	}

	@Test
	public void testEntityManagers() throws SQLException {
		createEntityManagers();
		em1.getTransaction().begin();
		em2.getTransaction().begin();
		em1.getTransaction().commit();
		em2.getTransaction().commit();
	}
// Add Tests cases for concurrent update (successfull, unsucessfull)
//	Hint: Look for RollbackException, OptimisticLockException

	public long getNextCustomerId() throws SQLException {
		final Object result = transactionManager.executeSQLQuerySingleResult(
				"select CUSTOMERSEQ.NEXTVAL from DUAL",
				TransactionManager.EMPTY_PARAMETERS);
		assert result != null;
		assert result instanceof BigDecimal : "Is: " + result.getClass();
		return ((BigDecimal) result).longValue();
	}

	private long insertCustomerInDB(final String surname, final String name)
			throws SQLException {
		final long id = getNextCustomerId();
		final List<Object> parameters = new ArrayList<Object>();
		parameters.add(new Long(id));
		parameters.add(surname);
		parameters.add(name);
		transactionManager.executeSQLInsert(
				"insert into CUSTOMER (ID, SURNAME, NAME) values (?, ?, ?)",
				parameters);
		transactionManager.commit();
		return id;
	}

}
