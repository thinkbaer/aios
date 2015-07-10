package de.thinkbaer.aios.server.action;


import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.action.DataSourceQueryRequest;
import de.thinkbaer.aios.api.action.DataSourceQueryResponse;
import de.thinkbaer.aios.api.action.DataSourceRequest;
import de.thinkbaer.aios.api.action.DataSourceResponse;
import de.thinkbaer.aios.api.datasource.Connection;
import de.thinkbaer.aios.api.datasource.DataSource;
import de.thinkbaer.aios.api.datasource.query.QueryResults;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.api.transport.ErrorMessage;
import de.thinkbaer.aios.server.annotation.RequestHandler;
import de.thinkbaer.aios.server.datasource.DataSourceManager;

@RequestHandler(request=DataSourceQueryRequest.class,response=DataSourceQueryResponse.class)
public class DataSourceQueryResponseHandler extends OperationResponseHandler<DataSourceQueryRequest, DataSourceQueryResponse, DataSourceQueryResponseHandler> {

	private static final Logger L = LogManager.getLogger( DataSourceQueryResponseHandler.class );
	
	@Inject
	private DataSourceManager manager;

	public void execute() {
		// TODO create new Response through injector
		DataSourceQueryResponse response = new DataSourceQueryResponse();
		response.getSpec().setRid(getRequestId());
		response.setDsn(request().getDsn());
		
		// get datasource
		DataSource dataSource = manager.get(request().getDsn());
		Connection connection;
		try {
			connection = dataSource.connection();
			QueryResults res = connection.query(request().getQuery());
			response.setResult(res);
			connection.close();
		} catch (AiosException e) {
			L.throwing(e);
			response.addError(e.asErrorMessage());
		}

		getChannelHandler().writeAndFlush(response);		
	}

	
}
