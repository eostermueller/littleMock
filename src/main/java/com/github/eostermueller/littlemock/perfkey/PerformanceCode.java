package com.github.eostermueller.littlemock.perfkey;

import com.github.eostermueller.littlemock.Config;

/**
 * one or more letters followed by a string of zero or more digits.
 * Multiple codes (comma delimited) make up a PerformanceKey.
 * 
 * PerformanceKey example 1: A3,Q,B353
 * PerformanceKey example 2: X2,J25,K26,L0,Q,S,A10,B65535,C0,D10
 * A is a code, Q is a code and B is a code, because you can find them defined in the below enum.
 * 
 * The letters make up the "code" and the digits make up the "value"
 * A code of zero digits represents a boolean "true", as in "this code is present(aka, true)"
 * The enum instances define how each code/value pair behaves.
 * @author erikostermueller
 *
 */
public enum PerformanceCode {
	
	X() {
		public void apply(Config config) throws InvalidOption {
			config.setXPathImplementation( getCodeAndValue().getIntValue() );
		}
	}
	,J() {
		public void apply(Config config) throws InvalidOption {
			config.setProcessingItems( getCodeAndValue().getIntValue() );
		}
	}
	,K() {
		public void apply(Config config) throws InvalidOption {
			config.setProcessingIterations( getCodeAndValue().getIntValue());
		}
	}
	,L() {
		public void apply(Config config) throws InvalidOption {
			config.setFixedDelayMilliseconds( getCodeAndValue().getIntValue() );
		}
	}
	,Q() {
		public void apply(Config config) throws InvalidOption {
			config.setFileCacheEnabled( getCodeAndValue().getBooleanValue() );
		}
	}
	,S() {
		public void apply(Config config) throws InvalidOption {
			config.setFileCacheEnabled( getCodeAndValue().getBooleanValue() );
		}
	}
	,A() {
		public void apply(Config config) throws InvalidOption {
			config.setOldGenRequestCountThresholdForPruning( getCodeAndValue().getIntValue() );
		}
	}
	,B() {
		public void apply(Config config) throws InvalidOption {
			config.setOldGenMaxBytes( getCodeAndValue().getIntValue() );
		}
	}
	,C() {
		public void apply(Config config) throws InvalidOption {
			config.setOldGenMinExpirationMs( getCodeAndValue().getIntValue() );
		}
	}
	,D() {
		public void apply(Config config) throws InvalidOption {
			config.setOldGenMaxExpirationMs( getCodeAndValue().getIntValue() );
		}
	}
	;
	
	
	private CodeAndValue codeAndValue;

	public void setCodeAndValue(CodeAndValue v) {
		this.codeAndValue = v;
	}
	public CodeAndValue getCodeAndValue() {
		return codeAndValue;
	}
	
	public abstract void apply(Config c) throws InvalidOption;
	

}
