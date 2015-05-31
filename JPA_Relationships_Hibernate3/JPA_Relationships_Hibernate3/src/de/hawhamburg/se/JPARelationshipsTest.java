package de.hawhamburg.se;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Vor dem Test muss die Datenbank mit dem Skript sql/createdb.sql erzeugt
 * worden sein.
 */
public class JPARelationshipsTest {

    // Die folgenden drei Konstanten müssen jeweils angepasst werden:
    private static final String DB_URL = "jdbc:oracle:thin:@//ora14.informatik.haw-hamburg.de:1521/inf14.informatik.haw-hamburg.de";
    private static final String DB_USER = getUsername();
    private static final String DB_PASSWORD = getPassword();
    // Ende

    private static final Logger LOG = LoggerFactory
            .getLogger(JPARelationshipsTest.class);

    private static final String SURNAME_1 = Messages.getString("INP2.3"); // Furchtlos
    private static final String NAME_1 = Messages.getString("INP2.4"); // Felix
    private static final String SURNAME_2 = Messages.getString("INP2.5"); // Ratlos
    private static final String NAME_2 = Messages.getString("INP2.6"); // Rudi

    private static final String STREET_1 = Messages.getString("INP4.0"); // Murmelgasse

    private static final String CREDITCARDNO_1 = Messages.getString("INP4.1");
    private static final String CREDITCARDNO_2 = Messages.getString("INP4.2");

    private static final String BANK_1 = Messages.getString("INP4.3");
    private static final String BANK_2 = Messages.getString("INP4.4");

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
    public void testCreateAddress() throws SQLException {
        createEntityManager();
        em.getTransaction().begin();
        final Address address = new Address(STREET_1);
        LOG.info("Address ID before persist(): " + address.getId());
        em.persist(address);
        LOG.info("Address ID after persist(): " + address.getId());
        em.getTransaction().commit();
    }

    @Test
    public void testCustomerCreditCardBank() throws SQLException {
        createEntityManager();
        em.getTransaction().begin();
        final Customer customer = new Customer(SURNAME_1, NAME_1);
        final Address address = new Address(STREET_1);
        final Bank bank = new Bank(BANK_1);
        final CreditCard creditCard = new CreditCard(CREDITCARDNO_1);

        creditCard.setHolder(customer);
        customer.addCreditCard(creditCard);
        customer.addBank(bank);

        customer.setHomeAddress(address);
        LOG.info("Customer ID before persist(): " + customer.getId());
        em.persist(customer);
        em.persist(address);
        em.persist(creditCard);
        em.persist(bank);
        LOG.info("Customer ID after persist(): " + customer.getId());
        em.getTransaction().commit();

        assertTrue(isCustomerOnDB(customer.getId(), SURNAME_1, NAME_1));

        Customer customerFromDB = em.find(Customer.class, customer.getId());

        Set<CreditCard> creditcardsFromDB = customerFromDB.getCreditCards();
        assertEquals(creditcardsFromDB.size(), 1);

        Set<Bank> banksFromDB = customerFromDB.getBanks();
        assertEquals("Banks in customer not saved", banksFromDB.size(),1);
    }

    @Test
    public void testCreateBank() throws SQLException {
        createEntityManager();
        em.getTransaction().begin();
        final Bank bank = new Bank(BANK_1);
        LOG.info("Bank ID before persist(): " + bank.getId());
        em.persist(bank);
        LOG.info("Bank ID after persist(): " + bank.getId());
        em.getTransaction().commit();

        assertTrue(isBankOnDB(bank.getId(), BANK_1));
    }

    @Test
    public void testCreateCreditCard() throws SQLException {
        createEntityManager();
        em.getTransaction().begin();
        final CreditCard creditCard = new CreditCard(CREDITCARDNO_1);
        final Customer customer = new Customer(SURNAME_1, NAME_1);
        final Address address = new Address(STREET_1);
        customer.setHomeAddress(address);
        creditCard.setHolder(customer);

        LOG.info("CreditCard ID before persist(): " + creditCard.getId());
        em.persist(customer);
        em.persist(address);
        em.persist(creditCard);
        LOG.info("CreditCard ID after persist(): " + creditCard.getId());
        em.getTransaction().commit();

        assertTrue(isCreditCardOnDB(creditCard.getId(), CREDITCARDNO_1));
    }


    public void testInserts() {
        insertInDB();
    }

    public void insertInDB() {

        final Customer rudi = new Customer(SURNAME_2, NAME_2);
        final Address addressRudi = new Address(STREET_1);
        final Bank bankHaspa = new Bank(BANK_1);
        final Bank bankDB = new Bank(BANK_2);

        createEntityManager();
        em.getTransaction().begin();
        // rudi.setId(getNextCustomerId());

        rudi.setHomeAddress(addressRudi);

        rudi.addBank(bankHaspa);
        rudi.addBank(bankDB);

        em.persist(rudi);
        em.getTransaction().commit();

    }


    private boolean isCustomerOnDB(final long id, final String surname,
                                   final String name) throws SQLException {
        final List<Object> parameters = new ArrayList<Object>();
        parameters.add(id);
        parameters.add(surname);
        parameters.add(name);
        return BigDecimal.ONE
                .equals(transactionManager
                        .executeSQLQuerySingleResult(
                                "select count(*) from CUSTOMER where ID = ? and SURNAME = ? and NAME = ?",
                                parameters));
    }

    private boolean isBankOnDB(final long id, final String name)
            throws SQLException {
        final List<Object> parameters = new ArrayList<Object>();
        parameters.add(new Long(id));
        parameters.add(name);
        return BigDecimal.ONE.equals(transactionManager
                .executeSQLQuerySingleResult(
                        "select count(*) from BANK where ID = ? and NAME = ?",
                        parameters));
    }

    private boolean isCreditCardOnDB(final long id, final String number)
            throws SQLException {
        final List<Object> parameters = new ArrayList<Object>();
        parameters.add(new Long(id));
        parameters.add(number);
        return BigDecimal.ONE
                .equals(transactionManager
                        .executeSQLQuerySingleResult(
                                "select count(*) from CREDITCARD where ID = ? and CCNUMBER = ?",
                                parameters));
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
