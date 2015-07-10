package de.thinkbaer.aios.api.action;

import de.thinkbaer.aios.api.action.support.OperationRequestBuilder;
import de.thinkbaer.aios.api.datasource.query.Query;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.client.ClientImpl;

public class DataSourceQueryRequestBuilder extends OperationRequestBuilder<DataSourceQueryRequest, DataSourceQueryResponse, DataSourceQueryRequestBuilder>{

	public DataSourceQueryRequestBuilder(ClientImpl client) {
		this(client, new DataSourceQueryRequest());
	}

	public DataSourceQueryRequestBuilder(ClientImpl client, DataSourceQueryRequest request) {
		super(client, request, DataSourceQueryResponse.class);
	}

	public DataSourceQueryRequestBuilder dsn(String dsn) throws AiosException{
		request().setDsn(dsn);
		return this;
	}

	public <X extends Query<?>> X query(Class<X> cls) throws AiosException{
		try {
			request().setQuery(cls.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			throw new AiosException(e);
		}
		return (X)request().getQuery();
	}

	



}
