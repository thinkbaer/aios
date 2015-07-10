package de.thinkbaer.aios.tests.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.After;
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
import de.thinkbaer.aios.api.datasource.Connection;
import de.thinkbaer.aios.api.datasource.DataSource;
import de.thinkbaer.aios.api.datasource.DataSourceSpec;
import de.thinkbaer.aios.api.datasource.query.QueryResults;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.jdbc.JdbcDataSourceSpec;
import de.thinkbaer.aios.jdbc.query.ExecuteBatchQueryImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteBatchResultsImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteQueryImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteResultsImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteUpdateQueryImpl;
import de.thinkbaer.aios.jdbc.query.ExecuteUpdateResultsImpl;
import de.thinkbaer.aios.jdbc.query.SelectQueryImpl;
import de.thinkbaer.aios.jdbc.query.SelectResultsImpl;
import de.thinkbaer.aios.server.CoreModule;
import de.thinkbaer.aios.server.Server;
import de.thinkbaer.aios.server.datasource.DataSourceManager;
import de.thinkbaer.aios.tests.server.ConnectionTests;

public class JdbcTests {
	@Rule public TestName name = new TestName();
	private static final Logger L = LogManager.getLogger( JdbcTests.class );
	
	private static Injector injector;
	
	private static DBTestEnviroment env;
	
	private static ObjectMapper mapper = Utils.defaultJsonMapper();

	@Test
	public void registerDataSource() throws AiosException{
		L.debug("Test connect");
		DataSourceManager manager = injector.getInstance(DataSourceManager.class);
		JdbcDataSourceSpec spec = getSpecFrom("hsql_t1", env.getDB());
		
		DataSource ds = manager.register(spec);
		assertNotNull(ds);
		
		List<DataSourceSpec> specs = manager.list();
		assertEquals(1, specs.size());
		
		boolean unregResult = manager.unregister(ds.getName());
		assertEquals(true, unregResult);
	}
	
	@Test
	public void querySelect() throws AiosException, JsonProcessingException{

		DataSourceManager manager = injector.getInstance(DataSourceManager.class);
		JdbcDataSourceSpec spec = getSpecFrom("hsql_t1", env.getDB());
		
		DataSource ds = manager.register(spec);
		assertNotNull(ds);

		SelectQueryImpl query = new SelectQueryImpl();
		query.sql("SELECT * FROM car");
		Connection connection = ds.connection();
		QueryResults results = connection.query(query);
		assertNotNull(results);		
		
		assertEquals(true, results instanceof SelectResultsImpl);
		
		SelectResultsImpl sResults = (SelectResultsImpl)results;
		
		assertEquals(true, sResults.getSize() > 0);
		
		L.debug(mapper.writeValueAsString(sResults));
		
	}

	@Test
	public void queryExecute() throws AiosException, JsonProcessingException{
		
		DataSourceManager manager = injector.getInstance(DataSourceManager.class);
		JdbcDataSourceSpec spec = getSpecFrom("hsql_t1", env.getDB());
		
		DataSource ds = manager.register(spec);
		assertNotNull(ds);

		ExecuteQueryImpl query = new ExecuteQueryImpl();
		query.sql("INSERT INTO car (type, name) VALUES('Volvo', 'V70')");
		Connection connection = ds.connection();
		QueryResults results = connection.query(query);
		assertNotNull(results);		
		
		assertEquals(true, results instanceof ExecuteResultsImpl);
		
		ExecuteResultsImpl sResults = (ExecuteResultsImpl)results;
		
		assertEquals(true, !sResults.isRet());
		
		L.debug(mapper.writeValueAsString(sResults));
		
	}

	@Test
	public void queryExecuteUpdate() throws AiosException, JsonProcessingException{
		
		DataSourceManager manager = injector.getInstance(DataSourceManager.class);
		JdbcDataSourceSpec spec = getSpecFrom("hsql_t1", env.getDB());
		
		DataSource ds = manager.register(spec);
		assertNotNull(ds);

		ExecuteUpdateQueryImpl query = new ExecuteUpdateQueryImpl();
		query.sql("INSERT INTO car (type, name) VALUES('Volvo', 'V50')");
		Connection connection = ds.connection();
		QueryResults results = connection.query(query);
		assertNotNull(results);		
		
		assertEquals(true, results instanceof ExecuteUpdateResultsImpl);
		
		ExecuteUpdateResultsImpl sResults = (ExecuteUpdateResultsImpl)results;
		
		assertEquals(true, sResults.getAffected() > 0);
		
		L.debug(mapper.writeValueAsString(sResults));
		
	}

	@Test
	public void queryExecuteBatchUpdate() throws AiosException, JsonProcessingException{
		
		DataSourceManager manager = injector.getInstance(DataSourceManager.class);
		JdbcDataSourceSpec spec = getSpecFrom("hsql_t1", env.getDB());
		
		DataSource ds = manager.register(spec);
		assertNotNull(ds);

		ExecuteBatchQueryImpl query = new ExecuteBatchQueryImpl();
		query.sql("INSERT INTO car (type, name) VALUES('Volvo', 'V50')");
		query.sql("INSERT INTO car (type, name) VALUES('Volvo', 'V60')");
		query.sql("INSERT INTO car (type, name) VALUES('Volvo', 'XC90')");
		Connection connection = ds.connection();
		QueryResults results = connection.query(query);
		assertNotNull(results);		
		
		assertEquals(true, results instanceof ExecuteBatchResultsImpl);
		
		ExecuteBatchResultsImpl sResults = (ExecuteBatchResultsImpl)results;
		
		assertEquals(true, sResults.getBatchResults().length == 3);
		
		L.debug(mapper.writeValueAsString(sResults));
		
	}

	

	private static JdbcDataSourceSpec getSpecFrom(String name, DB db){
		JdbcDataSourceSpec spec = new JdbcDataSourceSpec();
		spec.setName(name);
		spec.setDriver(db.getDriverClassName());
		spec.setDriverLocation(db.getDriverPath());
		spec.setUrl(db.getConnectionUrl());
		spec.setUser(db.getUserName());
		spec.setPassword(db.getPassword());
		return spec;
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
		env = new DBTestEnviroment("hsqldb");
		env.getDB().createDB("default");
	}

	@AfterClass
	public static void end(){
		// env.cleanUp();
	}

}
