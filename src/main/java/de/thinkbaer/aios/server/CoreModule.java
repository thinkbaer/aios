package de.thinkbaer.aios.server;

import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import de.thinkbaer.aios.api.ApiModule;
import de.thinkbaer.aios.jdbc.DriverManager;
import de.thinkbaer.aios.jdbc.DriverManagerImpl;
import de.thinkbaer.aios.server.action.OperationResponseHandler;
import de.thinkbaer.aios.server.annotation.RequestHandler;
import de.thinkbaer.aios.server.datasource.DataSourceManager;
import de.thinkbaer.aios.server.datasource.DataSourceManagerImpl;

public class CoreModule extends AbstractModule {

	private Set<Class<? extends OperationResponseHandler<?, ?, ?>>> handlers = new HashSet<>();

	@SuppressWarnings("unchecked")
	public CoreModule() {
		super();

		Reflections reflections = new Reflections("de.thinkbaer.aios");

		Set<Class<?>> _messageTypes = reflections.getTypesAnnotatedWith(RequestHandler.class);
		_messageTypes.forEach((c) -> handlers.add((Class<? extends OperationResponseHandler<?, ?, ?>>) c));

	}

	@Override
	protected void configure() {
		install(new ApiModule());

		TypeLiteral<Set<Class<? extends OperationResponseHandler<?, ?, ?>>>> msgType = new TypeLiteral<Set<Class<? extends OperationResponseHandler<?, ?, ?>>>>() {
		};
		bind(msgType).annotatedWith(Names.named("responseHandler")).toInstance(handlers);

		bind(ServerImpl.class).in(Singleton.class);
		bind(Server.class).to(ServerImpl.class);

		bind(DataSourceManagerImpl.class).in(Singleton.class);
		bind(DataSourceManager.class).to(DataSourceManagerImpl.class);

		bind(ResponseHandlerDispatcherImpl.class).in(Singleton.class);
		bind(ResponseHandlerDispatcher.class).to(ResponseHandlerDispatcherImpl.class);

		bind(TransportChannelInboundHandler.class).to(TransportChannelInboundHandlerImpl.class);
		
		// JDBC Stuff
		
		bind(DriverManagerImpl.class).in(Singleton.class);
		bind(DriverManager.class).to(DriverManagerImpl.class);

	}

}
