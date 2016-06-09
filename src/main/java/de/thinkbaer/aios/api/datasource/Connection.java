package de.thinkbaer.aios.api.datasource;



import de.thinkbaer.aios.api.datasource.query.Query;
import de.thinkbaer.aios.api.datasource.query.QueryResults;
import de.thinkbaer.aios.api.exception.AiosException;


public interface Connection {

	public void close() throws AiosException;

	public boolean open() throws AiosException;

	public QueryResults query(Query<?> query) throws AiosException;
}
