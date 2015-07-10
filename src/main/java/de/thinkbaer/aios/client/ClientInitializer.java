package de.thinkbaer.aios.client;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import de.thinkbaer.aios.api.channel.AbstractChannelInitializer;

public class ClientInitializer extends AbstractChannelInitializer {

	// @Inject
	// private Provider<ClientExchangeChannelInboundHandler> handler;
	
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        addLogger(pipeline, ClientImpl.class);
        addExchangeCodec(pipeline);        
        // pipeline.addLast(handler.get());		
    }
}
