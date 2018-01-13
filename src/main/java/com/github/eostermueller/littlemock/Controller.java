package com.github.eostermueller.littlemock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.github.eostermueller.littlemock.OldGenerationRepo.OldGenerationData;
import com.github.eostermueller.littlemock.perfkey.InvalidCode;
import com.github.eostermueller.littlemock.perfkey.PerformanceKey;
import com.github.eostermueller.littlemock.xslt.XsltProcessor;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class Controller implements EnvironmentAware {
        /**   if Config.isSleepSynchronized() then 
          *   this lock will allow only one thread to be sleeping at a time.
          */
        Object sleepLock = new Object();
	XsltProcessor xsltProcessor = null;

	private String humanReadableConfig = "<uninitialized>";
	
	public Controller() {
		this.repo = PlaybackRepository.SINGLETON;
		
		this.setOldGenRepo( new OldGenerationRepo() );
		getConfig().setOldGenRequestCountThresholdForPruning_changeListener(this.getOldGenRepo());
		
	}

	private Environment env;
	private volatile PlaybackRepository repo;

	@RequestMapping(
		    value = "/", 
		    method = RequestMethod.POST)	
    String home(@RequestBody String input) {
    	String rc = "<LittleMockError>HTTP input did not match any of the configured XPath expressions.  \n[" + humanReadableConfig + "]</LittleMockError>";
    	StringBuilder sb = new StringBuilder();
    	try {
    		if (isInfo()) {
	    		Controller.logInfo("####");
	    		Controller.logInfo("HTTP rq size: [" + lpad(""+input.length(),7) + "] bytes. Looking for match.");
    		}
    		
    		if (getConfig().getFixedDelayMilliseconds()>0)
                          if (getConfig().isSleepSynchronized() )
    			      simulateSynchronizedSlowCode( (long) getConfig().getFixedDelayMilliseconds() );
                          else
    			      simulateSlowCode( (long) getConfig().getFixedDelayMilliseconds() );
    		
    		busyProcessing();
    		
    		oldGenProcessing();
    		
    		String xsltRc = xsltProcessing();
    		sb.append("<xslt>"+ xsltRc + "</xslt>");
    		
    	    rc = this.getRepository().getResponse(input);
    	    sb.append(rc);
    	    
    	    
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        return sb.toString();
    }
	
	private String xsltProcessing() {
		String rc = null;
		switch( getConfig().getXsltImplementation() ) {
			case 0:
				rc = "";
				//incur no overhead from xslt by default at startup.
				break;
			case 1:
				rc = xsltProcessor.unPooledTransformerXslt();
				break;
			case 2:
				rc = this.xsltProcessor.pooledTransformerXslt();
				break;
			default:
				throw new UnsupportedOperationException("Xslt impl ["  + getConfig().getXsltImplementation() + "] is not supported.  only 0, 1, and 2");
		}
		return rc;
	}

	private void oldGenProcessing() {
		
		OldGenerationData data = getConfig().createData(); //somewhat randomized expiration time is encoded inside created object.
		
		getOldGenRepo().maybeAdd(data);				//only adds if enabled flag is set.
		
		getOldGenRepo().maybePrune();					//remove data whose expiration has expired, 
															//but only when enabled and when the 'nth' request has been made
		
	}
	private void simulateSlowCode(long milliseconds) {
		try {
			Thread.sleep( milliseconds );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
 	private void simulateSynchronizedSlowCode(long milliseconds) {
		try {
                        synchronized(sleepLock) {
			     Thread.sleep( milliseconds );
                        }
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
   private void busyProcessing() {
    	
    	List<String> myList = new ArrayList<String>();
    	
		for( int i = 0; i < getConfig().getProcessingItems(); i++) {
			Item item = new Item(); //create some uuids
			item.process( getConfig().getProcessingIterations() ); //shuffle them around a bit.
			myList.add( item.toString() ); //concatenate them into a big string
		}
		
		Collections.sort(myList);
		
	}
    
    
    /**
     * Apply a comma-separated parameter string, called a performanceKey, in a single operation.
     * The individual 'codes' in a performanceKey string (between the commas) each map to a single parameter in Controller#config().
     * This enables two users on separate machines to easily reproduce a complicated, multi-parameter littleMock performance
     * scenario by simply dialing in the same perfKey.
     * The code mapping is defined in com/github/eostermueller/littlemock/perfkey/PerformanceCode 
     * @param myPerfKey EXAMPLE: X2,J25,K26,L0,Q,S,A10,B65535,C0,D10
     * @return
     * @throws IOException
     * @throws InvalidCode
     */
//	@RequestMapping(value="/perfKey", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
//    String perfKey(
//			@RequestParam(value="value", required=false) String myPerfKey
//			) throws IOException, InvalidCode {
//		
//		PerformanceKey pk = new PerformanceKey(getConfig(), myPerfKey);
//		pk.apply();
//		
//    	return getConfig().toXmlString();
//	}
    
	/**
	 * Set individual parameters that change performance characteristics for 
	 * this web application, even while load is being applied.
	 * @param intLogLevel
	 * @param intXPathImpl
	 * @param intXsltImpl
	 * @param ynUuidIsOptimized
	 * @param intRandomIntImpl
	 * @param intProcessingItems
	 * @param intProcessingIterations
	 * @param intFixedDelayMilliseconds
	 * @param ynXPathFactoryCache
	 * @param ynDocBuilderCache
	 * @param ynFileCache
	 * @param intOldGenMinExpirationMs
	 * @param intOldGenMaxExpirationMs
	 * @param intOldGenMaxBytes
	 * @param intOldGenRequestCountThresholdForPruning
	 * @return
	 * @throws IOException
	 * @throws InvalidCode 
	 */
	@RequestMapping(value="/config", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String config(
    			@RequestParam(value="logLevel", required=false) Integer intLogLevel,
    			@RequestParam(value="xpathImplementation", required=false) Integer intXPathImpl,
    			@RequestParam(value="xsltImplementation", required=false) Integer intXsltImpl,
    			@RequestParam(value="uuidIsOptimized", required=false) Boolean ynUuidIsOptimized,
    			@RequestParam(value="randomIntegerImplementation", required=false) Integer intRandomIntImpl,
    			@RequestParam(value="processingItems", required=false) Integer intProcessingItems,
    			@RequestParam(value="processingIterations", required=false) Integer intProcessingIterations,
    			@RequestParam(value="fixedDelayMilliseconds", required=false) Integer intFixedDelayMilliseconds,
    			@RequestParam(value="xpathFactoryCache", required=false) Boolean ynXPathFactoryCache,
    			@RequestParam(value="docBuilderCache", required=false) Boolean ynDocBuilderCache,
    			@RequestParam(value="fileCache", required=false) Boolean ynFileCache,
    			@RequestParam(value="syncSleep", required=false) Boolean ynIsSleepSynchronized,
    			@RequestParam(value="oldGenMinExpirationMs", required=false) Integer intOldGenMinExpirationMs,
    			@RequestParam(value="oldGenMaxExpirationMs", required=false) Integer intOldGenMaxExpirationMs,
    			@RequestParam(value="oldGenMaxBytes", required=false) Integer intOldGenMaxBytes,
    			@RequestParam(value="oldGenRequestCountThresholdForPruning", required=false) Integer intOldGenRequestCountThresholdForPruning,
    			@RequestParam(value="perfKey", required=false) String perfKey
    			
    			) throws IOException, InvalidCode {

		if (perfKey!=null && perfKey.trim().length()!=0) {
			/**
		     * Apply a comma-separated parameter string, called a performanceKey.  This string specifies multiple codes at one time.
		     * The individual 'codes' in a performanceKey string (between the commas) each map to a single parameter in Controller#config().
		     * This enables two users on separate machines to easily reproduce a complicated, multi-parameter littleMock performance
		     * scenario by simply dialing in the same perfKey.
		     * The code mapping is defined in com/github/eostermueller/littlemock/perfkey/PerformanceCode
		     * EXAMPLE perfKey: X2,J25,K26,L0,Q,S,A10,B65535,C0,D10 
		     */ 
			
			/**
			 * Set defaults first.
			 */
			new PerformanceKey(getConfig(), Config.DEFAULT_KEY).apply();
			
			/**
			 * override defaults with caller's perfKey
			 */
			new PerformanceKey(getConfig(), perfKey).apply();
			
		} else {
	   		getConfig().setCurrentLogLevel(intLogLevel);
	   		getConfig().setXPathImplementation(intXPathImpl);
	   		getConfig().setXsltImplementation(intXsltImpl);
	   		getConfig().setUUIDIsOptimized(ynUuidIsOptimized);
	   		getConfig().setRandomIntegerImplementation(intRandomIntImpl);
	   		getConfig().setFileCacheEnabled(ynFileCache);
	   		getConfig().setSynchronizedSleep(ynIsSleepSynchronized);
	   		getConfig().setProcessingItems(intProcessingItems);
	   		getConfig().setProcessingIterations(intProcessingIterations);
	   		getConfig().setFixedDelayMilliseconds(intFixedDelayMilliseconds);
	   		
	   		
	   		Config c = getConfig();
	   		c.setOldGenMinExpirationMs(intOldGenMinExpirationMs);
	   		getConfig().setOldGenMaxExpirationMs(intOldGenMaxExpirationMs);
	   		getConfig().setOldGenMaxBytes(intOldGenMaxBytes);
	   		getConfig().setOldGenRequestCountThresholdForPruning(intOldGenRequestCountThresholdForPruning);
		}
    	
    	return getConfig().toXmlString();
    }	
	static Config getConfig() {
		return Config.SINGLETON;
	}
	public OldGenerationRepo getOldGenRepo() {
		return oldGenRepo;
	}
	public void setOldGenRepo(OldGenerationRepo oldGenRepo) {
		this.oldGenRepo = oldGenRepo;
	}
	OldGenerationRepo oldGenRepo = null; 
	
    public static void main(String[] args) throws Exception {
        
		ApplicationContext ac = SpringApplication.run(Controller.class, args);
        ServletContext sc = null;
                 if (ac instanceof WebApplicationContext) {
                         WebApplicationContext wac = (WebApplicationContext)ac;
                         sc = wac.getServletContext();
                 }
		
//		String contextPath = sc.getRealPath(File.separator);
//		System.out.println("real path [" + contextPath + "]");
//	/private/var/folders/0g/kw0y_65d6xq9w3944219hwc80000gn/T/tomcat-docbase.1459412510349223373.8080/
        
    }
    
    /**
     * @return
     */
    private PlaybackRepository getRepository() {
    	PlaybackRepository result = this.repo;
    	/**
    	 * Use double check locking during initialization
    	 * https://en.wikipedia.org/wiki/Double-checked_locking and https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
    	 */
        if (result.getCount() == 0) {
            synchronized(this) {
                result = repo;
                if (result.getCount() == 0) {
                    this.loadProperties();
                }
            }
        }
        return result;
    }
        
    /**
     * Properties are loaded from application.properties.
<pre>
request.3.xpath=/foo/bar
request.3.response.file=xml-anti-pattern.jmx
</pre>
     */
	private void loadProperties() {
		
		String fldr = env.getProperty("response.folder");
		this.repo.setRootFolder( new File(fldr));;
		
		int nthConfigItem = 1;
		while(true) {
			String seq = (""+nthConfigItem++).trim();
			
			String xpathPropName = "request." + seq + ".xpath";
			String responseTextFilePropName = "request." + seq + ".response.file";
			String xpath = env.getProperty(xpathPropName);
			String responseTextFileName = env.getProperty(responseTextFilePropName);
			if (xpath!=null && !"".equals(xpath.trim())
				&& responseTextFileName!=null & !"".equals(responseTextFileName.trim())	) {
				
				repo.addFile(xpath, responseTextFileName);
				Controller.logAlways("Loading config item [" + seq + "] xpath: [" + rpad(xpath, 60) + "] data file: [" + rpad(responseTextFileName,25) + "]" );
				
			} else {
				Controller.logAlways("End of littleMock initialization.  No more properties from application.properties will be loaded because either [" + xpathPropName + "] or [" + responseTextFilePropName + "] was not found in application.properties.");
				//parameters in application.properties must be
				//in sequential order, starting at 1.
				//When we discover the first missing one, stop looking for more.
				break;
			}
		}
		humanReadableConfig = PlaybackRepository.SINGLETON.humanReadable();
	}
	@Override
	public void setEnvironment(Environment arg0) {
		this.env = arg0;
		
		/*
		 * Had to put load xslt here because env was not set during the time the Controller was executing :-(
		 */
		xsltProcessor = new XsltProcessor( env.getProperty("xslt.files"));
		Controller.logAlways("Just loaded Xslt Repositories [" + this.xsltProcessor.getHumanReadableFileList() + "] ." );
		
	}

	Document createFactoryAndParse(InputSource input) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(input);
		return document;
	}
	public static String rpad(String str, int num) {
		    return String.format("%1$-" + num + "s", str);
	 }
	public static String lpad(String str, int num) {
	    return String.format("%1$" + num + "s", str);
	}
	public static boolean isDebug() {
		boolean yn = false;
		if( getConfig().getCurrentLogLevel() >= 2 )
			yn = true;
		return yn;
	}
	public static boolean isInfo() {
		boolean yn = false;
		if( getConfig().getCurrentLogLevel() >= 1 )
			yn = true;
		return yn;
	}
	public static void logDebug(String msg) {
		 if (isDebug()) {
			System.out.println(getBanner()+"DEBUG:"+msg);
		}
	}
	public static void logInfo(String msg) {
		if( isInfo() ) {
			System.out.println(getBanner()+"INFO:"+msg);
		}
	}
	public static void logAlways(String msg, Exception e) {
		System.out.println(getBanner()+msg);
		e.printStackTrace();
	}
	public static void logAlways(String msg) {
			System.out.println(getBanner()+msg);
	}

	private static String getBanner() {
		return "littleMock: ";
	}
	@RequestMapping(
		    value = "/clearOldGen", 
		    method = RequestMethod.GET)	
    	String clearOldGen() {
		
		this.getOldGenRepo().clear();
		logAlways("Old Generation has been cleared.");
		return "";
	}

}
