package de.hawhamburg.se;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionManager {

	private static final Logger LOGGER = Logger
			.getLogger(TransactionManager.class.getName());

	public static final List<Object> EMPTY_PARAMETERS = new ArrayList<Object>();

	public interface ObjectBuilder<T> {
		T buildObjectFromRow(ResultSet rs) throws SQLException;
	}

	private final String dbUrl;

	private Connection connection = null;

	public TransactionManager(final String dbUrl) {
		assert dbUrl != null;
		LOGGER.fine(dbUrl);
		this.dbUrl = dbUrl;
	}

	public void connect(final String user, final String password)
			throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		LOGGER.fine(user);
		connection = DriverManager.getConnection(dbUrl, user, password);
		connection.setAutoCommit(false);
	}

	public void disconnect() {
		LOGGER.entering(TransactionManager.class.getName(), "disconnect");
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (final SQLException e) {
				LOGGER.log(Level.WARNING,
						"Error when closing JDBC connection - ignoring", e);
			} finally {
				connection = null;
			}
		}
		LOGGER.exiting(TransactionManager.class.getName(), "disconnect");
	}

	public void commit() throws SQLException {
		LOGGER.fine("Commit");
		connection.commit();
	}

	public void rollback() throws SQLException {
		LOGGER.fine("Rollback");
		connection.rollback();
	}

	public Object executeSQLQuerySingleResult(final String sqlStmt,
			List<Object> parameters) throws SQLException {
		LOGGER.entering(TransactionManager.class.getName(),
				"executeSQLQuerySingleResult");
		LOGGER.fine(sqlStmt);
		final PreparedStatement pstmt = this.connection
				.prepareStatement(sqlStmt);
		try {
			prepareParameters(pstmt, parameters);
			final ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				return rset.getObject(1);
			} else {
				throw new SQLException("Error when executing: " + sqlStmt);
			}
		} catch (final SQLException e0) {
			LOGGER.log(Level.WARNING, "Error when executing '" + sqlStmt
					+ "' - propagating");
			throw e0;
		} finally {
			try {
				pstmt.close();
			} catch (final SQLException e1) {
				LOGGER.log(Level.WARNING,
						"Error when closing JDBC statement - ignoring", e1);
			}
		}
	}

	public int executeSQLDeleteOrUpdate(final String sqlStmt,
			final List<Object> parameters) throws SQLException {
		final PreparedStatement pstmt = connection.prepareStatement(sqlStmt);
		prepareParameters(pstmt, parameters);
		try {
			return pstmt.executeUpdate();
		} catch (final SQLException e0) {
			LOGGER.log(Level.WARNING, "Error when executing '" + sqlStmt
					+ "' - propagating");
			throw e0;
		} finally {
			try {
				pstmt.close();
			} catch (final SQLException e1) {
				LOGGER.log(Level.WARNING,
						"Error when closing JDBC statement - ignoring", e1);
			}
		}
	}

	public void executeSQLInsert(final String insertStmt,
			List<Object> parameters) throws SQLException {
		final PreparedStatement pstmt = connection.prepareStatement(insertStmt);
		prepareParameters(pstmt, parameters);
		try {
			pstmt.execute();
		} catch (final SQLException e0) {
			LOGGER.log(Level.WARNING, "Error when executing '" + insertStmt
					+ "' - propagating");
			throw e0;
		} finally {
			try {
				pstmt.close();
			} catch (final SQLException e1) {
				LOGGER.log(Level.WARNING,
						"Error when closing JDBC statement - ignoring", e1);
			}
		}
	}

	private void prepareParameters(final PreparedStatement pstmt,
			final List<Object> parameters) throws SQLException {
		int i = 1;
		for (final Object p : parameters) {
			pstmt.setObject(i, p);
			i++;
		}
	}

	public <T> List<T> executeSQLQuery(final String sqlQuery,
			final ObjectBuilder<T> objectBuilder) throws SQLException {
		final List<T> tList = new ArrayList<T>();
		final Statement stmt = this.connection.createStatement();
		try {
			final ResultSet rset = stmt.executeQuery(sqlQuery);
			while (rset.next()) {
				final T t = objectBuilder.buildObjectFromRow(rset);
				tList.add(t);
			}
		} catch (final SQLException e0) {
			LOGGER.log(Level.WARNING, "Error when executing '" + sqlQuery
					+ "' - propagating");
			throw e0;
		} finally {
			try {
				stmt.close();
			} catch (final SQLException e1) {
				LOGGER.log(Level.WARNING,
						"Error when closing JDBC statement - ignoring", e1);
			}
		}
		return tList;
	}
}
