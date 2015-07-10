package de.thinkbaer.aios.api.action;

import de.thinkbaer.aios.api.action.support.OperationResponse;
import de.thinkbaer.aios.api.annotation.TransportSpec;
import de.thinkbaer.aios.api.datasource.DataSourceSpec;
import de.thinkbaer.aios.api.datasource.query.QueryResults;

@TransportSpec(ns="ds.query")
public class DataSourceQueryResponse extends OperationResponse {

	private String dsn;
	
	private QueryResults result;

	public String getDsn() {
		return dsn;
	}

	public void setDsn(String dsn) {
		this.dsn = dsn;
	}

	public QueryResults getResult() {
		return result;
	}

	public void setResult(QueryResults result) {
		this.result = result;
	}

	

}
