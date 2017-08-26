package com.github.eostermueller.littlemock.perfkey;

public class CodeAndValue {
	public CodeAndValue(String v) {
		this.setCodeAndValue(v);
	}
	private String codeAndValue = "<uninitialized>";
	
	public void setCodeAndValue(String v) {
		this.codeAndValue = v;
	}
	public String getCodeAndValue() {
		return this.codeAndValue;
	}
	/**
	 * 
	 * @return
	 * @throws InvalidOption
	 */
	public String getCode() throws InvalidOption {
		int i = getIndexOfLastLetter();
		return getCodeAndValue().substring(0,i+1);//+1 b/c substring does a -1, per its javadoc.
	}
	
	public String getValue() throws InvalidOption {
		String rcValue;
		if (this.getCodeAndValue().length() == this.getCode().length())
			rcValue = "";
		else 
			rcValue = this.getCodeAndValue().substring(getIndexOfLastLetter()+1, this.getCodeAndValue().length() );
		
		return rcValue;
	}
	/**
	 * By definition, if a just a code is present, that means 'true'.
	 * To specify false, specify the code followed by 0.
	 * "Q" == true
	 * "Q0" == false
	 * @return
	 * @throws InvalidOption
	 */
	public boolean getBooleanValue() throws InvalidOption {
		boolean ynRC = false;
		
		if ( getValue().equals("") ) {
			ynRC = true;
		} else if ( getValue().equals("0"))
			ynRC = false; 
		else {
			throw new InvalidOption(this.getCodeAndValue(),"Was expecting either [" + this.getCode() + "] (meaning true) or [" + this.getCode() + "0] (meaning faluse) for this boolean option.");
		}
	
		
		return ynRC;
	}
	public int getIntValue() throws InvalidOption {
		int rc = -1;
		try {
			rc = Integer.parseInt(getValue() );
		} catch (NumberFormatException nfe) {
			throw new InvalidOption(nfe, this.getCodeAndValue() );
		}
		return rc;
	}
	
	public int getIndexOfLastLetter() throws InvalidOption {
		if (!Character.isLetter(getCodeAndValue().charAt(0)))
			throw new InvalidOption(getCodeAndValue(), "First char must be letter but instead fnd " + getCodeAndValue().charAt(0) );

		int indexOfLastLetter = -1;
		
		for(int i = 0; i < getCodeAndValue().length(); i++ ) {
			if (Character.isLetter(getCodeAndValue().charAt(i))) {
				indexOfLastLetter = i;
			} else {
				break;
			}
		}
		return indexOfLastLetter;
	}

}
