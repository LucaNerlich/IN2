package de.hawhamburg.se;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Vor dem Start der Tests m�ssen auf der Datenbank eine Tabelle CUSTOMER und
 * eine Sequenz CUSTOMERSEQ angelegt werden. Dazu kann das Skript
 * sql/createdb.sql genutzt werden.
 * <p>
 * Es ist die Klasse CustomerDAOImpl fertig zu implementieren.
 *
 * @author Ulrike Steffens.
 * @author Bernd Kahlbrandt, streamlined parts (at least in my optinion), externalized Strings, made changes for Java 8 and
 *         added tests.
 */
public class JDBCTest {

    // Die folgenden drei Konstanten muessen jeweils angepasst werden:
    private static final String DB_URL = Messages.getString("INP2.0"); //$NON-NLS-1$
    //		Description	Resource	Path	Location	Type
//	Eingabemoeglichkeit schaffen!
    private static final String DB_USER = Messages.getString("INP2.1"); //$NON-NLS-1$
    //		Description	Resource	Path	Location	Type
    //Verdeckte Eingabemoeglichkeit schaffen!
    private static final String DB_PASSWORD = Messages.getString("INP2.2"); //$NON-NLS-1$
    // Ende

    private static final String SURNAME_1 = Messages.getString("INP2.3"); //$NON-NLS-1$
    private static final String NAME_1 = Messages.getString("INP2.4"); //$NON-NLS-1$
    private static final String SURNAME_2 = Messages.getString("INP2.5"); //$NON-NLS-1$
    private static final String NAME_2 = Messages.getString("INP2.6"); //$NON-NLS-1$

    private static TransactionManager transactionManager;
    private static CustomerDAO customerDAO;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        System.out.println("TEST");
        transactionManager = new TransactionManager(DB_URL);
        transactionManager.connect(DB_USER, DB_PASSWORD);
        customerDAO = new CustomerDAOImpl(transactionManager);
    }

    @AfterClass
    public static void tearDownClass() {
        customerDAO = null;
        transactionManager.disconnect();
        transactionManager = null;
    }

    @Before
    public void setUp() throws SQLException {
        transactionManager.executeSQLDeleteOrUpdate(Messages.getString("INP2.7"), //$NON-NLS-1$
                TransactionManager.EMPTY_PARAMETERS);
        transactionManager.commit();
    }

    @Test
    public void testGetNextCustomerId() throws SQLException {
        final long id1 = customerDAO.getNextCustomerId();
        final long id2 = customerDAO.getNextCustomerId();
        assertNotEquals(id1, id2);
    }

    @Test
    public void testCreateCustomer() throws SQLException {
        final long id = createCustomer(SURNAME_1, NAME_1);
        checkIsCustomerOnDB(id, SURNAME_1, NAME_1);
    }

    @Test
    public void testQueryCustomers() throws SQLException {
        final long id1 = createCustomer(SURNAME_1, NAME_1);
        final long id2 = createCustomer(SURNAME_2, NAME_2);
        final List<Customer> customers = customerDAO.selectAllCustomers();
        assertEquals(2, customers.size());
        for (final Customer customer : customers) {
            final long idC = customer.getId();
            if (idC == id1) {
                assertEquals(SURNAME_1, customer.getSurname());
                assertEquals(NAME_1, customer.getName());
            } else if (idC == id2) {
                assertEquals(SURNAME_2, customer.getSurname());
                assertEquals(NAME_2, customer.getName());
            } else {
                fail(Messages.getString("INP2.8") + id1 + Messages.getString("INP2.9") + id2 + Messages.getString("INP2.10") + idC); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }
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

    public long getNextCustomerId() throws SQLException {
        final Object result = transactionManager.executeSQLQuerySingleResult(
                "select CUSTOMERSEQ.NEXTVAL from DUAL",
                TransactionManager.EMPTY_PARAMETERS);
        assert result != null;
        assert result instanceof BigDecimal : "Is: " + result.getClass();
        return ((BigDecimal) result).longValue();
    }


    @Test
    public void testDeleteCustomer() throws SQLException {
        final List<Customer> customers;
        final List<Customer> customers2;
        final List<Customer> customers3;
        Customer rudi;
        createCustomer(SURNAME_1, NAME_1);
        createCustomer(SURNAME_2, NAME_2);
        customers = customerDAO.selectAllCustomers();
        assertEquals(2, customers.size());
        rudi = customers.get(0);
        boolean rc = customerDAO.deleteCustomer(rudi);
        assertTrue(rc);
        customers2 = customerDAO.selectAllCustomers();
        assertEquals(1, customers2.size());
        rc = customerDAO.deleteCustomer(customers2.get(0));
        assertTrue(rc);
        customers3 = customerDAO.selectAllCustomers();
        assertEquals(0, customers3.size());
        rc = customerDAO.deleteCustomer(rudi);
        assertFalse(rc);
    }

    @Test
    public void testUpdateCustomer() throws SQLException {
        final long id = createCustomer(SURNAME_1, NAME_1);
        final List<Customer> customers = customerDAO.selectAllCustomers();
        assertEquals(1, customers.size());
        final Customer customer = customers.get(0);
        customer.setName(NAME_2);
        customer.setSurname(SURNAME_2);
        customerDAO.updateCustomer(customer);
        checkIsCustomerOnDB(id, SURNAME_2, NAME_2);
    }

    private long createCustomer(final String surname, final String name)
            throws SQLException {
        final long id = customerDAO.getNextCustomerId();
        final Customer customer = new Customer();
        customer.setId(id);
        customer.setSurname(surname);
        customer.setName(name);
        customerDAO.insertCustomer(customer);
        return id;
    }

    private void checkIsCustomerOnDB(final long id, final String surname,
                                     final String name) throws SQLException {
        final List<Object> parameters = new ArrayList<Object>();
        parameters.add(new Long(id));
        parameters.add(surname);
        parameters.add(name);
        assertEquals(
                BigDecimal.ONE,
                transactionManager
                        .executeSQLQuerySingleResult(
                                Messages.getString("INP2.11"), //$NON-NLS-1$
                                parameters));
    }

}
