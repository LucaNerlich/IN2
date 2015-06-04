package de.hawhamburg.se;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class JPAQueryTest {
    private static final String DB_URL = Messages.getString("INP2.0"); //$NON-NLS-1$
    private static final String DB_USER = getUsername();
    private static final String DB_PASSWORD = getPassword();
    // Ende

    private static final Logger LOG = LoggerFactory
            .getLogger(JPAQueryTest.class);
    //Add more, if you need or like.
    private static final String SURNAME_1 = Messages.getString("INP2.3"); //$NON-NLS-1$
    private static final String NAME_1 = Messages.getString("INP2.4"); //$NON-NLS-1$
    private static final String SURNAME_2 = Messages.getString("INP2.5"); //$NON-NLS-1$
    private static final String NAME_2 = Messages.getString("INP2.6"); //$NON-NLS-1$

    private static TransactionManager transactionManager;

    private EntityManagerFactory emf = null;
    private EntityManagerFactory emf2 = null;
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
                emf2.close();
            } catch (final Throwable ex) {
                LOG.warn("while closing entity manager factory", ex);
            } finally {
                emf = null;
                emf2 = null;
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

    private void createEntityManagers() {
        emf = Persistence.createEntityManagerFactory("haw_demo");
        emf2 = Persistence.createEntityManagerFactory("haw_demo");
        em1 = emf.createEntityManager();
        em2 = emf2.createEntityManager();
    }

    private static final class CustomerObjectBuilder implements
            TransactionManager.ObjectBuilder<Customer> {

        @Override
        public Customer buildObjectFromRow(final ResultSet rs)
                throws SQLException {

            Customer customer = new Customer();
            customer.setId(rs.getLong("ID"));
            customer.setName(rs.getString("NAME"));
            customer.setSurname(rs.getString("SURNAME"));

            return customer;
        }
    }

    @Test
    public void testEntityManagers() throws SQLException {
        createEntityManagers();
        em1.getTransaction().begin();
        em2.getTransaction().begin();
        em1.getTransaction().commit();
        em2.getTransaction().commit();
    }

    @Test
    public void testInsert() {
        try {
            createEntityManagers();
            final Customer konrad = new Customer(Messages.getString("INP5.0"), Messages.getString("INP5.1"));
            Address addressKonrad = new Address("20099", "Hamburg", "Berliner Tor");
            konrad.setHomeAddress(addressKonrad);

            CardIssuer visa = new CardIssuer("VISA");
            CardIssuer mastercard = new CardIssuer("MASTERCARD");

            Card cardVisa = new Card("2015", CardType.CREDIT,konrad, visa);
            Card cardMaster = new Card("5102", CardType.DEBIT,konrad, mastercard);

            konrad.addCreditCard(cardVisa);
            konrad.addCreditCard(cardMaster);

            em1.getTransaction().begin();
            em1.persist(konrad);
            em1.getTransaction().commit();

            assertTrue(isCustomerOnDB(konrad.getId(), konrad.getSurname(), konrad.getName()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        return JOptionPane
                .showInputDialog("Enter Username");
    }

    private static String getPassword() {
    /* Passwort abfragen */

        JPasswordField passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*');
        JOptionPane.showMessageDialog(null, passwordField,
                "Enter password", JOptionPane.OK_OPTION);
        char[] pw = passwordField.getPassword();

        return String.valueOf(pw);
    }
}