package de.thinkbaer.aios.server.action;


import io.netty.channel.Channel;
import de.thinkbaer.aios.api.action.support.OperationRequest;
import de.thinkbaer.aios.api.action.support.OperationResponse;



public abstract class OperationResponseHandler<Request extends OperationRequest, Response extends OperationResponse, ResponseBuilder extends OperationResponseHandler> {

	protected Request request;
	
	private int requestId = 0;

    protected Channel channel;


	public void initialize(Channel chc, OperationRequest req) {
        this.channel = chc;
        this.request = (Request)req;
        this.setRequestId(request.getSpec().getRid());        
	}
    
    public Request request(){
    	return request;
    }
    
    public abstract void execute();


	public Request getRequest() {
		return request;
	}


	public Channel getChannel() {
		return channel;
	}


	public int getRequestId() {
		return requestId;
	}


	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
}
