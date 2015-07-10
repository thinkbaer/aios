package de.thinkbaer.aios.tests.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import io.netty.channel.embedded.EmbeddedChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import de.thinkbaer.aios.api.ApiModule;
import de.thinkbaer.aios.api.Utils;
import de.thinkbaer.aios.api.action.DataSourceRequest;
import de.thinkbaer.aios.api.action.PingRequest;
import de.thinkbaer.aios.api.transport.TransportCodecFactory;
import de.thinkbaer.aios.jdbc.JdbcDataSourceSpec;

public class ExchangeCodecTests {

	private static final Logger L = LogManager.getLogger( ExchangeCodecTests.class );

	private static Injector injector;
	
	
	@Test
	public void ping() throws JsonParseException, JsonMappingException, IOException{
		TransportCodecFactory factory = injector.getInstance(TransportCodecFactory.class);
		EmbeddedChannel channel = new EmbeddedChannel( 
				factory.newExchangeEncoder(), 
				factory.newExchangeDecoder() );

		ObjectMapper mapper = Utils.defaultJsonMapper();
		
		PingRequest e = new PingRequest();
		e.now();
		String strRep = mapper.writeValueAsString(e);
		L.debug("Ping[before]: " + strRep + " time = " + e.getTime().getTime());
		
		channel.writeOutbound( e );		
		channel.writeInbound( channel.readOutbound() );		
		Object returnedFoo = channel.readInbound();

		assertNotNull(returnedFoo);
				
		assertEquals(true, returnedFoo instanceof PingRequest);
		PingRequest ret = (PingRequest)returnedFoo;
		
		String strRep2 = mapper.writeValueAsString(ret);
		L.debug("Ping[after]: " + strRep2 + " time = "+ ret.getTime().getTime());
		
		assertEquals(strRep, strRep2);
		
		// Time error because of granality
		// assertEquals(e.getClientTime().getTime(), ret.getClientTime().getTime());
		long diff = (long) (e.getTime().getTime() % 1000);
		assertEquals(e.getTime().getTime() - diff, ret.getTime().getTime());
	}

	@Test
	public void registerDataSource() throws JsonParseException, JsonMappingException, IOException{
		TransportCodecFactory factory = injector.getInstance(TransportCodecFactory.class);
		EmbeddedChannel channel = new EmbeddedChannel( 
				factory.newExchangeEncoder(), 
				factory.newExchangeDecoder() );

		ObjectMapper mapper = Utils.defaultJsonMapper();
		
		DataSourceRequest e = new DataSourceRequest();
		JdbcDataSourceSpec desc = new JdbcDataSourceSpec();
		desc.setDriver("org.hsqldb.jdbc.JDBCDriver");
		desc.setDriverLocation("http://central.maven.org/maven2/org/hsqldb/hsqldb/2.3.3/hsqldb-2.3.3.jar");
		desc.setUrl("jdbc:hsqldb:file:test/dbfile");
		desc.setUser("SA");
		desc.setPassword("");

		e.setDataSourceSpec(desc);
		e.setMethod("register");
		
		String strRep = mapper.writeValueAsString(e);
		L.debug("DataSource[before]: " + strRep );
		
		channel.writeOutbound( e );		
		channel.writeInbound( channel.readOutbound() );		
		Object returnedFoo = channel.readInbound();

		assertNotNull(returnedFoo);
				
		assertEquals(true, returnedFoo instanceof DataSourceRequest);
		DataSourceRequest ret = (DataSourceRequest)returnedFoo;
		
		String strRep2 = mapper.writeValueAsString(ret);
		L.debug("DataSource[after]: " + strRep2);
		
		assertEquals(strRep, strRep2);
		

	}

	@BeforeClass
	public static void __before(){
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new ApiModule());
		injector = Guice.createInjector(modules);
	}
	
	@AfterClass
	public static void __after(){
		
	}	
	

}
