package de.thinkbaer.aios.api.action;

import de.thinkbaer.aios.api.action.support.OperationRequestBuilder;
import de.thinkbaer.aios.client.ClientImpl;


public class PingRequestBuilder extends OperationRequestBuilder<PingRequest, PingResponse, PingRequestBuilder> {

		
	public PingRequestBuilder(ClientImpl client, PingRequest request) {
		super(client, request, PingResponse.class);		
		request.now();
	}

	
	
}
