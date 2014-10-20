package it.golem.alarm.exceptions;

public class AlarmException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6227028426656595832L;

	public AlarmException(Throwable cause, String message,
			StackTraceElement[] stackTrace) {
		super(message, cause);
		super.setStackTrace(stackTrace);
	}
	

}
