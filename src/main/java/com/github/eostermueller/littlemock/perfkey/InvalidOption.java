package com.github.eostermueller.littlemock.perfkey;

public class InvalidOption extends Exception {

	NumberFormatException numberFormatException = null;
	String codeAndValue = null;
	String message = null;
	public InvalidOption() {};
	
	public InvalidOption(String codeAndValue, String message) {
		this.codeAndValue = codeAndValue;
		this.message = message;
	}
	@Override
	public String getMessage() {
		return this.message;
	}
	public NumberFormatException getNumberFormatException() {
		return this.numberFormatException;
	}
	
	public String getCodeAndValue() {
		return this.codeAndValue;
	}

	public InvalidOption(NumberFormatException nfe, String codeAndValue) {
		this.numberFormatException = nfe;
		this.codeAndValue = codeAndValue;
	}

}
