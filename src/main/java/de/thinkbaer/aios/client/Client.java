package de.thinkbaer.aios.client;

import java.util.concurrent.Future;

import com.google.common.util.concurrent.ListenableFuture;

import de.thinkbaer.aios.api.action.support.OperationRequest;
import de.thinkbaer.aios.api.action.support.OperationResponse;


public interface Client {
	public void connect();
	public void close();
	// public PingResponse ping();
	
	// public DataSourceRequestBuilder dataSource();
	
	public <Request extends OperationRequest, Response extends OperationResponse> Future<Response> doGet(Request request, Class<Response> responseType);
}
