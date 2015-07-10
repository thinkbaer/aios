package de.thinkbaer.aios.api.action;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.thinkbaer.aios.api.action.support.OperationResponse;
import de.thinkbaer.aios.api.annotation.TransportSpec;


@TransportSpec(ns="sys.ping")
public class PingResponse extends OperationResponse {
	
	private static final Logger L = LogManager.getLogger(PingResponse.class);
	
	private Date time;
	
	private long duration;
	

	public void now(){
		time = new Date();
	}


	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}


	public void duration(Date time2) {
		setDuration(time.getTime() - time2.getTime());		
	}


	public long getDuration() {
		return duration;
	}


	public void setDuration(long duration) {
		this.duration = duration;
	}
}
