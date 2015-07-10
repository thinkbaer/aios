package de.thinkbaer.aios.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import de.thinkbaer.aios.api.action.support.OperationRequest;
import de.thinkbaer.aios.api.action.support.OperationResponse;
import de.thinkbaer.aios.api.exception.AiosException;


public class ClientImpl implements Client{
	private static final Logger L = LogManager.getLogger(ClientImpl.class);
	
	static final String HOST = System.getProperty("host", "127.0.0.1");
	static final int PORT = Integer.parseInt(System.getProperty("port", "8118"));

	
	ExecutorService service = Executors.newCachedThreadPool();
	
	@Inject
	private ClientInitializer initializer;

	EventLoopGroup group;
	Bootstrap b;
	Channel ch;
	ChannelFuture lastWriteFuture = null;

	private ListenableFuture<OperationResponse> submit;
	
	public void connect() {
		try {
			L.debug("Connecting ...");
			group = new NioEventLoopGroup();
			b = new Bootstrap();
			b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			b.option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024);
			b.option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024);
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(initializer);

			// Start the connection attempt.
			ChannelFuture chf = b.connect(HOST, PORT).sync();			
			
			chf.await(5,TimeUnit.SECONDS);
			ch = chf.channel();
						
			L.debug("Connected");
		} catch (Exception e) {
			L.throwing(e);
			close();
		}
	}
	
	/*
	public PingResponse ping(){
		PingRequest p = new PingRequest();
		p.now();
		PingResponse result = sendAndWait(p, PingResponse.class);
		L.debug("Result!! " + result);
		return result;
	}*/
	
	@SuppressWarnings("unchecked")
	public <Request extends OperationRequest, Response extends OperationResponse> Future<Response> doGet(Request request, Class<Response> responseType){
		ResponseCallback<Request, Response> call = new ResponseCallback<Request, Response>(ch, request,responseType);
		return service.submit(call);
				
	}
	
	static class ResponseCallback<Request extends OperationRequest, Response extends OperationResponse> extends ChannelInboundHandlerAdapter implements Callable<Response>{

		private final Channel channel;
		
		private final Request request;
		
		private final Class<Response> responseClass;
		

		private Response response;
		
		private CountDownLatch latch = new CountDownLatch(1);
		
		
		public ResponseCallback(final Channel channel, final Request request, final Class<Response> responseClass) {
			super();
			this.channel = channel;
			this.request = request;
			this.responseClass = responseClass;
		}
		
	    public boolean acceptInboundMessage(Object msg) throws Exception {
	    	if(this.responseClass.isInstance(msg)){
	    		Response resMsg = ((Response)msg);
	    		if(resMsg.getSpec().getRid() == request.getSpec().getRid()){
	    			return true;
	    		}else{
	    			L.debug("Response ID is wrong! "+resMsg.getSpec().getRid() +"=="+ request.getSpec().getRid());
	    		}
	    	}else{
	    		L.debug("Message type is wrong: " + msg.getClass());
	    	}
	    	return false;
	    }
		
		/*
		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if(evt instanceof ResponseEvent && (responseClass.isInstance(((ResponseEvent) evt).get()))){
				L.debug("response event");
				result = (X)((ResponseEvent) evt).get();
				latch.countDown();				
			}else{
				super.userEventTriggered(ctx, evt);	
			}
			
		}
*/
		
		
		@Override
		public Response call() throws Exception {
			this.channel.pipeline().addLast(this);
			ChannelFuture future = channel.writeAndFlush(request);			

			try {
				future.sync();			
			} catch (InterruptedException e) {
				L.throwing(e);
			}
			if (!future.isSuccess()) {
			    L.throwing(future.cause());
			    throw new AiosException(future.cause().getLocalizedMessage(), future.cause());
			}else{
				L.debug("Successful transmitted");				
			}

			latch.await(10, TimeUnit.SECONDS);
			
			this.channel.pipeline().remove(this);
			return this.response;
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			this.response = (Response)msg;
			latch.countDown();
		}
		
	}
	


	public void close() {
		L.debug("Close client");
		try {
			if(ch != null){
				// ch.closeFuture().sync();	
			}			
			if (lastWriteFuture != null) {
				lastWriteFuture.sync();
			}
		} catch (InterruptedException e) {
		} finally {
			if(group != null){
				group.shutdownGracefully();	
			}
			
		}
	}
	

	
	



	public void setService(ListeningExecutorService service) {
		this.service = service;
	}


	/*
	public DataSourceRequestBuilder dataSource() {
		DataSourceRequest request = new DataSourceRequest();
		DataSourceRequestBuilder dsrb = new DataSourceRequestBuilder(this, request);
		return dsrb;
	}*/

	
}
