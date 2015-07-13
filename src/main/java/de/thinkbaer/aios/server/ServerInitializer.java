package de.thinkbaer.aios.server;

import com.google.inject.Inject;
import com.google.inject.Provider;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import de.thinkbaer.aios.api.channel.AbstractChannelInitializer;

public class ServerInitializer extends AbstractChannelInitializer {
	
	@Inject
	private Provider<TransportChannelInboundHandler> provider;
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();		
		addLogger(pipeline, ServerImpl.class);
		addExchangeCodec(pipeline);
				
		pipeline.addLast(provider.get());		
	}

}
