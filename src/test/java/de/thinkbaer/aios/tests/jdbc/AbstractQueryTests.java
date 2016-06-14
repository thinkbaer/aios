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
import de.thinkbaer.aios.jdbc.struct.SchemaQueryImpl;
import de.thinkbaer.aios.jdbc.struct.SchemaResultsImpl;
import de.thinkbaer.aios.jdbc.struct.TableQueryImpl;
import de.thinkbaer.aios.server.CoreModule;

import de.thinkbaer.aios.server.datasource.DataSourceManager;


public class AbstractQueryTests {
	
	@Rule public TestName name = new TestName();
	
	private static final Logger L = LogManager.getLogger( AbstractQueryTests.class );
	
	public static Injector injector;
	
	public static DBTestEnviroment env;
	
	public static ObjectMapper mapper = Utils.defaultJsonMapper();

	

	public  static JdbcDataSourceSpec getSpecFrom(String name, DB db){
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
