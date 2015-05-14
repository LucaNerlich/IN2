package de.hawhamburg.se;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vor dem Test muss die Datenbank mit dem Skript sql/createdb.sql erzeugt
 * worden sein.
 */
public class JPARelationshipsTest {

	// Die folgenden drei Konstanten m�ssen jeweils angepasst werden:
	private static final String DB_URL = "jdbc:oracle:thin:@//ora14.informatik.haw-hamburg.de:1521/inf14.informatik.haw-hamburg.de";
	private static final String DB_USER = "scott";
	private static final String DB_PASSWORD = "tiger";
	// Ende

	private static final Logger LOG = LoggerFactory
			.getLogger(JPARelationshipsTest.class);

	//Todo
	private static final String SURNAME_1 = "Felix";
	private static final String NAME_1 = "Furchtlos";
	private static final String SURNAME_2 = "Rudi";
	private static final String NAME_2 = "Ratlos";

	private static final String STREET_1 = "Murmelgasse";

	private static final String CREDITCARDNO_1 = "1234567890123456";
	private static final String CREDITCARDNO_2 = "0123456789012345";

	private static final String BANK_1 = "Money & More";
	private static final String BANK_2 = "Less Money";

	private static TransactionManager transactionManager;

	private EntityManagerFactory emf = null;
	private EntityManager em = null;

	@org.junit.BeforeClass
	public static void setUpClass() throws SQLException {
		transactionManager = new TransactionManager(DB_URL);
		transactionManager.connect(DB_USER, DB_PASSWORD);
	}

	@org.junit.AfterClass
	public static void tearDownClass() {
		transactionManager.disconnect();
		transactionManager = null;
	}

	@org.junit.Before
	public void setUp() throws SQLException {
		transactionManager.executeSQLDeleteOrUpdate(
				"delete from BANK_CUSTOMER",
				TransactionManager.EMPTY_PARAMETERS);
		transactionManager.executeSQLDeleteOrUpdate("delete from CREDITCARD",
				TransactionManager.EMPTY_PARAMETERS);
		transactionManager.executeSQLDeleteOrUpdate("delete from CUSTOMER",
				TransactionManager.EMPTY_PARAMETERS);
		transactionManager.executeSQLDeleteOrUpdate("delete from BANK",
				TransactionManager.EMPTY_PARAMETERS);
		transactionManager.executeSQLDeleteOrUpdate("delete from ADDRESS",
				TransactionManager.EMPTY_PARAMETERS);
		transactionManager.commit();
	}

	@org.junit.After
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
//Todo: Do you see a good possibility to really test this?

	@org.junit.Test
	public void testEntityManager() throws SQLException {
		createEntityManager();
		em.getTransaction().begin();
		em.getTransaction().commit();
	}
// Add good test cases!

}
