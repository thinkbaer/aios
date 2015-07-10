package de.thinkbaer.aios.jdbc.query;

import java.util.ArrayList;
import java.util.List;

import de.thinkbaer.aios.api.datasource.query.QueryResults;

public class ExecuteResultsImpl implements QueryResults{

	private boolean ret;
	
	private int affected;
	
	private List<DataSetImpl> data;
	
	private int size;
	
	
	public void push(DataSetImpl dset) {
		if(data == null){
			size = 0;
			data = new ArrayList<DataSetImpl>();
		}

		data.add(dset);
		size = data.size();
		
	}

	public void status(boolean okay) {
		this.ret = okay;
		
	}




	public boolean isRet() {
		return ret;
	}




	public void setRet(boolean ret) {
		this.ret = ret;
	}




	public int getAffected() {
		return affected;
	}




	public void setAffected(int affected) {
		this.affected = affected;
	}




	public List<DataSetImpl> getData() {
		return data;
	}




	public void setData(List<DataSetImpl> data) {
		this.data = data;
	}

	public void updateCount(int updateCount) {
		setAffected(updateCount);
	}

}
