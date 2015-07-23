package de.thinkbaer.aios.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.action.support.OperationRequest;
import de.thinkbaer.aios.api.transport.Transport;
import de.thinkbaer.aios.server.action.OperationResponseHandler;

public class TransportChannelInboundHandlerImpl extends SimpleChannelInboundHandler<Transport> implements
		TransportChannelInboundHandler {

	// private static final Logger L = LogManager.getLogger(
	// ExchangeChannelInboundHandlerImpl.class );

	private Logger L;


	
	@Inject
	private ResponseHandlerDispatcher dispatcher;

	
	
	public TransportChannelInboundHandlerImpl() {
		super();
		L = LogManager.getLogger("channel.inbound.server");
		
	}
	
	/*
	@Override
	public boolean acceptInboundMessage(Object msg) throws Exception {
		boolean accepted = super.acceptInboundMessage(msg);
		if(accepted){
			if(msg instanceof Transport){
				return true;
			}else if(this.context.contentEquals("client") && msg instanceof Outbound){
				return true;
			}else{
				return false;
			}
		}
		return accepted;
	}*/

	
	protected void channelRead0(ChannelHandlerContext ctx, Transport exchange_2) throws Exception {
		if(exchange_2 instanceof OperationRequest){			
			OperationResponseHandler<?,?, ?> oph =  dispatcher.inject(ctx, (OperationRequest)exchange_2);
			oph.execute();			
			
		}
		
		/*
		
		ExchangeHandler<?> handler = dispatcher.handlerFor(exchange_2, exchange_2.getClass());
		if(handler == null){
			throw new Todo();
		}
		
		handler.prepare();
		
		if (handler instanceof Responsive) {
			// fires response(s)

			Responsive responsiveExchange = (Responsive) handler;
			if (responsiveExchange.hasResponse()) {

				boolean sync = responsiveExchange.isSync();

				if (handler instanceof MultiResponsive) {
					MultiResponsive multiResponsiveExchange = (MultiResponsive) handler;

					Iterator<Exchange> responses = multiResponsiveExchange.iterator();

					// fires multiple response
					while (responses.hasNext()) {
						Exchange response = responses.next();
						handleResponse(ctx, response, sync);
					}
				} else {
					Exchange response = responsiveExchange.response();
					handleResponse(ctx, response, sync);
				}
			}
		}
		
		handler.finished();
		*/
	}

	private void handleResponse(ChannelHandlerContext ctx, Transport response, boolean sync) throws InterruptedException {
		ChannelFuture writeFuture = ctx.write(response);
		if (sync) {
			writeFuture.sync();
		}
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		L.throwing(cause);
		L.debug("Channel closed");
		ctx.close();
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		// Once session is secured, send a greeting and register the channel to
		// the global channel
		// list so the channel received the messages from others.
		L.debug("Channel active");

	}

}
