package de.thinkbaer.aios.api;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import org.reflections.Reflections;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import de.thinkbaer.aios.api.annotation.DataSourceType;
import de.thinkbaer.aios.api.annotation.TransportSpec;
import de.thinkbaer.aios.api.datasource.DataSource;
import de.thinkbaer.aios.api.transport.ByteToTransportDecoder;
import de.thinkbaer.aios.api.transport.ByteToTransportDecoderImpl;
import de.thinkbaer.aios.api.transport.TransportCodecFactory;
import de.thinkbaer.aios.api.transport.TransportDispatcher;
import de.thinkbaer.aios.api.transport.TransportDispatcherImpl;
import de.thinkbaer.aios.api.transport.TranportToByteEncoder;
import de.thinkbaer.aios.api.transport.TransportToByteEncoderImpl;
import de.thinkbaer.aios.api.transport.Transport;
import de.thinkbaer.aios.api.utils.GenericObjectFactory;
import de.thinkbaer.aios.api.utils.GenericObjectFactoryImpl;
import de.thinkbaer.aios.server.TransportChannelInboundHandler;
import de.thinkbaer.aios.server.TransportChannelInboundHandlerImpl;

public class ApiModule extends AbstractModule {

	private Set<Class<? extends Transport>> messageTypes = new HashSet<>();
	
	private Set<Class<? extends DataSource>> dataSourceTypes = new HashSet<>();
	
	
	@SuppressWarnings("unchecked")
	public ApiModule() {
		super();
		
		Reflections reflections = new Reflections("de.thinkbaer.aios");
		 
		Set<Class<?>> _messageTypes = reflections.getTypesAnnotatedWith(TransportSpec.class);
		_messageTypes.forEach((c) -> messageTypes.add((Class<? extends Transport>)c));
		

		reflections.getTypesAnnotatedWith(DataSourceType.class).forEach((c) -> dataSourceTypes.add((Class<? extends DataSource>)c));
		
		/*
		annotated.forEach( new Consumer<Class<?>>() {
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void accept(Class t) {
				
				ExchangeDispatcherImpl.registerExchangeClass(t);				
			}
		
		});
		*/
	}
	
	
	@Override
	protected void configure() {
		TypeLiteral<Set<Class<? extends Transport>>> msgType
	       = new TypeLiteral<Set<Class<? extends Transport>>>() {};
		bind(msgType).annotatedWith(Names.named("messageTypes")).toInstance(messageTypes);
		TypeLiteral<Set<Class<? extends DataSource>>> dsType
	       = new TypeLiteral<Set<Class<? extends DataSource>>>() {};
		bind(dsType).annotatedWith(Names.named("dataSourceTypes")).toInstance(dataSourceTypes);

		bind(GenericObjectFactoryImpl.class).in(Singleton.class);
		bind(GenericObjectFactory.class).to(GenericObjectFactoryImpl.class);

		
		bind(TransportDispatcherImpl.class).in(Singleton.class);
		bind(TransportDispatcher.class).to(TransportDispatcherImpl.class);
		
		
		install(new FactoryModuleBuilder()
		.implement(ByteToTransportDecoder.class, ByteToTransportDecoderImpl.class)
		.implement(TranportToByteEncoder.class, TransportToByteEncoderImpl.class)
		.implement(TransportChannelInboundHandler.class, TransportChannelInboundHandlerImpl.class)
		.build(TransportCodecFactory.class));

	}

	
}
