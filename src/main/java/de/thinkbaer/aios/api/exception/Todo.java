package de.thinkbaer.aios.api.exception;


public class Todo extends AiosException{

	public Todo(String string, Throwable root) {
		super(string,root);
	}

	public Todo() {
		super();
	}

	public Todo(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 125352734930758974L;

}
