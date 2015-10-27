package de.thinkbaer.aios.server.action;


import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.action.DataSourceRequest;
import de.thinkbaer.aios.api.action.DataSourceResponse;
import de.thinkbaer.aios.api.datasource.DataSource;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.api.transport.ErrorMessage;
import de.thinkbaer.aios.server.annotation.RequestHandler;
import de.thinkbaer.aios.server.datasource.DataSourceManager;

@RequestHandler(request=DataSourceRequest.class,response=DataSourceResponse.class)
public class DataSourceResponseHandler extends OperationResponseHandler<DataSourceRequest, DataSourceResponse, DataSourceResponseHandler> {

	private static final Logger L = LogManager.getLogger( DataSourceResponseHandler.class );
	
	@Inject
	private DataSourceManager manager;

	public void execute() {
		// TODO create new Response through injector
		DataSourceResponse response = new DataSourceResponse();
		response.getSpec().setRid(getRequestId());
		
		if(request().getMethod().contentEquals("register")){
			// TODO create enum for methods
			try {
				DataSource dataSource = manager.register(request().getDataSourceSpec());
				response.setDataSourceSpec(dataSource.getDataSourceSpec());
			} catch (Exception e) {
				L.throwing(e);
				if(e instanceof AiosException){
					response.addError(((AiosException)e).asErrorMessage());	
				}else{
					response.addError(new AiosException(e).asErrorMessage());
				}
				
			}
		}else{
			response.addError(new ErrorMessage("Method not found"));
		}
				
		getChannel().writeAndFlush(response);		
	}

	
}
