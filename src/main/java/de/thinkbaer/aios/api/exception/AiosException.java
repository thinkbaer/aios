package de.thinkbaer.aios.api.exception;

import de.thinkbaer.aios.api.transport.ErrorMessage;

public class AiosException extends Exception {

	public AiosException(String string, Throwable e) {
		super(string, e);
	}

	public AiosException() {
		super();
	}

	public AiosException(Throwable e) {
		super(e);
	}
	
	public AiosException(String string) {
		super(string);
	}

	public ErrorMessage asErrorMessage(){
		return new ErrorMessage(getCause().getMessage());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3120698635422858390L;

}
