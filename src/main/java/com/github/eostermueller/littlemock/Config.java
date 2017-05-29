package com.github.eostermueller.littlemock;

/**
 * Default values are set to be the slowest configuration, performance-wise.
 * @author erikostermueller
 *
 */
public class Config {
	public static Config SINGLETON = new Config();

	private  int currentLogLevel = 0;
	private  int xpathImplementation = 0; 
	private  int uuidImplementation = 0; 
	private  int randomIntegerImplementation = 0; 
	private  int processingItems = 100;
	private  int processingIterations = 100; 
	private  int fixedDelayMilliseconds = 0; 
	private  boolean fileCacheEnabled = false;

	/**
	 * Private ctor helps make this a singleton;
	 */
	private Config() {	}
	public int getCurrentLogLevel() {
		return currentLogLevel;
	}
	public void setCurrentLogLevel(Integer val) {
		if (val != null) {
			this.currentLogLevel = val;
			this.logAlways("CurrentLogLevel set to [" + this.currentLogLevel + "]");
		}
	}
	public int getXPathImplementation() {
		return xpathImplementation;
	}
	public void setXPathImplementation(Integer val) {
		if (val!=null) {
			this.xpathImplementation = val;
			this.logAlways("XPathImplementation set to [" + this.xpathImplementation + "]");
		}
	}
	public int getUuidImplementation() {
		return uuidImplementation;
	}
	public void setUuidImplementation(Integer val) {
		if (val!=null) {
			this.uuidImplementation = val;
			this.logAlways("UUIDImplementation set to [" + this.uuidImplementation + "]");
		}
	}
	public int getProcessingItems() {
		return processingItems;
	}
	public void setProcessingItems(Integer val) {
		if (val != null) {
			this.processingItems = val;
			this.logAlways("ProcessingItems set to [" + this.processingItems + "]");
		}
	}
	public int getProcessingIterations() {
		return processingIterations;
	}
	public int getFixedDelayMilliseconds() {
		return this.fixedDelayMilliseconds;
	}
	public void setFixedDelayMilliseconds(Integer val) {
		if (val != null) {
			this.fixedDelayMilliseconds = val;
			this.logAlways("FixedDelayMilliseconds set to [" + this.fixedDelayMilliseconds + "]");
		}
	}
	public void setProcessingIterations(Integer val) {
		if (val != null) {
			this.processingIterations = val;
			this.logAlways("ProcessingIterations set to [" + this.processingIterations + "]");
		}
	}
	public int getRandomIntegerImplementation() {
		return randomIntegerImplementation;
	}
	public void setRandomIntegerImplementation(Integer val) {
		if (val != null) {
			this.randomIntegerImplementation = val;
			this.logAlways("RandomIntegerImplementation set to [" + this.randomIntegerImplementation + "]");
		}
	}
	public boolean isFileCacheEnabled() {
		return fileCacheEnabled;
	}
	public void setFileCacheEnabled(Boolean val) {
		if (val != null) {
			this.fileCacheEnabled = val;
			this.logAlways("FileCacheEnabled set to [" + this.fileCacheEnabled + "]");
		}
	}
	private void logAlways(String msg) {
		Controller.logAlways(msg);
	}
	public String toXmlString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("<config>");
    	sb.append("\n    <logLevel>").append(getCurrentLogLevel() ).append("    </logLevel>");
    	sb.append("\n    <fileCache>").append(isFileCacheEnabled() ).append("    </fileCache>");
    	sb.append("\n    <xpathImplementation>").append(this.getXPathImplementation() ).append("    </xpathImplementation>");
    	sb.append("\n    <uuidImplementation>").append(this.getUuidImplementation() ).append("    </uuidImplementation>");
    	sb.append("\n    <randomIntegerImplementation>").append(this.getRandomIntegerImplementation() ).append("    </randomIntegerImplementation>");
    	sb.append("\n    <processingItems>").append(this.getProcessingItems() ).append("    </processingItems>");
    	sb.append("\n    <processingIterations>").append(this.getProcessingIterations() ).append("    </processingIterations>");
    	sb.append("\n    <fixedDelayMilliseconds>").append(this.getFixedDelayMilliseconds() ).append("    </fixedDelayMilliseconds>");
    	sb.append("\n</config>");
		return sb.toString();
	}
	
}
