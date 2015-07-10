package de.thinkbaer.aios.server.action;


import de.thinkbaer.aios.api.action.PingRequest;
import de.thinkbaer.aios.api.action.PingResponse;
import de.thinkbaer.aios.server.action.OperationResponseHandler;
import de.thinkbaer.aios.server.annotation.RequestHandler;

@RequestHandler(request=PingRequest.class,response=PingResponse.class)
public class PingResponseHandler extends OperationResponseHandler<PingRequest, PingResponse, PingResponseHandler> {


	public void execute() {		
		PingResponse response = new PingResponse();
		response.now();
		response.duration(request.getTime());
		response.getSpec().setRid(getRequestId());
		getChannelHandler().writeAndFlush(response);		
	}

	
}
