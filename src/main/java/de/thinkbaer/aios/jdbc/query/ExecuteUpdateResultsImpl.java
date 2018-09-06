package de.thinkbaer.aios.jdbc.query;

import java.util.ArrayList;
import java.util.List;

import de.thinkbaer.aios.api.datasource.query.QueryResults;

public class ExecuteUpdateResultsImpl implements QueryResults {

	private int affected;

	private List<DataSetImpl> data;

	private int size;

	public void push(DataSetImpl dset) {
		if (data == null) {
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

	public void affected(int affected) {
		this.affected = affected;
	}

	public int getAffected() {
		return affected;
	}

	public void setAffected(int affected) {
		this.affected = affected;
	}

}
