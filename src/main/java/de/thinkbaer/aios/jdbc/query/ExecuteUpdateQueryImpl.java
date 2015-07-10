package de.thinkbaer.aios.jdbc.query;

import java.sql.Connection;
import java.sql.Statement;

import de.thinkbaer.aios.api.datasource.query.ModifyQuery;
import de.thinkbaer.aios.jdbc.ConnectionImpl;

public class ExecuteUpdateQueryImpl extends AbstractQueryImpl<ExecuteUpdateResultsImpl,ExecuteUpdateQueryImpl> implements ModifyQuery<ExecuteUpdateResultsImpl>{

	private String query;
	
	public String queryAsString() {
		return query;
	}

	@Override
	public ExecuteUpdateResultsImpl newResultsObject() {
		return new ExecuteUpdateResultsImpl();
	}

	@Override
	public ExecuteUpdateResultsImpl execute(ConnectionImpl conn) throws Exception {
		ExecuteUpdateResultsImpl results = new ExecuteUpdateResultsImpl();
		Statement stmt = null;
		try {
			stmt = tryAcquireStatement(conn);
			int affected = stmt.executeUpdate(getSql());
			results.affected(affected);
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
