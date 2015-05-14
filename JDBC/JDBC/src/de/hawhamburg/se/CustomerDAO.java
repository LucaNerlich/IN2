package de.hawhamburg.se;

import java.sql.SQLException;
import java.util.List;

/**
 * Zugriff auf die Tabelle CUSTOMER.
 */
public interface CustomerDAO {

	/**
	 * Hole die nächste ID für einen Customer aus der Sequenz CUSTOMERSEQ.
	 * 
	 * @return Neue ID.
	 * @throws SQLException
	 */
	long getNextCustomerId() throws SQLException;

	/**
	 * Füge diesen Customer in die Datenbanktabelle CUSTOMER ein.
	 * 
	 * @param customer
	 *            Der einzufügende Customer.
	 * @throws SQLException
	 */
	void insertCustomer(Customer customer) throws SQLException;

	/**
	 * Selektiere alle Customer aus der Datenbanktabelle CUSTOMER.
	 * 
	 * @return Liste alle Customer (evtl. leer).
	 * @throws SQLException
	 */
	List<Customer> selectAllCustomers() throws SQLException;

	/**
	 * Lösche diesen Customer aus der Datenbanktabelle CUSTOMER.
	 * 
	 * @param customer
	 *            Der zu löschende Customer.
	 * @return true, wenn der Customer gelöscht wurde; false, wenn nicht
	 * @throws SQLException
	 */
	boolean deleteCustomer(Customer customer) throws SQLException;

	/**
	 * Ändere diesen Customer auf der Datenbanktabelle CUSTOMER. Die Id wird
	 * nicht geändert.
	 * 
	 * @param customer
	 *            Der zu änderende Customer.
	 * @return true, wenn der Customer geändert wurde; false, wenn nicht
	 * @throws SQLException
	 */
	boolean updateCustomer(Customer customer) throws SQLException;

}