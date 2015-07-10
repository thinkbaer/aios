package de.thinkbaer.aios.api.action;

import java.util.Date;

import de.thinkbaer.aios.api.action.support.OperationRequest;

import de.thinkbaer.aios.api.annotation.TransportSpec;

@TransportSpec(ns="sys.ping")
public class PingRequest extends OperationRequest {

	private Date time;
	
	public void now(){
		time = new Date();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
}
