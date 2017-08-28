package com.github.eostermueller.littlemock.perfkey;

import java.util.Hashtable;

import com.github.eostermueller.littlemock.Config;

public class PerformanceKey {

	private static final String DELIM = ",";
	private Config config;
	private String perfKey;

	private Hashtable<String,String> codes = new Hashtable<String,String>();
	
	public PerformanceKey(Config c, String myPerfKey) throws DuplicateKeyOption {
		this.setConfig(c);
		this.perfKey = myPerfKey;
	}

	/**
	 * Used when you want to know the perfKey for an existing Config object.
	 * @param c
	 */
	public PerformanceKey(Config c) {
		this.setConfig(c);
	}

	public void apply() throws InvalidCode, DuplicateKeyOption {
		String[] perfKeyOptions = perfKey.split(DELIM);
		for (String singlePerfKeyOption : perfKeyOptions) {
			CodeAndValue codeAndValue = new CodeAndValue(singlePerfKeyOption.trim() );
			String code = codeAndValue.getCode();
			if (codes.containsKey(code)) {
				String oldCodeAndValue = codes.get(code); 
				throw new DuplicateKeyOption(perfKey, oldCodeAndValue, singlePerfKeyOption);
			} else {
				codes.put(code, singlePerfKeyOption);
				PerformanceCode myOption = null;
				try {
					myOption = PerformanceCode.valueOf(code);
				} catch( java.lang.IllegalArgumentException iae) {
					throw new CodeDoesNotExist(codeAndValue.getCodeAndValue(),iae);
				}
				myOption.setCodeAndValue(codeAndValue);
				myOption.set( getConfig() );
			}
		}
	}

	private void setConfig(Config c) {
		config = c;
	}
	private Config getConfig() {
		return config;
	}

	public String getKey() throws InvalidCode {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for( PerformanceCode code : PerformanceCode.values() ) {
			if (count++>0) sb.append(DELIM);
			sb.append( code.get(getConfig() ) );
		}
		return sb.toString().trim();
	}

}
