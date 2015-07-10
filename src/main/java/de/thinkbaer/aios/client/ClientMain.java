package de.thinkbaer.aios.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import de.thinkbaer.aios.api.action.PingRequest;
import de.thinkbaer.aios.api.action.PingRequestBuilder;
import de.thinkbaer.aios.api.action.PingResponse;
import de.undercouch.bson4jackson.BsonParser;
import de.undercouch.bson4jackson.deserializers.BsonDateDeserializer;
import de.undercouch.bson4jackson.io.LittleEndianInputStream;


public class ClientMain {
	private Injector injector;
	
	private Client client;

	public void boot() throws Exception {
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new CoreModule());

		injector = Guice.createInjector(modules);

		client = injector.getInstance(Client.class);
		client.connect();
		PingRequestBuilder builder = new PingRequestBuilder((ClientImpl) client, new PingRequest());
		PingResponse response;
		try {
			response = builder.execute().get();
		} catch (InterruptedException | ExecutionException e) {			
		}	
		
		client.close();



	}

	public static void main(String[] args) throws Exception {
		ClientMain main = new ClientMain();
		main.boot();
	}
}
