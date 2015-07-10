package de.thinkbaer.aios.api.transport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Transport {
		
	@JsonIgnore
	private TransportSpec spec;

	private List<ErrorMessage> errors;
	
	
	
	public TransportSpec getSpec() {
		if(spec == null){
			spec = new TransportSpec(this);			
		}
		
		return spec;
	}

	public void setSpec(TransportSpec spec) {
		this.spec = spec;
	}

	public List<ErrorMessage> getErrors() {
		return errors;
	}
	
	public void addError(ErrorMessage error) {
		if(errors == null){
			errors = new ArrayList<>();
		}
		errors.add(error);
		
	}
	
}
