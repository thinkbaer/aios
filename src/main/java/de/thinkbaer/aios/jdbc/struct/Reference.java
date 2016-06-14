package de.thinkbaer.aios.jdbc.struct;

import java.util.HashMap;
import java.util.Map;

public class Reference {
	
	private boolean referrer = false;
	
	private String catalog;
	
	private String schema;
	
	private String table;
	
	private String column;
	
	private Map<String,Object> properties = new HashMap<>();

	public boolean isReferrer() {
		return referrer;
	}

	public void setReferrer(boolean referrer) {
		this.referrer = referrer;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	
	
}
