package de.thinkbaer.aios.server;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;



public class ServerMain {

	private Injector injector;
	
	public void boot() throws Exception{
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new CoreModule());
		
		injector = Guice.createInjector(modules);
		
		Server server = injector.getInstance(Server.class);
		server.start();
		
	}
	
	
	public static void main(String[] args) throws Exception{
		ServerMain main = new ServerMain();
		main.boot();
	}
}
