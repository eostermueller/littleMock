package com.github.eostermueller.littlemock.perfkey;

public class CodeDoesNotExist extends InvalidOption {
	private String invalidCode;
	public CodeDoesNotExist(String codeAndValue, IllegalArgumentException iae) {
		this.codeAndValue = codeAndValue;
		
		//java.lang.IllegalArgumentException: No enum constant com.github.eostermueller.littlemock.ui.PerformanceOption.e
		String msg = iae.getMessage();
		int indexOfLastPeriod = msg.lastIndexOf('.');
		invalidCode = msg.substring( indexOfLastPeriod+1 );
	}
	public String getInvalidCode() {
		return this.invalidCode;
	}
}
