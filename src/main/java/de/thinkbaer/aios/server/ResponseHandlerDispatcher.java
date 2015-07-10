package de.thinkbaer.aios.server;

import io.netty.channel.ChannelHandlerContext;
import de.thinkbaer.aios.api.action.support.OperationRequest;

import de.thinkbaer.aios.server.action.OperationResponseHandler;

public interface ResponseHandlerDispatcher {

	
	public Class<? extends OperationResponseHandler<?, ?, ?>> getFor(Class<? extends OperationRequest> cls);

	public OperationResponseHandler<?, ?, ?> inject(ChannelHandlerContext chc, OperationRequest req);

}
