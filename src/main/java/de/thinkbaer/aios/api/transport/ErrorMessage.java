package de.thinkbaer.aios.api.transport;

public class ErrorMessage {
	
	private String message;

	public ErrorMessage(){}

	public ErrorMessage(String string) {
		this.message = string;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
