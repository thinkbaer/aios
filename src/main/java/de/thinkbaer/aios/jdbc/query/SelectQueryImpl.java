package de.thinkbaer.aios.jdbc.query;

import java.sql.ResultSet;
import java.sql.Statement;

import de.thinkbaer.aios.api.datasource.query.SearchQuery;
import de.thinkbaer.aios.jdbc.ConnectionImpl;

public class SelectQueryImpl extends AbstractQueryImpl<SelectResultsImpl,SelectQueryImpl> implements SearchQuery<SelectResultsImpl> {

	

	@Override
	public SelectResultsImpl newResultsObject() {
		return new SelectResultsImpl();
	}

	@Override
	public SelectResultsImpl execute(ConnectionImpl conn) throws Exception {
		SelectResultsImpl results = new SelectResultsImpl();
		Statement stmt = null;
		try {
			stmt = tryAcquireStatement(conn);
			ResultSet set = stmt.executeQuery(getSql());
			while (set.next()) {
				DataSetImpl dset = new DataSetImpl(set);
				results.push(dset);
			}
			set.close();
		} catch (Exception e) {
			throw e;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return results;
	}


}
