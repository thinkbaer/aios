package de.thinkbaer.aios.tests.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import de.thinkbaer.aios.api.action.PingRequest;
import de.thinkbaer.aios.api.action.PingRequestBuilder;
import de.thinkbaer.aios.api.action.PingResponse;
import de.thinkbaer.aios.client.Client;
import de.thinkbaer.aios.client.ClientImpl;
import de.thinkbaer.aios.server.CoreModule;
import de.thinkbaer.aios.server.Server;

public class ConnectionTests {


	private static final Logger L = LogManager.getLogger( ConnectionTests.class );
	
	private static Server server;
	
	public static Client getClient(){
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new de.thinkbaer.aios.client.CoreModule());
		Injector injector = Guice.createInjector(modules);
		return injector.getInstance(Client.class);		
	}
	
	@Test
	public void openAndCloseConnection(){
		Client client = getClient();
		
		L.info("Test start !!!");
		
		client.connect();
		PingRequestBuilder builder = new PingRequestBuilder((ClientImpl) client, new PingRequest());
		PingResponse response;
		try {
			response = builder.execute().get();
			L.info("Ping: " + response.getTime() + " => " + response.getDuration());
		} catch (InterruptedException | ExecutionException e) {
			L.throwing(e);
		}	
		
		client.close();

		L.info("Test done");
	}


	@BeforeClass
	public static void start(){
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new CoreModule());
		
		Injector injector = Guice.createInjector(modules);
		
		server = injector.getInstance(Server.class);
		try {
			server.start();			
			Thread.sleep(1000);
		} catch (Exception e) {
			L.throwing(e);
		}		
	}

	@AfterClass
	public static void end(){
		try {
			if(server != null) {
				server.shutdown();	
			}						
			Thread.sleep(1000);
		} catch (Exception e) {
			L.throwing(e);
		}		
	}

}
