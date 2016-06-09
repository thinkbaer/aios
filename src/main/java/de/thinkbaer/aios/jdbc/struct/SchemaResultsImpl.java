package de.thinkbaer.aios.jdbc.struct;

import java.util.ArrayList;
import java.util.List;


import de.thinkbaer.aios.api.datasource.query.QueryResults;



public class SchemaResultsImpl implements QueryResults{

	
	
	private List<String> catalogs = new ArrayList<>();
	
	private List<CatalogSchema> schemas = new ArrayList<>();


	public List<String> getCatalogs() {
		return catalogs;
	}

	public void setCatalogs(List<String> catalogs) {
		this.catalogs = catalogs;
	}

	public List<CatalogSchema> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<CatalogSchema> schemas) {
		this.schemas = schemas;
	}

	public void addCatalog(String string) {
		if(!catalogs.contains(string.trim())){
			catalogs.add(string.trim());
		}
	}
	
	public void addSchema(String catalog, String schema, boolean isDefault){
		CatalogSchema catalogSchema = new CatalogSchema();
		catalogSchema.setCatalog(catalog);
		catalogSchema.setSchema(schema);
		catalogSchema.setDefault(isDefault);
		schemas.add(catalogSchema);
	}
		

	
}
