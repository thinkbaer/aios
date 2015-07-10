package de.thinkbaer.aios.server;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import de.thinkbaer.aios.api.action.support.OperationRequest;
import de.thinkbaer.aios.api.action.support.OperationResponse;
import de.thinkbaer.aios.server.action.OperationResponseHandler;
import de.thinkbaer.aios.server.annotation.RequestHandler;



public class ResponseHandlerDispatcherImpl implements ResponseHandlerDispatcher {

	private List<ResponseHandlerDescriptor> descriptors = new ArrayList<ResponseHandlerDispatcherImpl.ResponseHandlerDescriptor>();
	
	@Inject
	private Injector injector;
	
	
	@Inject
	public ResponseHandlerDispatcherImpl(@Named("responseHandler") Set<Class<? extends OperationResponseHandler<?,?,?>>> classes){
		classes.forEach((c) -> register(c));		
	}
	
	@Override
	public OperationResponseHandler<?, ?, ?> inject(ChannelHandlerContext chc, OperationRequest req){
		Class<? extends OperationResponseHandler<?, ?, ?>> cls = getFor(req.getClass());
		OperationResponseHandler<?, ?, ?> handler =  injector.getInstance(cls);
		handler.initialize(chc,req);
		return handler;
	}
	
	
	public Class<? extends OperationResponseHandler<?, ?, ?>> getFor(Class<? extends OperationRequest> cls){
		Iterator<ResponseHandlerDescriptor> irhd = descriptors.iterator();
		while(irhd.hasNext()){
			ResponseHandlerDescriptor rhd = irhd.next();
			if(rhd.getRequestClass().equals(cls)){
				return rhd.getResponseHandlerClass();
			}
		}
		return null;
	}
	
	
	public void register(Class<? extends OperationResponseHandler<?, ?, ?>> c) {		
		RequestHandler rh = c.getAnnotation(RequestHandler.class);
		ResponseHandlerDescriptor rhd = new ResponseHandlerDescriptor(rh.request(), rh.response(), c);
		descriptors.add(rhd);
	}




	static class ResponseHandlerDescriptor {
		private final Class<? extends OperationRequest> requestClass;
		private final Class<? extends OperationResponse> responseClass;
		private final Class<? extends OperationResponseHandler<?,?,?>> responseHandlerClass;
		
		public ResponseHandlerDescriptor(Class<? extends OperationRequest> requestClass,
				Class<? extends OperationResponse> responseClass,
				Class<? extends OperationResponseHandler<?,?,?>> responseHandlerClass
				) {
			this.requestClass = requestClass;
			this.responseClass = responseClass;
			this.responseHandlerClass = responseHandlerClass;
		}
		
		public Class<? extends OperationRequest> getRequestClass() {
			return requestClass;
		}
		public Class<? extends OperationResponse> getResponseClass() {
			return responseClass;
		}
		public Class<? extends OperationResponseHandler<?, ?, ?>> getResponseHandlerClass() {
			return responseHandlerClass;
		}
		
		
		
	}
	
	
}
