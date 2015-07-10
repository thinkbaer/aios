package de.thinkbaer.aios.jdbc.query;

import java.util.ArrayList;
import java.util.List;

import de.thinkbaer.aios.api.datasource.query.QueryResults;
import de.thinkbaer.aios.api.datasource.query.SearchQuery;

public class SelectResultsImpl implements QueryResults{

	private int size;
	
	private List<DataSetImpl> data;
	
	

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void push(DataSetImpl dset) {
		if(data == null){
			size = 0;
			data = new ArrayList<DataSetImpl>();
		}

		data.add(dset);
		size = data.size();
		
	}

	public List<DataSetImpl> getData() {
		return data;
	}

	public void setData(List<DataSetImpl> data) {
		this.data = data;
	}
	
}
