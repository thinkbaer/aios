package de.thinkbaer.aios.api.datasource;

import de.thinkbaer.aios.api.exception.AiosException;

public interface DataSource {

	public boolean initialize(DataSourceSpec spec) throws AiosException;
	
	public <X extends Connection> Connection connection() throws AiosException;

	public DataSourceSpec getDataSourceSpec();

	public String getName();
	
	
}
