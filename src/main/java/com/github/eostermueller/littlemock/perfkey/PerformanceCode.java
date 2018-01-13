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
		public void set(Config config) throws InvalidCode {
			config.setXPathImplementation( getCodeAndValue().getIntValue() );
		}
		public String get(Config config)  {
			return this.name() + String.valueOf(config.getXPathImplementation()).trim();
		}
	}
	,J() {
		public void set(Config config) throws InvalidCode {
			config.setProcessingItems( getCodeAndValue().getIntValue() );
		}
		public String get(Config config)  {
			return this.name() + String.valueOf(config.getProcessingItems() ).trim();
		}
	}
	,K() {
		public void set(Config config) throws InvalidCode {
			config.setProcessingIterations( getCodeAndValue().getIntValue());
		}
		public String get(Config config)  {
			return this.name() + String.valueOf(config.getProcessingIterations() ).trim();
		}
	}
	,L() {
		public void set(Config config) throws InvalidCode {
			config.setFixedDelayMilliseconds( getCodeAndValue().getIntValue() );
		}
		public String get(Config config)  {
			return this.name() + String.valueOf(config.getFixedDelayMilliseconds() ).trim();
		}
	}
	,M() {
		public void set(Config config) throws InvalidCode {
			config.setSynchronizedSleep( getCodeAndValue().getBooleanValue() );
		}
	        public String get(Config config)  {
			boolean yn = config.isSleepSynchronized();
			String rc = "";
			
			if (yn)
				rc = this.name() + "1";
			else 
				rc = this.name() + "0";
			
			return rc;
		}
	}
	,Q() {
		public void set(Config config) throws InvalidCode {
			config.setFileCacheEnabled( getCodeAndValue().getBooleanValue() );
		}
		public String get(Config config)  {
			boolean yn = config.isFileCacheEnabled();
			String rc = "";
			
			if (yn)
				rc = this.name() + "1";
			else 
				rc = this.name() + "0";
			
			return rc;
		}
	}
	,R() {
		public void set(Config config) throws InvalidCode {
			config.setUUIDIsOptimized( getCodeAndValue().getBooleanValue() );
		}
		public String get(Config config)  {
			boolean yn = config.isUuidOptimized();
			String rc = "";
			
			if (yn)
				rc = this.name() + "1";
			else 
				rc = this.name() + "0";
			
			return rc;
		}
	}
	,S() {
		public void set(Config config) throws InvalidCode {
			String value = getCodeAndValue().getValue().trim();
			
			if (value==null || value.length()==0 || value.equals("")) 
				config.setRandomIntegerImplementation( 1 );
			else if (Integer.parseInt(value)==0)
				config.setRandomIntegerImplementation( 0 );
			else if (Integer.parseInt(value)==1)
				config.setRandomIntegerImplementation( 1 );
			else throw new InvalidCode(getCodeAndValue().getCodeAndValue(),"only S or S0 or S1 are allowed.");
			
		}
		public String get(Config config)  {
			return this.name() + String.valueOf( config.getRandomIntegerImplementation() ).trim();
		}
	}
	,T() {
		public void set(Config config) throws InvalidCode {
			config.setXsltImplementation( getCodeAndValue().getIntValue() );
		}
		public String get(Config config)  {
			return this.name() + String.valueOf(config.getXsltImplementation() ).trim();
		}
	}
	,A() {
		public void set(Config config) throws InvalidCode {
			config.setOldGenRequestCountThresholdForPruning( getCodeAndValue().getIntValue() );
		}
		public String get(Config config)  {
			return this.name() + String.valueOf(config.getOldGenRequestCountThresholdForPruning() ).trim();
		}
	}
	,B() {
		public void set(Config config) throws InvalidCode {
			config.setOldGenMaxBytes( getCodeAndValue().getIntValue() );
		}
		public String get(Config config)  {
			return this.name() + String.valueOf(config.getOldGenMaxBytes() ).trim();
		}
	}
	,C() {
		public void set(Config config) throws InvalidCode {
			config.setOldGenMinExpirationMs( getCodeAndValue().getIntValue() );
		}
		public String get(Config config)  {
			return this.name() + String.valueOf(config.getOldGenMinExpirationMs() ).trim();
		}
	}
	,D() {
		public void set(Config config) throws InvalidCode {
			config.setOldGenMaxExpirationMs( getCodeAndValue().getIntValue() );
		}
		public String get(Config config)  {
			return this.name() + String.valueOf(config.getOldGenMaxExpirationMs() ).trim();
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
	
	public abstract void set(Config c) throws InvalidCode;
	public abstract String get(Config c) throws InvalidCode;
	

}
