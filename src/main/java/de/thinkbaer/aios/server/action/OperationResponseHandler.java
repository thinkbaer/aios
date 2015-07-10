package de.thinkbaer.aios.server.action;



import io.netty.channel.ChannelHandlerContext;
import de.thinkbaer.aios.api.action.support.OperationRequest;
import de.thinkbaer.aios.api.action.support.OperationResponse;




public abstract class OperationResponseHandler<Request extends OperationRequest, Response extends OperationResponse, ResponseBuilder extends OperationResponseHandler> {

	protected Request request;
	
	private int requestId = 0;

    protected ChannelHandlerContext channelHandler;


	public void initialize(ChannelHandlerContext chc, OperationRequest req) {
        this.channelHandler = chc;
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


	public ChannelHandlerContext getChannelHandler() {
		return channelHandler;
	}


	public int getRequestId() {
		return requestId;
	}


	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}


    
    
	
}
