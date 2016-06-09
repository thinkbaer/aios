package de.thinkbaer.aios.jdbc;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.datasource.Connection;
import de.thinkbaer.aios.api.datasource.query.Query;
import de.thinkbaer.aios.api.datasource.query.QueryResults;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.api.exception.Todo;
import de.thinkbaer.aios.jdbc.query.AbstractQueryImpl;


public class ConnectionImpl implements Connection {

	private static final Logger L = LogManager.getLogger(ConnectionImpl.class);

	private java.sql.Connection currentConnection;

	private DataSourceImpl dataSource;

	public void initialize(DataSourceImpl source) throws AiosException {
		this.dataSource = source;
		this.reconnect();
	}

	private boolean isClosed()  {
		try {
			return currentConnection == null || currentConnection.isClosed();
		} catch (SQLException e) {
			return true;
		}
	}

	@Override
	public boolean open() throws AiosException {
		try {
			L.debug("DataSource open connection to " + dataSource.getDataSourceSpec());
			currentConnection = dataSource.getPool().getConnection();			
		} catch (Exception e) {
			throw new AiosException(e);
		}
		return true;
	}

	private void reconnect() throws AiosException {
		close();
		open();
	}

	@Override
	public void close() throws AiosException {
		try {
			boolean isClosed = isClosed();
			if (!isClosed) {

				currentConnection.close();
			}
		} catch (Exception e) {
			L.throwing(e);
			throw new AiosException(e);
		}
	}

	@Override
	public QueryResults query(Query<?> query) throws AiosException {
		if (query instanceof AbstractQueryImpl<?,?>) {
			try {			
				return ((AbstractQueryImpl<?,?>)query).execute(this);
			} catch (Exception e) {
				if(e instanceof AiosException){
					throw (AiosException)e;	
				}
				throw new AiosException(e);
			} 			
		}
		throw new Todo();
	}
	
	
	


	public java.sql.Connection getConnection() throws AiosException {
		boolean isClosed = isClosed();
		if (isClosed) {
			reconnect();
		}
		return currentConnection;
	}

	


}
