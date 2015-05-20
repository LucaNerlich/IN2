package de.hawhamburg.se;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    private final TransactionManager transactionManager;

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

    public CustomerDAOImpl(final TransactionManager transactionManager) {
        assert transactionManager != null;
        this.transactionManager = transactionManager;
    }

    @Override
    public long getNextCustomerId() throws SQLException {

        return Long.parseLong(transactionManager.executeSQLQuerySingleResult(
                "Select Customerseq.NEXTVAL From DUAL",
                TransactionManager.EMPTY_PARAMETERS).toString());
    }

    @Override
    public void insertCustomer(final Customer customer) throws SQLException {
        //Todo: Bitte ausprogrammieren!

        List<Object> ls = new ArrayList<>();

        ls.add(customer.getId());
        ls.add(customer.getSurname());
        ls.add(customer.getName());

        String stmt = Messages.getString("INP3.0");

        transactionManager.executeSQLInsert(stmt, ls);
        transactionManager.commit();
    }

    @Override
    public List<Customer> selectAllCustomers() throws SQLException {
        //Todo: Bitte ausprogrammieren!

        TransactionManager.ObjectBuilder objectBuilder = new CustomerObjectBuilder();

        return transactionManager.executeSQLQuery(Messages.getString("INP3.1"), objectBuilder);
    }

    @Override
    public boolean deleteCustomer(final Customer customer) throws SQLException {
        //Todo: Bitte ausprogrammieren!

        boolean deleted = false;
        List<Object> parameters = new ArrayList<>();
        parameters.add(customer.getId());

        int rows = transactionManager.executeSQLDeleteOrUpdate(Messages.getString("INP3.2"), parameters);
        transactionManager.commit();

        if(rows > 0){
            deleted = true;
        }

        return deleted;
    }

    @Override
    public boolean updateCustomer(final Customer customer) throws SQLException {
        //Todo: Bitte ausprogrammieren!

        boolean updated = false;
        List<Object> parameters = new ArrayList<>();

        parameters.add(customer.getSurname());
        parameters.add(customer.getName());
        parameters.add(customer.getId());

        int rows = transactionManager.executeSQLDeleteOrUpdate(Messages.getString("INP3.3"), parameters);
        transactionManager.commit();

        if(rows > 0){
            updated = true;
        }

        return updated;
    }
}
