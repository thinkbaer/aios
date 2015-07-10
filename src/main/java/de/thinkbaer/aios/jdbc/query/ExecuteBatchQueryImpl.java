package de.thinkbaer.aios.jdbc.query;

import java.sql.Statement;
import java.util.Arrays;

import de.thinkbaer.aios.api.datasource.query.ModifyQuery;
import de.thinkbaer.aios.jdbc.ConnectionImpl;

public class ExecuteBatchQueryImpl extends AbstractQueryImpl<ExecuteBatchResultsImpl,ExecuteBatchQueryImpl> implements ModifyQuery<ExecuteBatchResultsImpl> {

	private String[] sqls;
	
	
	@Override
	public ExecuteBatchResultsImpl newResultsObject() {
		return new ExecuteBatchResultsImpl();
	}

	@Override
	public ExecuteBatchResultsImpl execute(ConnectionImpl conn) throws Exception {
		ExecuteBatchResultsImpl results = new ExecuteBatchResultsImpl();
		Statement stmt = null;
		try {
			stmt = tryAcquireStatement(conn);
			for(int i =0;i< sqls.length;i++){
				stmt.addBatch(sqls[i]);
			}
			int[] ret = stmt.executeBatch();			
			results.batchDone(ret);
		} catch (Exception e) {
			throw e;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return results;
	}

	public String[] getSqls() {
		return sqls;
	}

	public void setSqls(String[] sqls) {
		this.sqls = sqls;
	}
	
	public ExecuteBatchQueryImpl batch(String sql){
		if(this.sqls == null){
			this.sqls = new String[]{};
		}
		
		int idx = this.sqls.length;
		this.sqls = Arrays.copyOf(this.sqls, this.sqls.length + 1);
		this.sqls[idx] = sql;
		return this;
	}
	
	public ExecuteBatchQueryImpl sql(String query){
		batch(query);
		return  this;
	}

	
	
}
