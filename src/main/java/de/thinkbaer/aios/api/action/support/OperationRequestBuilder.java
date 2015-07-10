package de.thinkbaer.aios.api.action.support;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.client.ClientImpl;



public abstract class OperationRequestBuilder<Request extends OperationRequest, Response extends OperationResponse, RequestBuilder extends OperationRequestBuilder> {
	private static final Logger L = LogManager.getLogger(OperationRequestBuilder.class);
	protected final Request request;
	
	protected final Class<Response> responseClass;

    protected final ClientImpl client;

    public OperationRequestBuilder(ClientImpl client, Request request, Class<Response> responseClass) {
        this.client = client;
        this.request = request;
        this.responseClass = responseClass;
    }

    
    public Request request(){
    	return request;
    }
    
	public RequestBuilder attr(String string, Object string2) {
		// request.attr(string, string2);
		return (RequestBuilder) this;
	}
	
	public Future<Response> execute(){		  
		return this.client.doGet(this.request, responseClass);
	}


}
