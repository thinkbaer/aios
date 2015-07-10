package de.thinkbaer.aios.api.transport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Injector;

import de.thinkbaer.aios.api.action.support.OperationRequest;
import de.thinkbaer.aios.api.action.support.OperationResponse;
import de.thinkbaer.aios.api.annotation.TransportSpec;

public class TransportDispatcherImpl implements TransportDispatcher {
	
	private static final Logger L = LogManager.getLogger( TransportDispatcherImpl.class );

	@Inject
	private Injector injector;

	private List<TransportDescriptor> descriptors = new ArrayList<>();
	
	@Inject
	public TransportDispatcherImpl(@Named("messageTypes") Set<Class<? extends Transport>> messageTypes){
		messageTypes.forEach((c) -> register(c));
	}
	
	public TransportDescriptor getByNS(String ns){
		Iterator<TransportDescriptor> itd = descriptors.iterator();
		while(itd.hasNext()){
			TransportDescriptor td = itd.next();
			if(td.getNs().contentEquals(ns)){
				return td;
			}
		}
		return null;
		
	}

	public void register(Class<? extends Transport> t) {
		TransportSpec desc = t.getAnnotation(TransportSpec.class);
		TransportDescriptor td = getByNS(desc.ns());
		if(td == null){
			td = new TransportDescriptor();
			td.setNs(desc.ns());
			descriptors.add(td);
		}

		if(OperationRequest.class.isAssignableFrom(t)){
			td.setRequestClass((Class<? extends OperationRequest>)t);
		}else if(OperationResponse.class.isAssignableFrom(t)){
			td.setResponseClass((Class<? extends OperationResponse>)t);
		}else{
				throw new RuntimeException("TODO!");
		}
		
		L.debug("Register type [" + desc.ns() + "] => " + t.getName());		
	}

	
	public de.thinkbaer.aios.api.transport.TransportSpec specOf(Transport msg) {		
		return msg.getSpec();
	}
	
	
	public Class<? extends Transport> classFor(de.thinkbaer.aios.api.transport.TransportSpec transport) {
		TransportDescriptor td = getByNS(transport.getNs());
		if(transport.isReq()){
			return td.getRequestClass();
		}else{
			return td.getResponseClass();
		}		
	}

	
	static class TransportDescriptor {
		
		private String ns;
		
		private Class<? extends OperationRequest> requestClass;
		
		private Class<? extends OperationResponse> responseClass;

		public String getNs() {
			return ns;
		}

		public void setNs(String ns) {
			this.ns = ns;
		}

		public Class<? extends OperationRequest> getRequestClass() {
			return requestClass;
		}

		public void setRequestClass(Class<? extends OperationRequest> requestClass) {
			this.requestClass = requestClass;
		}

		public Class<? extends OperationResponse> getResponseClass() {
			return responseClass;
		}

		public void setResponseClass(Class<? extends OperationResponse> responseClass) {
			this.responseClass = responseClass;
		}
	}

}
