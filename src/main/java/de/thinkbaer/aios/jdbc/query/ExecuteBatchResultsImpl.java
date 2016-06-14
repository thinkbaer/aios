package de.thinkbaer.aios.jdbc.query;

import de.thinkbaer.aios.api.datasource.query.QueryResults;

public class ExecuteBatchResultsImpl implements QueryResults {

	private int[] batchResults;
	
	
	public void batchDone(int[] ret) {
		batchResults = ret;		
	}


	public int[] getBatchResults() {
		return batchResults;
	}


	public void setBatchResults(int[] batchResults) {
		this.batchResults = batchResults;
	}


}
