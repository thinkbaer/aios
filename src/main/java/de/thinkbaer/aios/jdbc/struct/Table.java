package de.thinkbaer.aios.jdbc.struct;

import java.util.ArrayList;
import java.util.List;

public class Table {

	private String name;
	
	private String schema;
	
	private String catalog;
	
	private String type;

	private List<Column> columns = new ArrayList<>();
	
	
	public boolean hasColumn(String name) {
		Column c = getColumn(name);
		return c == null ? false : true;
	}
	
	
	public Column getColumn(String name) {
		java.util.Iterator<Column> iter = columns.iterator();
		
		while(iter.hasNext()){
			Column per = iter.next();
			
			if (per.getName().contentEquals(name)) {
				return per;
			}
		}
		return null;
	}

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public String getType() {
		return type;
	}
		
	public void setType(String typeSchem) {
		this.type = typeSchem;		
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}


	public Column addColumn(String columnName) {
		Column c = new Column();
		c.setName(columnName);
		getColumns().add(c);
		return c;
	}
	
}
