package de.thinkbaer.aios.tests.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import de.thinkbaer.aios.api.action.DataSourceQueryRequestBuilder;
import de.thinkbaer.aios.api.action.DataSourceQueryResponse;
import de.thinkbaer.aios.api.action.DataSourceRequest;
import de.thinkbaer.aios.api.action.DataSourceRequestBuilder;
import de.thinkbaer.aios.api.action.DataSourceResponse;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.client.Client;
import de.thinkbaer.aios.client.ClientImpl;
import de.thinkbaer.aios.jdbc.JdbcDataSourceSpec;
import de.thinkbaer.aios.jdbc.query.SelectQueryImpl;
import de.thinkbaer.aios.server.CoreModule;
import de.thinkbaer.aios.server.Server;
import de.thinkbaer.aios.tests.jdbc.DB;
import de.thinkbaer.aios.tests.jdbc.DBTestEnviroment;

public class DataSourceTests {

	private static final Logger L = LogManager.getLogger(DataSourceTests.class);
		

	private static Server server;
	
	private static DBTestEnviroment env;

	private ObjectMapper om = Utils.defaultJsonMapper();

	public static Client getClient() {
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new de.thinkbaer.aios.client.CoreModule());
		Injector injector = Guice.createInjector(modules);
		return injector.getInstance(Client.class);
	}

	@Test
	public void registerNewDataSource() throws InterruptedException, ExecutionException, JsonProcessingException {
		Client client = getClient();

		// TODO return SessionImpl
		client.connect();

		DataSourceRequest dsr = new DataSourceRequest();
		DataSourceRequestBuilder dsrh = new DataSourceRequestBuilder((ClientImpl) client, dsr);
		JdbcDataSourceSpec desc = ds();
		dsrh.register(desc);

		DataSourceResponse dsres = dsrh.execute().get();

		String out = om.writeValueAsString(dsres);
		L.debug(out);
		
		assertEquals(true, (dsres.getErrors() == null));
		
		assertEquals(true, dsres.getDataSourceSpec().getId() != null);
		client.close();
	}

	
	
	@Test
	public void queryDataSource() throws InterruptedException, ExecutionException, JsonProcessingException, AiosException {
		Client client = getClient();

		// TODO return SessionImpl
		client.connect();

		DataSourceRequest dsr = new DataSourceRequest();
		DataSourceRequestBuilder dsrh = new DataSourceRequestBuilder((ClientImpl) client, dsr);
		JdbcDataSourceSpec desc = ds();
		dsrh.register(desc);
		DataSourceResponse dsres = dsrh.execute().get();
		assertEquals(true, (dsres.getErrors() == null));
		assertEquals(true, dsres.getDataSourceSpec().getId() != null);
		
		DataSourceQueryRequestBuilder dsqrb = new DataSourceQueryRequestBuilder((ClientImpl) client);
		SelectQueryImpl query = dsqrb.dsn(desc.getName()).query(SelectQueryImpl.class);
		query.sql("SELECT * FROM car");

		DataSourceQueryResponse qResponse = dsqrb.execute().get();
		
		
		String out = om.writeValueAsString(qResponse);
		L.debug(out + " [size:"+out.length()+"]");

		
		assertEquals(true, (qResponse.getErrors() == null));
		
		assertEquals(true, dsres.getDataSourceSpec().getId() != null);
		client.close();
	}

	
	private static JdbcDataSourceSpec ds(){
		JdbcDataSourceSpec desc = new JdbcDataSourceSpec();
		desc.setName("hsql_test");
		desc.setDriver("org.hsqldb.jdbc.JDBCDriver");
		desc.setDriverLocation("http://central.maven.org/maven2/org/hsqldb/hsqldb/2.3.3/hsqldb-2.3.3.jar");
		desc.setUrl("jdbc:hsqldb:file:" + DB.testEnvDir + "/" + desc.getName());
		desc.setUser("SA");
		desc.setPassword("");
		return desc;
	}
	
	@Rule
	public TestName name = new TestName();

	@Before
	public void _start() throws Exception {
		L.debug("=== Test: " + name.getMethodName());
	}

	@After
	public void _end() {
		L.debug("=== Test-End: " + name.getMethodName());
	}

	@BeforeClass
	public static void start() throws Exception {
		
		
		env = new DBTestEnviroment("hsqldb",null, ds());
		env.getDB().createDB("default");

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
	public static void end() {
		try {
			server.shutdown();
			Thread.sleep(1000);
		} catch (Exception e) {
			L.throwing(e);
		}
	}

}
