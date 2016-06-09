


package de.thinkbaer.aios.tests.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import io.netty.channel.embedded.EmbeddedChannel;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import de.thinkbaer.aios.api.Utils;
import de.thinkbaer.aios.api.action.DataSourceQueryRequest;
import de.thinkbaer.aios.api.datasource.query.Query;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.api.transport.TransportCodecFactory;
import de.thinkbaer.aios.jdbc.query.ExecuteBatchQueryImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteQueryImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteUpdateQueryImpl;
import de.thinkbaer.aios.jdbc.query.SelectQueryImpl;
import de.thinkbaer.aios.server.CoreModule;

public class MessageTests {
	@Rule public TestName name = new TestName();
	private static final Logger L = LogManager.getLogger( MessageTests.class );
	
	private static Injector injector;
	
	// private static DBTestEnviroment env;
	
	private static ObjectMapper mapper = Utils.defaultJsonMapper();

	@Test
	public void testRequestQueries() throws AiosException, JsonProcessingException{
		SelectQueryImpl sql = new SelectQueryImpl();
		sql.sql("SELECT * FROM CAR");
		testTemplateRequest("test_db", sql);

		ExecuteBatchQueryImpl sql1 = new ExecuteBatchQueryImpl();
		sql1.sql("INSERT INTO car (type,name) VALUES ('asd','asd')");
		sql1.sql("INSERT INTO car (type,name) VALUES ('asd','asd')");		
		testTemplateRequest("test_db", sql1);

		ExecuteQueryImpl sql2 = new ExecuteQueryImpl();
		sql2.sql("INSERT INTO car (type,name) VALUES ('asd','asd')");				
		testTemplateRequest("test_db", sql2);

		ExecuteUpdateQueryImpl sql3 = new ExecuteUpdateQueryImpl();
		sql3.sql("INSERT INTO car (type,name) VALUES ('asd','asd')");				
		testTemplateRequest("test_db", sql3);

	}
	
	private void testTemplateRequest(String dsn, Query<?> q) throws JsonProcessingException{
		TransportCodecFactory factory = injector.getInstance(TransportCodecFactory.class);
		EmbeddedChannel channel = new EmbeddedChannel( 
				factory.newExchangeEncoder(), 
				factory.newExchangeDecoder() );


		
		DataSourceQueryRequest e = new DataSourceQueryRequest();
		e.setDsn(dsn);
		e.setQuery(q);
		String strRep = mapper.writeValueAsString(e);
		L.debug("DSQR[before]: " + strRep);
		
		channel.writeOutbound( e );		
		channel.writeInbound( channel.readOutbound() );		
		DataSourceQueryRequest returnedFoo = (DataSourceQueryRequest) channel.readInbound();

		String strRep2 = mapper.writeValueAsString(returnedFoo);
		L.debug("DSQR[after]: " + strRep2);

		assertNotNull(returnedFoo);
				
		
		assertEquals(true, q.getClass().isInstance(returnedFoo.getQuery()));
		
		
		
		assertEquals(strRep, strRep2);
		

	}
	

	@Before
	public void _start() throws Exception{
		L.debug("=== Test: " + name.getMethodName());
	}

	@After
	public void _end(){
		L.debug("=== Test-End: " + name.getMethodName());
	}

	@BeforeClass
	public static void start() throws Exception{
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new CoreModule());		
		injector = Guice.createInjector(modules);		
	}

	@AfterClass
	public static void end(){
		// env.cleanUp();
	}

}
