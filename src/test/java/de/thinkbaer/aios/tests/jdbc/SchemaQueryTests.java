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
import de.thinkbaer.aios.jdbc.struct.SchemaQueryImpl;
import de.thinkbaer.aios.jdbc.struct.SchemaResultsImpl;
import de.thinkbaer.aios.jdbc.struct.TableQueryImpl;
import de.thinkbaer.aios.jdbc.struct.TableResultsImpl;
import de.thinkbaer.aios.server.datasource.DataSourceManager;


public class SchemaQueryTests extends AbstractQueryTests {
	private static final Logger L = LogManager.getLogger( SchemaQueryTests.class );
	
	@Test
	public void queryListSchemas() throws AiosException, JsonProcessingException{
		
		// HSQLDB => {"@t":"SchemaResultsImpl","size":3,"data":[{"TABLE_SCHEM":"INFORMATION_SCHEMA","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":false},{"TABLE_SCHEM":"PUBLIC","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":true},{"TABLE_SCHEM":"SYSTEM_LOBS","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":false}]}
		DataSourceManager manager = injector.getInstance(DataSourceManager.class);
		JdbcDataSourceSpec spec = getSpecFrom("hsql_t1", env.getDB());
		
		DataSource ds = manager.register(spec);
		assertNotNull(ds);

		SchemaQueryImpl query = new SchemaQueryImpl();
		// query.sql("SELECT * FROM car");
		Connection connection = ds.connection();
		QueryResults results = connection.query(query);
		assertNotNull(results);		
		
		assertEquals(true, results instanceof SchemaResultsImpl);
		
		SchemaResultsImpl sResults = (SchemaResultsImpl)results;
		
		assertEquals(true, sResults.getSchemas().size() == 3);
		
		L.debug(mapper.writeValueAsString(sResults));		
	}
	
	@Test
	public void querySchema() throws AiosException, JsonProcessingException{
		
		// HSQLDB => {"@t":"SchemaResultsImpl","size":3,"data":[{"TABLE_SCHEM":"INFORMATION_SCHEMA","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":false},{"TABLE_SCHEM":"PUBLIC","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":true},{"TABLE_SCHEM":"SYSTEM_LOBS","TABLE_CATALOG":"PUBLIC","IS_DEFAULT":false}]}
		DataSourceManager manager = injector.getInstance(DataSourceManager.class);
		JdbcDataSourceSpec spec = getSpecFrom("hsql_t1", env.getDB());
		
		DataSource ds = manager.register(spec);
		assertNotNull(ds);

		SchemaQueryImpl query = new SchemaQueryImpl();
		query.schema("PUBLIC");
		// query.sql("SELECT * FROM car");
		Connection connection = ds.connection();
		QueryResults results = connection.query(query);
		assertNotNull(results);		
		
		assertEquals(true, results instanceof SchemaResultsImpl);
		
		SchemaResultsImpl sResults = (SchemaResultsImpl)results;
		
		L.debug(mapper.writeValueAsString(sResults));	
		assertEquals(true, sResults.getSchemas().size() == 1);
		
			
	}

}
