package de.thinkbaer.aios.tests.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.thinkbaer.aios.api.datasource.Connection;
import de.thinkbaer.aios.api.datasource.DataSource;
import de.thinkbaer.aios.api.datasource.query.QueryResults;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.jdbc.JdbcDataSourceSpec;
import de.thinkbaer.aios.jdbc.struct.TableQueryImpl;
import de.thinkbaer.aios.jdbc.struct.TableResultsImpl;
import de.thinkbaer.aios.server.datasource.DataSourceManager;


public class TableQueryTests extends AbstractQueryTests {
	
	
	private static final Logger L = LogManager.getLogger( TableQueryTests.class );
	
	@Test
	public void queryTables() throws AiosException, JsonProcessingException{
		
		// HSQLDB => {"@t":"SchemaResultsImpl","size":3,"data":[{"TABLE_SCHEM":"INFORMATION_SCHEMA","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":false},{"TABLE_SCHEM":"PUBLIC","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":true},{"TABLE_SCHEM":"SYSTEM_LOBS","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":false}]}
		DataSourceManager manager = injector.getInstance(DataSourceManager.class);
		JdbcDataSourceSpec spec = getSpecFrom("hsql_t1", env.getDB());
		
		DataSource ds = manager.register(spec);
		assertNotNull(ds);

		TableQueryImpl query = new TableQueryImpl();
		query.schema("PUBLIC");
		query.setSkipColumns(true);
		// query.sql("SELECT * FROM car");
		Connection connection = ds.connection();
		QueryResults results = connection.query(query);
		assertNotNull(results);		
		
		assertEquals(true, results instanceof TableResultsImpl);
		
		TableResultsImpl sResults = (TableResultsImpl)results;
		
		L.debug(mapper.writeValueAsString(sResults));	
		assertEquals(true, sResults.getTables().size() == 3);
		
			
	}

	
	@Test
	public void queryTable() throws AiosException, JsonProcessingException{
		
		// HSQLDB => {"@t":"SchemaResultsImpl","size":3,"data":[{"TABLE_SCHEM":"INFORMATION_SCHEMA","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":false},{"TABLE_SCHEM":"PUBLIC","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":true},{"TABLE_SCHEM":"SYSTEM_LOBS","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":false}]}
		DataSourceManager manager = injector.getInstance(DataSourceManager.class);
		JdbcDataSourceSpec spec = getSpecFrom("hsql_t1", env.getDB());
		
		DataSource ds = manager.register(spec);
		assertNotNull(ds);

		TableQueryImpl query = new TableQueryImpl();
		query.schema("PUBLIC");
		query.table("CAR");
		Connection connection = ds.connection();
		QueryResults results = connection.query(query);
		assertNotNull(results);				
		assertEquals(true, results instanceof TableResultsImpl);
		
		TableResultsImpl sResults = (TableResultsImpl)results;		
		L.debug(mapper.writeValueAsString(sResults));	
		assertEquals(true, sResults.getTables().size() == 1);
		
			
	}

	@Test
	public void queryTableRefs() throws AiosException, JsonProcessingException{
		
		// HSQLDB => {"@t":"SchemaResultsImpl","size":3,"data":[{"TABLE_SCHEM":"INFORMATION_SCHEMA","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":false},{"TABLE_SCHEM":"PUBLIC","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":true},{"TABLE_SCHEM":"SYSTEM_LOBS","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":false}]}
		DataSourceManager manager = injector.getInstance(DataSourceManager.class);
		JdbcDataSourceSpec spec = getSpecFrom("hsql_t1", env.getDB());
		
		DataSource ds = manager.register(spec);
		assertNotNull(ds);

		TableQueryImpl query = new TableQueryImpl();
		query.schema("PUBLIC");
		query.table("DRIVER");
		Connection connection = ds.connection();
		QueryResults results = connection.query(query);
		assertNotNull(results);				
		assertEquals(true, results instanceof TableResultsImpl);
		
		TableResultsImpl sResults = (TableResultsImpl)results;		
		L.debug(mapper.writeValueAsString(sResults));	
		assertEquals(true, sResults.getTables().size() == 1);
		
			
	}

}
