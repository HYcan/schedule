package com.ronhe.romp.schedule.console.util.exception;

/**
 * Jdbc异常信息
 */
public class JdbcException extends RuntimeException {

	/**
	 * @fields serialVersionUID 
	 */
	private static final long serialVersionUID = 8350049272861703406L;

	public JdbcException() {
		super();
	}


	public JdbcException(String message, Throwable cause) {
		super(message, cause);
	}

	public JdbcException(String message) {
		super(message);
	}

	public JdbcException(Throwable cause) {
		super(cause);
	}

}
