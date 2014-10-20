package it.golem.web.exceptions;

public class DuplicateEmailException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg = "Duplicate file ";

	public DuplicateEmailException(String msg) {

		this.msg = this.msg + msg;
	}
	
	public String getMessage(){
		return msg;
	}
}
