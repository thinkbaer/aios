package de.thinkbaer.aios.server.datasource;

import java.util.List;

import de.thinkbaer.aios.api.datasource.Connection;
import de.thinkbaer.aios.api.datasource.DataSource;
import de.thinkbaer.aios.api.datasource.DataSourceSpec;
import de.thinkbaer.aios.api.exception.AiosException;

/**
 * Manager for DataSource Connections
 * 
 * @author cezaryrk
 *
 */
public interface DataSourceManager {

	public DataSource register(DataSourceSpec dataSourceSpec) throws AiosException;

	public boolean unregister(String dataSourceName) throws AiosException;

	public DataSource get(String dataSourceName);

	public Connection getConnection(String dataSourceName) throws AiosException;

	public List<DataSourceSpec> list();

	
}
