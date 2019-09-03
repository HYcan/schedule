package com.ronhe.romp.schedule.console.util.validation;

public class ValidateResult {
    private String message;
    private boolean isValid=true;
	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
