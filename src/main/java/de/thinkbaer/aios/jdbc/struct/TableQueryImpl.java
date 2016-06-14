package de.thinkbaer.aios.jdbc.struct;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.datasource.query.Query;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.jdbc.ConnectionImpl;
import de.thinkbaer.aios.jdbc.SqlDataType;
import de.thinkbaer.aios.jdbc.query.AbstractQueryImpl;

public class TableQueryImpl extends AbstractQueryImpl<TableResultsImpl, TableQueryImpl>
		implements Query<TableResultsImpl> {

	private static final Logger L = LogManager.getLogger(TableQueryImpl.class);
	// private static final Logger L =
	// LogManager.getLogger(SchemaQueryImpl.class);

	private String schema = null;

	private String catalog = null;

	private String table = null;

	private String[] types = null;

	private String column = null;

	private boolean skipKeys = false;

	@Override
	public TableResultsImpl execute(ConnectionImpl conn) throws Exception {

		TableResultsImpl results = new TableResultsImpl();

		if (catalog == null) {
			throw new AiosException("Catalog is not set");
		} else {

		}
		boolean attachKeys = skipKeys;
		 if(!attachKeys && getTable() != null){
			 attachKeys = true;
		 }

		try {
			DatabaseMetaData databaseMetaData = conn.getConnection().getMetaData();

			ResultSet rsTables = databaseMetaData.getTables(getCatalog(), getSchema(), getTable(), types);
			while (rsTables.next()) {
				String catalogName = rsTables.getString("TABLE_CAT");
				String schemaName = rsTables.getString("TABLE_SCHEM");
				String tableName = rsTables.getString("TABLE_NAME");
				
				String tableType = rsTables.getString("TABLE_TYPE");
				String remarks = rsTables.getString("REMARKS");
				String typeCat = rsTables.getString("TYPE_CAT");
				String typeSchem = rsTables.getString("TYPE_SCHEM");
				String typeName = rsTables.getString("TYPE_NAME");
				String selfRefColName = rsTables.getString("SELF_REFERENCING_COL_NAME");
				String refGeneration = rsTables.getString("REF_GENERATION");

				Table t = results.addTable(tableName);
				t.setCatalog(catalogName);
				t.setSchema(schemaName);
				t.setType(typeSchem);
			}

			if (attachKeys) {
				
				ResultSet rsColumns = databaseMetaData.getColumns(getCatalog(), getSchema(), getTable(), getColumn());
				while (rsColumns.next()) {
					String catalogName = rsColumns.getString("TABLE_CAT");
					String schemaName = rsColumns.getString("TABLE_SCHEM");
					String tableName = rsColumns.getString("TABLE_NAME");
					
										
					String columnName = rsColumns.getString("COLUMN_NAME");
															
					Table t = results.getTable(catalogName, schemaName, tableName);
					if(!t.hasColumn(columnName)){
						Column c = t.addColumn(columnName);
												
						int oDataType = rsColumns.getInt("DATA_TYPE");												
						String oTypeName = rsColumns.getString("TYPE_NAME");					
						int oColumnSize = rsColumns.getInt("COLUMN_SIZE");
						int oDecimalDigits = rsColumns.getInt("DECIMAL_DIGITS");
						int oNumPrecRadix = rsColumns.getInt("NUM_PREC_RADIX");
						int oNnullable = rsColumns.getInt("NULLABLE");
						String oRemarks = rsColumns.getString("REMARKS");
						String oColumnDef = rsColumns.getString("COLUMN_DEF");
						int oSqlDataType = rsColumns.getInt("SQL_DATA_TYPE"); // unused
						int oSqlDataTypeSub = rsColumns.getInt("SQL_DATETIME_SUB"); // unused
						int oCharOctetLength = rsColumns.getInt("CHAR_OCTET_LENGTH"); 
						int oOrdinalPosition = rsColumns.getInt("ORDINAL_POSITION"); 
						String oIsNullable = rsColumns.getString("IS_NULLABLE");
						String oScopeCatalog = rsColumns.getString("SCOPE_CATALOG");
						String oScopeSchema = rsColumns.getString("SCOPE_SCHEMA");
						String oScopeTable = rsColumns.getString("SCOPE_TABLE");
						short oSourceDataType = rsColumns.getShort("SOURCE_DATA_TYPE");
						String oIsAutoincrement = rsColumns.getString("IS_AUTOINCREMENT");
						
						try{
							String oIsGeneratedcolumn = rsColumns.getString("IS_GENERATEDCOLUMN");	
						}catch(SQLException e){
							L.error("SQL Exception",e);
						}
						
						
						c.setTypeCode(oDataType);
						c.setType(SqlDataType.forCode(oDataType).name());						
					}
					
					// ResultSet rsColumns = databaseMetaData.getColumns(getCatalog(), getSchema(), getTable(), getColumn());
				}
				
				// getPrimary
				ResultSet rsPrimarys = databaseMetaData.getPrimaryKeys(getCatalog(), getSchema(), getTable());
				while (rsPrimarys.next()) {
					String catalogName = rsPrimarys.getString("TABLE_CAT");
					String schemaName = rsPrimarys.getString("TABLE_SCHEM");
					String tableName = rsPrimarys.getString("TABLE_NAME");
					String columnName = rsPrimarys.getString("COLUMN_NAME");
					
					Column c = results.getTable(catalogName, schemaName, tableName).getColumn(columnName);
										
					c.isPrimary(true);
					c.getProperties().put("KEY_SEQ", rsPrimarys.getShort("KEY_SEQ"));
					c.getProperties().put("PK_NAME", rsPrimarys.getString("PK_NAME"));
				}

				// getImportedKeys				
				ResultSet rsImported = databaseMetaData.getImportedKeys(getCatalog(), getSchema(), getTable());
				while (rsImported.next()) {
					String fk_catalogName = rsImported.getString("FKTABLE_CAT");
					String fk_schemaName = rsImported.getString("FKTABLE_SCHEM");
					String fk_tableName = rsImported.getString("FKTABLE_NAME");
					String fk_columnName = rsImported.getString("FKCOLUMN_NAME");
					
					Column c = results.getTable(fk_catalogName, fk_schemaName, fk_tableName).getColumn(fk_columnName);
					
					String catalogName = rsImported.getString("PKTABLE_CAT");
					String schemaName = rsImported.getString("PKTABLE_SCHEM");
					String tableName = rsImported.getString("PKTABLE_NAME");
					String columnName = rsImported.getString("PKCOLUMN_NAME");
					
					// c.addReferencedKeys();
					
					Reference r = c.addForeignKeys(catalogName, schemaName, tableName , columnName);
					
					r.getProperties().put("KEY_SEQ", rsImported.getShort("KEY_SEQ"));
					r.getProperties().put("PK_NAME", rsImported.getString("PK_NAME"));
					r.getProperties().put("FK_NAME", rsImported.getString("FK_NAME"));
					r.getProperties().put("UPDATE_RULE", rsImported.getShort("UPDATE_RULE"));
					r.getProperties().put("DELETE_RULE", rsImported.getShort("DELETE_RULE"));
					r.getProperties().put("DEFERRABILITY", rsImported.getShort("DEFERRABILITY"));
				}

				// getExportedKeys
				ResultSet rsExported = databaseMetaData.getExportedKeys(getCatalog(), getSchema(), getTable());
				while (rsExported.next()) {
					String catalogName = rsExported.getString("PKTABLE_CAT");
					String schemaName = rsExported.getString("PKTABLE_SCHEM");
					String tableName = rsExported.getString("PKTABLE_NAME");
					String columnName = rsExported.getString("PKCOLUMN_NAME");
					
					Column c = results.getTable(catalogName, schemaName, tableName).getColumn(columnName);

					String fk_catalogName = rsExported.getString("FKTABLE_CAT");
					String fk_schemaName = rsExported.getString("FKTABLE_SCHEM");
					String fk_tableName = rsExported.getString("FKTABLE_NAME");
					String fk_columnName = rsExported.getString("FKCOLUMN_NAME");

					Reference r = c.addForeignReference(fk_catalogName, fk_schemaName, fk_tableName , fk_columnName);
					
					r.getProperties().put("KEY_SEQ", rsExported.getShort("KEY_SEQ"));
					r.getProperties().put("PK_NAME", rsExported.getString("PK_NAME"));
					r.getProperties().put("FK_NAME", rsExported.getString("FK_NAME"));
					r.getProperties().put("UPDATE_RULE", rsExported.getShort("UPDATE_RULE"));
					r.getProperties().put("DELETE_RULE", rsExported.getShort("DELETE_RULE"));
					r.getProperties().put("DEFERRABILITY", rsExported.getShort("DEFERRABILITY"));
				}

				// getIndexInfo
				ResultSet rsIndexes = databaseMetaData.getIndexInfo(getCatalog(), getSchema(), getTable(), false, true);
				while (rsIndexes.next()) {
					// TODO
				}


				// getColumnPrivileges
				// getCrossReference

			}

		} catch (Exception e) {
			throw e;
		}

		return results;
	}

	@Override
	public TableResultsImpl newResultsObject() {
		return new TableResultsImpl();
	}

	public String schema(String schema) {
		this.schema = schema;//.toUpperCase();
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

	public String table(String table) {
		this.table = table;// .toUpperCase();
		return this.table;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public boolean isSkipKeys() {
		return skipKeys;
	}

	public void setSkipKeys(boolean skipKeys) {
		this.skipKeys = skipKeys;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

}
