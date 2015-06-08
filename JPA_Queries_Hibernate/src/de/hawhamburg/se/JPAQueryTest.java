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
import java.util.Set;

import static org.junit.Assert.assertTrue;

@NamedQueries({
        @NamedQuery(name ="x", query="x"),
        @NamedQuery(name ="y", query="y")
})
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

    @Before
    public void setUp() throws SQLException {
        transactionManager.executeSQLDeleteOrUpdate("delete from ADDRESS",
                TransactionManager.EMPTY_PARAMETERS);
        transactionManager.executeSQLDeleteOrUpdate("delete from CARDISSUER",
                TransactionManager.EMPTY_PARAMETERS);
        transactionManager.executeSQLDeleteOrUpdate("delete from CARD",
                TransactionManager.EMPTY_PARAMETERS);
        transactionManager.executeSQLDeleteOrUpdate("delete from BANK",
                TransactionManager.EMPTY_PARAMETERS);
        transactionManager.executeSQLDeleteOrUpdate("delete from bank_customer",
                TransactionManager.EMPTY_PARAMETERS);
        transactionManager.executeSQLDeleteOrUpdate("delete from office_address",
                TransactionManager.EMPTY_PARAMETERS);
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

    // @Test
    public void testEntityManagers() throws SQLException {
        createEntityManagers();
        em1.getTransaction().begin();
        em2.getTransaction().begin();
        em1.getTransaction().commit();
        em2.getTransaction().commit();
    }

    // Finden aller Kunden, die einen bestimmten Kartentyp haben (CardType). Ausgabe von Name und Kartentyp.
    @Test
    public void testFindbyCardType() {
        insertDataIntoDB();
        createEntityManagers();
        em1.getTransaction().begin();

        try {
            final List<Customer> customers = selectAllCustomers();

            for (int i = 0; i < customers.size(); i++) {

                Customer konrad = em1.find(Customer.class, customers.get(i).getId());
                Set<Card> creditCards = konrad.getCreditCards();

                for (Card card : creditCards) {
                    if (card.getType() == CardType.CREDIT) {
                        LOG.info(konrad.getName() + " " + konrad.getSurname() + " = CREDITCARD");
                    }
                    if (card.getType() == CardType.DEBIT) {
                        LOG.info(konrad.getName() + " " + konrad.getSurname() + " = DEBITCARD");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        em1.getTransaction().commit();
    }

    @Test
    public void testFindbyCardTypeQuery() {
        insertDataIntoDB();
        createEntityManagers();
        em1.getTransaction().begin();


        // String queryString = "SELECT Name, Surname, CCNumber FROM Customer, Card, WHERE Card.holder_id = Customer.id and cardtype = ?1";
        // Select Name, Surname, CCNumber From customer, Card, where card.holder_id=customer.id and cardtype like 'credit';

       // javax.persistence.Query query = em1.createNativeQuery(queryString);
        // query.setParameter(1, "CREDIT");

        //query:
        List<Object> list = findWithName("CREDIT");
        System.out.println("xx");

        //named query:

        em1.getTransaction().commit();
    }

    public List findWithName(String type) {
        return em1.createNativeQuery(
                "SELECT cu.surname, ca.cardtype FROM Customer cu, Card ca Where cu.id like ca.holder_id and ca.cardtype LIKE :type")
                .setParameter("type", type)
                .getResultList();
    }

    @Test
    public void testCustomerAndBank() {
        insertDataIntoDB();
        createEntityManagers();
        em1.getTransaction().begin();
        int counterCredit = 0;
        int counterDebit = 0;
        try {
            final List<Customer> customers = selectAllCustomers();
            for (int i = 0; i < customers.size(); i++) {

                Customer customer = em1.find(Customer.class, customers.get(i).getId());
                Set<Bank> banks = customer.getBanks();

                for (Bank bank : banks) {
                    for (Address address : bank.getOffices()) {
                        LOG.info(customer.getName() + " " + customer.getSurname() + " Bank: " + address.toString());
                    }
                    bank.getOffices().forEach(System.err::println);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        em1.getTransaction().commit();
    }

    @Test
    public void testCustomerAndBankQuery() {
        insertDataIntoDB();
        createEntityManagers();
        em1.getTransaction().begin();

        em1.getTransaction().commit();
    }

    @Test
    public void testCustomerCityBanks() {
        insertDataIntoDB();
        createEntityManagers();
        em1.getTransaction().begin();
        int counterCredit = 0;
        int counterDebit = 0;
        try {
            final List<Customer> customers = selectAllCustomers();

            for (int i = 0; i < customers.size(); i++) {

                Customer customer = em1.find(Customer.class, customers.get(i).getId());
                Set<Bank> banks = customer.getBanks();
                String city = customer.getHomeAddress().getCity();

                for (Bank bank : banks) {
                    for (Address address : bank.getOffices()) {
                        if (address.getCity().equals(city)) {
                            LOG.info(customer.getName() + " " + customer.getSurname() + " Bank: " + address.toString());
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        em1.getTransaction().commit();
    }

    @Test
    public void testCustomerCityBanksQuery() {
        insertDataIntoDB();
        createEntityManagers();
        em1.getTransaction().begin();

        em1.getTransaction().commit();
    }

    @Test
    public void testCustomerAndCards() {
        insertDataIntoDB();
        createEntityManagers();
        em1.getTransaction().begin();
        int counterCredit = 0;
        int counterDebit = 0;
        try {
            final List<Customer> customers = selectAllCustomers();

            for (int i = 0; i < customers.size(); i++) {

                Customer customer = em1.find(Customer.class, customers.get(i).getId());
                Set<Card> creditCards = customer.getCreditCards();

                for (Card card : creditCards) {
                    LOG.info(customer.getName() + " " + customer.getSurname() + " Card: " + card.toString());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        em1.getTransaction().commit();
    }

    @Test
    public void testCustomerAndCardsQuery() {
        insertDataIntoDB();
        createEntityManagers();
        em1.getTransaction().begin();

        em1.getTransaction().commit();
    }

    @Test
    public void testInsert() {
        insertDataIntoDB();
    }

    private void insertDataIntoDB() {
        try {
            createEntityManagers();

            Bank haspa = new Bank("HASPA");
            Address addressHaspa = new Address("11111", "Hamburg", "Weg 1");
            Address addressHaspa2 = new Address("33333", "Hamburg", "Weg 2");
            haspa.addOfficeByAddress(addressHaspa);
            haspa.addOfficeByAddress(addressHaspa2);

            em1.getTransaction().begin();
            em1.persist(haspa);
            em1.getTransaction().commit();

            Bank deutscheBank = new Bank("Deutsche Bank");
            Address addressDeutscheBank = new Address("22222", "Bremen", "Highway 3000");
            deutscheBank.addOfficeByAddress(addressDeutscheBank);

            em1.getTransaction().begin();
            em1.persist(haspa);
            em1.getTransaction().commit();


            Customer konrad = new Customer(Messages.getString("INP5.0"), Messages.getString("INP5.1"));
            Address addressKonrad = new Address("20099", "Hamburg", "Berliner Tor");
            konrad.setHomeAddress(addressKonrad);

            CardIssuer visa = new CardIssuer("VISA");
            CardIssuer mastercard = new CardIssuer("MASTERCARD");

            Card cardVisa = new Card("2015", CardType.CREDIT, konrad, visa);
            Card cardVisa2 = new Card("20152", CardType.CREDIT, konrad, visa);
            Card cardMaster = new Card("5102", CardType.DEBIT, konrad, mastercard);

            konrad.addCreditCard(cardVisa);
            konrad.addCreditCard(cardVisa2);
            konrad.addCreditCard(cardMaster);

            konrad.addBank(haspa);
            konrad.addBank(deutscheBank);

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

    public List<Customer> selectAllCustomers() throws SQLException {
        TransactionManager.ObjectBuilder objectBuilder = new CustomerObjectBuilder();

        return transactionManager.executeSQLQuery(Messages.getString("INP3.1"), objectBuilder);
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
}