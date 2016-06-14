package de.thinkbaer.aios.jdbc.struct;

import java.util.ArrayList;
import java.util.List;

import com.ibm.icu.util.BytesTrie.Iterator;

import de.thinkbaer.aios.api.datasource.query.QueryResults;



public class TableResultsImpl implements QueryResults{

	private List<Table> tables = new ArrayList<>();	

	public Table addTable(String name) {
		Table t = new Table();
		t.setName(name);
		tables.add(t);
		return t;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public Table getTable(String catalogName, String schemaName, String tableName) {

		java.util.Iterator<Table> iter = tables.iterator();
		
		while(iter.hasNext()){
			Table t = iter.next();
			if(t.getName().contentEquals(tableName) && t.getSchema().contentEquals(schemaName) && t.getCatalog().contentEquals(catalogName)){
				return t;
			}
		}
		
		
		return null;
	}
	
	
		
}
