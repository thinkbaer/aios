package de.thinkbaer.aios.server.datasource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.annotation.DataSourceType;
import de.thinkbaer.aios.api.datasource.Connection;
import de.thinkbaer.aios.api.datasource.DataSource;
import de.thinkbaer.aios.api.datasource.DataSourceSpec;
import de.thinkbaer.aios.api.exception.AiosException;
import de.thinkbaer.aios.api.utils.GenericObjectFactory;

/**
 * Registry for DataSourceTypes
 * 
 * 
 * @author cezaryrk
 *
 */
public class DataSourceManagerImpl implements DataSourceManager {

	private static final Logger L = LogManager.getLogger(DataSourceManagerImpl.class);

	private Set<DataSourceDescriptor> dataSourceTypes = new HashSet<>();

	private ConcurrentHashMap<String, DataSource> dataSources = new ConcurrentHashMap<>();

	@Inject
	private GenericObjectFactory genericFactory;

	@Inject
	public DataSourceManagerImpl(@Named("dataSourceTypes") Set<Class<? extends DataSource>> dataSourceTypes) {
		dataSourceTypes.forEach((c) -> register(c));
	}

	@Override
	public DataSource register(DataSourceSpec dataSourceSpec) throws AiosException {
		DataSource ds = get(dataSourceSpec.getName());
		if (ds == null) {
			Class<? extends DataSource> dsClass = getBy(dataSourceSpec.getClass());
			ds = genericFactory.inject(dsClass);
			ds.initialize(dataSourceSpec);
			synchronized (dataSources) {
				dataSources.put(ds.getDataSourceSpec().getName(), ds);
			}
		}else{
			L.debug("DataSource with name " + dataSourceSpec.getName() + " is defined returning the instance.");
		}

		return ds;
	}

	@Override
	public boolean unregister(String dataSourceName) throws AiosException {
		synchronized (dataSources) {
			if (dataSources.containsKey(dataSourceName)) {
				dataSources.remove(dataSourceName);
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public DataSource get(String dataSourceName) {
		return dataSources.get(dataSourceName);
	}

	@Override
	public Connection getConnection(String dataSourceName) throws AiosException {
		return dataSources.get(dataSourceName).connection();
	}

	public void register(Class<? extends DataSource> datasource) {
		DataSourceType dst = datasource.getAnnotation(DataSourceType.class);
		DataSourceDescriptor dsd = new DataSourceDescriptor(dst.name(), dst.spec(), datasource);
		L.debug(dsd + "");
		dataSourceTypes.add(dsd);
	}

	public Class<? extends DataSource> getBy(Class<? extends DataSourceSpec> spec) {
		Iterator<DataSourceDescriptor> idsd = dataSourceTypes.iterator();
		while (idsd.hasNext()) {
			DataSourceDescriptor dsd = idsd.next();
			if (dsd.getSpec().equals(spec)) {
				return dsd.getHandler();
			}
		}
		return null;
	}

	static class DataSourceDescriptor {

		private final String name;

		private final Class<? extends DataSourceSpec> spec;

		private final Class<? extends DataSource> handler;

		public DataSourceDescriptor(String name, Class<? extends DataSourceSpec> spec, Class<? extends DataSource> handler) {
			this.name = name;
			this.spec = spec;
			this.handler = handler;
		}

		public String toString() {
			return "DataSourceDescriptor name=" + name + " spec=" + spec.getSimpleName() + " handler=" + handler.getSimpleName();
		}

		public String getName() {
			return name;
		}

		public Class<? extends DataSourceSpec> getSpec() {
			return spec;
		}

		public Class<? extends DataSource> getHandler() {
			return handler;
		}

	}

	@Override
	public List<DataSourceSpec> list() {
		List<DataSourceSpec> specs = new ArrayList<DataSourceSpec>();
		synchronized (dataSources) {
			Iterator<DataSource> iDatasources = dataSources.values().iterator();
			while (iDatasources.hasNext()) {
				specs.add(iDatasources.next().getDataSourceSpec());
			}
		}
		return specs;
	}

}
