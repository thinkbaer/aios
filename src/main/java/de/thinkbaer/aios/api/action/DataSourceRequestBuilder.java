package de.thinkbaer.aios.api.action;

import de.thinkbaer.aios.api.action.support.OperationRequestBuilder;
import de.thinkbaer.aios.api.datasource.DataSourceSpec;
import de.thinkbaer.aios.client.ClientImpl;

public class DataSourceRequestBuilder extends OperationRequestBuilder<DataSourceRequest, DataSourceResponse, DataSourceRequestBuilder>{

	public DataSourceRequestBuilder(ClientImpl client, DataSourceRequest request) {
		super(client, request, DataSourceResponse.class);
	}

	
	
	public DataSourceRequestBuilder register(String type){
		/*
		request.setOperation("register");
		request.attr("type",type);
		*/
		return this;
	}

	public DataSourceRequestBuilder register(DataSourceSpec desc) {
		request().setMethod("register");
		request().setDataSourceSpec(desc);
		return this;		
	}




}
