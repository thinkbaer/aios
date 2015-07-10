package de.thinkbaer.aios.api.action;

import de.thinkbaer.aios.api.action.support.OperationRequest;
import de.thinkbaer.aios.api.annotation.TransportSpec;
import de.thinkbaer.aios.api.datasource.DataSourceSpec;
import de.thinkbaer.aios.api.datasource.query.Query;

@TransportSpec(ns="ds.query")
public class DataSourceQueryRequest extends OperationRequest{
	
	
	private String dsn;
	
	private Query<?> query;

	public String getDsn() {
		return dsn;
	}

	public void setDsn(String ds) {
		this.dsn = ds;
	}

	public Query<?> getQuery() {
		return query;
	}

	public void setQuery(Query<?> query) {
		this.query = query;
	}

	

}
