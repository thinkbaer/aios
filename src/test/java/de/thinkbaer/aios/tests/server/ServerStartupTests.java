package de.thinkbaer.aios.tests.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import de.thinkbaer.aios.server.CoreModule;

import de.thinkbaer.aios.server.Server;

public class ServerStartupTests {

	private static final Logger L = LogManager.getLogger( ServerStartupTests.class );
	
	@Test
	public void startupMain(){
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new CoreModule());
		
		Injector injector = Guice.createInjector(modules);
		
		Server server = injector.getInstance(Server.class);
		try {
			L.info("Starting server ...");
			server.start();			
			L.info("Server started.");
			Thread.sleep(1000);
			server.shutdown();
			// TODO not blocking 
		} catch (Exception e) {
			L.throwing(e);
		}

	}
}
