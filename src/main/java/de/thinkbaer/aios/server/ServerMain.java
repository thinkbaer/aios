package de.thinkbaer.aios.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import de.thinkbaer.aios.api.Constants;
import de.thinkbaer.aios.api.Utils;



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
		File logConf = new File(Utils.localPath(Constants.APP_DIR,"log4j2.xml") );
		if(logConf.exists()){
			System.setProperty("log4j.configurationFile", logConf.getCanonicalPath());
		}			
		ServerMain main = new ServerMain();
		main.boot();
	}
}
