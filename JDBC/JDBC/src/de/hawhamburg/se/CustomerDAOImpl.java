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
			
			Customer customer= new Customer();
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
		long custId = Long.parseLong(transactionManager.executeSQLQuerySingleResult(
				"Select Customerseq.NEXTVAL From DUAL", 
				TransactionManager.EMPTY_PARAMETERS).toString());
		
		return custId;		
	}

	@Override
	public void insertCustomer(final Customer customer) throws SQLException { 
		//Todo: Bitte ausprogrammieren!
		List<Object> ls = new ArrayList<>();	
	
	}

	@Override
	public List<Customer> selectAllCustomers() throws SQLException {
		//Todo: Bitte ausprogrammieren!
		
		List<Customer> ls = new ArrayList<>();	
		return ls;
	}

	@Override
	public boolean deleteCustomer(final Customer customer) throws SQLException {
		//Todo: Bitte ausprogrammieren!		
	
		return false;
	}

	@Override
	public boolean updateCustomer(final Customer customer) throws SQLException {
		//Todo: Bitte ausprogrammieren!
		
		return false;
	}
}
