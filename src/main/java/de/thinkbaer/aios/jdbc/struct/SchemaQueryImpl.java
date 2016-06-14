package de.thinkbaer.aios.jdbc.struct;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;


import de.thinkbaer.aios.api.datasource.query.Query;
import de.thinkbaer.aios.jdbc.ConnectionImpl;
import de.thinkbaer.aios.jdbc.query.AbstractQueryImpl;


public class SchemaQueryImpl extends AbstractQueryImpl<SchemaResultsImpl, SchemaQueryImpl> implements Query<SchemaResultsImpl> {

//	private static final Logger L = LogManager.getLogger(SchemaQueryImpl.class);

	
	private String schema = null;
	
	// synonym database!
	// TODO filter !!
	private String catalog = null;
	
	@Override
	public SchemaResultsImpl execute(ConnectionImpl conn) throws Exception {
		SchemaResultsImpl results = new SchemaResultsImpl();
						
		
		try {
			DatabaseMetaData databaseMetaData = conn.getConnection().getMetaData();
			ResultSet rsCatalog = databaseMetaData.getCatalogs();
			while(rsCatalog.next()){
				results.addCatalog(rsCatalog.getString("TABLE_CAT"));
			}
			
			ResultSet rsSchema = databaseMetaData.getSchemas();			
			while(rsSchema.next()){
				// L.info(rsSchema."");
				String cat = rsSchema.getString("TABLE_CATALOG");
				String schem = rsSchema.getString("TABLE_SCHEM");				
				boolean isDefault = true;
				try {
					isDefault = rsSchema.getBoolean("IS_DEFAULT");
				}catch(SQLException e){}
						
				results.addSchema(cat, schem,isDefault);
			}
			
		} catch (Exception e) {
			throw e;
		}
		return results;
	}
	
	
	


	@Override
	public SchemaResultsImpl newResultsObject() {
		return new SchemaResultsImpl();
	}


	public String schema(String schema) {
		this.schema = schema.toUpperCase();
		return this.schema;
	}

	public String getSchema() {
		return schema;
	}


	public void setSchema(String schema) {
		this.schema = schema;
	}


	public String getCatalog() {
		return catalog;
	}


	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}


}
