package de.thinkbaer.aios.api.action;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.thinkbaer.aios.api.action.support.OperationRequest;
import de.thinkbaer.aios.api.annotation.TransportSpec;
import de.thinkbaer.aios.api.datasource.DataSourceSpec;

@TransportSpec(ns="ds")
public class DataSourceRequest extends OperationRequest{

	
	
	private String method;
	
	@JsonProperty("spec")
	private DataSourceSpec dataSourceSpec;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public DataSourceSpec getDataSourceSpec() {
		return dataSourceSpec;
	}

	public void setDataSourceSpec(DataSourceSpec dataSourceSpec) {
		this.dataSourceSpec = dataSourceSpec;
	}

}
