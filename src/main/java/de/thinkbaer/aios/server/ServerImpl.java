package de.thinkbaer.aios.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.server.datasource.DataSourceManager;





@Singleton
public class ServerImpl implements Server, Runnable {

	private static final Logger L = LogManager.getLogger( ServerImpl.class );
	
	private int PORT = Integer.valueOf(System.getProperty("aios.port","8118"));
	private String HOST = System.getProperty("aios.host","localhost");
	private Thread serverThread;
	
	@Inject
	private ServerInitializer initializer;

	@Inject
	private DataSourceManager dataSourceManager;

	EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;
    ServerBootstrap b;
    ChannelFuture channel;
    
	public void start() throws Exception {
		serverThread = new Thread(this);
		serverThread.start();

	}
	
	public void shutdown(){
		try{
		channel.cancel(true);
        // bossGroup.shutdownGracefully();
        // workerGroup.shutdownGracefully();		
		
		}catch(Exception e){
			
		}
		if(serverThread.isAlive()){
			try{
				serverThread.interrupt();
			}catch(Exception e){
				
			}
		}

	}


	public void run() {
		bossGroup = new NioEventLoopGroup(1);
	    workerGroup = new NioEventLoopGroup();
	    b = new ServerBootstrap();
	    b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
	    b.childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024);
	    b.childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024);
	    try {            
	    	L.info("Server started");
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(initializer);
                         
            channel = b.bind(HOST,PORT).sync().channel().closeFuture();
            channel.sync();
        } catch (InterruptedException e) {
        	L.throwing(e);
		} finally {			
			L.info("Shutting down ...");
			try{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
			}catch(Exception e){
				
			}
        }		
	}
}
