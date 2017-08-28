package com.github.eostermueller.littlemock.perfkey;

public class DuplicateKeyOption extends InvalidCode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getOldOption() {
		return oldOption;
	}
	public String getNewOption() {
		return newOption;
	}
	public String getRaw() {
		return raw;
	}
	private String oldOption;
	private String newOption;
	private String raw;

	public DuplicateKeyOption(String raw, String oldOption, String newOption) {
		super();
		this.raw = raw;
		this.oldOption = oldOption; 
		this.newOption = newOption; 
	}
	public String getMessage() {
		return "Found duplicate options [" + oldOption + "] and [" + newOption + "] in the string [" + raw + "]";  
	}
}