package de.thinkbaer.aios.api.channel;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import javax.inject.Inject;

import de.thinbaer.netty.logger.Log4j2Handler;
import de.thinkbaer.aios.api.transport.ByteToTransportDecoder;
import de.thinkbaer.aios.api.transport.TransportCodecFactory;
import de.thinkbaer.aios.api.transport.TranportToByteEncoder;
import de.thinkbaer.aios.server.ServerImpl;

public abstract class AbstractChannelInitializer extends ChannelInitializer<SocketChannel>{

	private static int maxBytes = 1024*1024*1024; // 1GB
	
	@Inject
	private TransportCodecFactory fExchangeChannel;

	public TransportCodecFactory factory(){
		return fExchangeChannel;
	}
	
	public ByteToTransportDecoder newExchangeDecoder(){
		return fExchangeChannel.newExchangeDecoder();
	}

	public TranportToByteEncoder newExchangeEncoder(){
		return fExchangeChannel.newExchangeEncoder();		
	}


	public void addLogger(ChannelPipeline pipeline, Class<?> cls) throws Exception {
		pipeline.addLast(new Log4j2Handler(cls));
	}
		
	public void addExchangeCodec(ChannelPipeline pipeline) throws Exception {		

		pipeline.addLast(new LengthFieldBasedFrameDecoder(maxBytes, 0, 4, 4, 0));
	    pipeline.addLast("bytesDecoder", newExchangeDecoder());
	    pipeline.addLast("bytesEncoder", newExchangeEncoder());
	    //pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
		
	}


}
