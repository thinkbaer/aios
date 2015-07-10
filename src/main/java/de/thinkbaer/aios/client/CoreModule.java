package de.thinkbaer.aios.client;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import de.thinkbaer.aios.api.ApiModule;
import de.thinkbaer.aios.api.transport.ByteToTransportDecoder;
import de.thinkbaer.aios.api.transport.ByteToTransportDecoderImpl;
import de.thinkbaer.aios.api.transport.TransportCodecFactory;
import de.thinkbaer.aios.api.transport.TranportToByteEncoder;
import de.thinkbaer.aios.api.transport.TransportToByteEncoderImpl;
import de.thinkbaer.aios.server.TransportChannelInboundHandler;
import de.thinkbaer.aios.server.TransportChannelInboundHandlerImpl;
import de.thinkbaer.aios.server.Server;
import de.thinkbaer.aios.server.ServerImpl;


public class CoreModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ApiModule());
		bind(Client.class).to(ClientImpl.class);
		
		
		
		
		
				
		
		
		
	}

}
