package de.thinkbaer.aios.jdbc;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import de.thinkbaer.aios.api.annotation.DataSourceType;
import de.thinkbaer.aios.api.datasource.DataSource;
import de.thinkbaer.aios.api.datasource.DataSourceSpec;
import de.thinkbaer.aios.api.exception.AiosException;

@DataSourceType(name = "jdbc", spec = JdbcDataSourceSpec.class)
public class DataSourceImpl implements DataSource {

	private static final Logger L = LogManager.getLogger(DataSourceImpl.class);

	private JdbcDataSourceSpec spec;

	@Inject
	private DriverManager driverManager;
	
	@Inject
	private Provider<ConnectionImpl> connections;
	
	private BoneCPConfig poolConfiguration;

	private BoneCP pool;


	public boolean initialize(DataSourceSpec spec) throws AiosException {
		this.spec = (JdbcDataSourceSpec) spec;
		L.debug("try loading [" + this.spec.getDriver() + "] from " + this.spec.getDriverLocation());

		try {
			this.driverManager.registerIfNotExists(this.spec.getDriver(), this.spec.getDriverLocation());
		} catch (Exception e) {
			throw new AiosException(e);
		}
		
		this.spec.setId(this.hashCode()+"");
		
		try {
			// DriverManager.setLogWriter( new ConsoleWriter(new ByteArrayOutputStream(),(s) -> {L.info(s);}));
			this.poolConfiguration = new BoneCPConfig();
			poolConfiguration.setPoolName(this.getClass().getPackage().getName() + "." + this.spec.getName());
			poolConfiguration.setJdbcUrl(this.spec.getUrl());
			poolConfiguration.setUsername(this.spec.getUser());
			poolConfiguration.setPassword(this.spec.getPassword());			
			poolConfiguration.setLazyInit(true);
			poolConfiguration.setIdleMaxAgeInSeconds(60);
			poolConfiguration.setMaxConnectionAge(360, TimeUnit.SECONDS);
			poolConfiguration.setMaxConnectionsPerPartition(this.spec.getMaxConnectionPerPartial());
			poolConfiguration.setPartitionCount(this.spec.getMaxPartials());		
			poolConfiguration.setConnectionTimeout(360, TimeUnit.SECONDS);
			
			setPool(new BoneCP(poolConfiguration));
			
			return true;
		} catch (Exception e) {
			L.throwing(e);			
			throw new AiosException(e);			
		}
	}

	public ConnectionImpl connection() throws AiosException {
		// TODO Manage connections
		ConnectionImpl connection = connections.get();
		connection.initialize(this);
		return connection;

	}

	@Override
	public DataSourceSpec getDataSourceSpec() {

		return spec;
	}

	public BoneCP getPool() {
		return pool;
	}

	public void setPool(BoneCP pool) {
		this.pool = pool;
	}
	
	public String getName(){
		return this.getDataSourceSpec().getName();
	}
}
