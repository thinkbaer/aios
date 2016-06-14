package de.thinkbaer.aios.jdbc.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Column {

	private String name;
	
	private String type;
	
	private int typeCode;
	
	private boolean primary;
			
	private Map<String,Object> properties = new HashMap<>();
	
	private List<Reference> references = new ArrayList<>(); 
		

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}
	

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public boolean isPrimary() {
		return primary;
	}

	public boolean isPrimary(boolean primary) {
		setPrimary(primary);
		return isPrimary();
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public Reference addForeignReference(String fk_catalogName, String fk_schemaName, String fk_tableName,
			String fk_columnName) {
		Reference r = new Reference();
		r.setReferrer(true);
		r.setCatalog(fk_catalogName);
		r.setSchema(fk_schemaName);
		r.setTable(fk_tableName);
		r.setColumn(fk_columnName);
		
		getReferences().add(r);
		return r;
	}
	
	public Reference addForeignKeys(String fk_catalogName, String fk_schemaName, String fk_tableName,
			String fk_columnName) {
		Reference r = new Reference();
		r.setReferrer(false);
		r.setCatalog(fk_catalogName);
		r.setSchema(fk_schemaName);
		r.setTable(fk_tableName);
		r.setColumn(fk_columnName);
		
		getReferences().add(r);
		return r;
	}

	public List<Reference> getReferences() {
		return references;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}


	
	
}
