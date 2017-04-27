package com.github.eostermueller.littlemock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;





import java.util.Map;

//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
public class Controller implements EnvironmentAware {

	private String humanReadableConfig = "<uninitialized>";
	
	public Controller() {
		this.repo = PlaybackRepository.SINGLETON;
	}

	private Environment env;
	private volatile PlaybackRepository repo;

	@RequestMapping(
		    value = "/", 
		    method = RequestMethod.POST)	
    String home(@RequestBody String input) {
    	String rc = "<LittleMockError>HTTP input did not match any of the configured XPath expressions.  \n[" + humanReadableConfig + "]</LittleMockError>";
    	
    	try {
    		if (isInfo()) {
	    		Controller.logInfo("####");
	    		Controller.logInfo("HTTP rq size: [" + lpad(""+input.length(),7) + "] bytes. Looking for match.");
    		}
    		
    		if (getConfig().getFixedDelayMilliseconds()>0)
    			Thread.sleep( (long) getConfig().getFixedDelayMilliseconds() );
    		
    		busyProcessing();
    		
    	    rc = this.getRepository().getResponse(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        return rc;
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
	@RequestMapping(value="/config", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String config(
    			@RequestParam(value="logLevel", required=false) Integer intLogLevel,
    			@RequestParam(value="xpathImplementation", required=false) Integer intXPathImpl,
    			@RequestParam(value="uuidImplementation", required=false) Integer intUuidImpl,
    			@RequestParam(value="randomIntegerImplementation", required=false) Integer intRandomIntImpl,
    			@RequestParam(value="processingItems", required=false) Integer intProcessingItems,
    			@RequestParam(value="processingIterations", required=false) Integer intProcessingIterations,
    			@RequestParam(value="fixedDelayMilliseconds", required=false) Integer intFixedDelayMilliseconds,
    			@RequestParam(value="xpathFactoryCache", required=false) Boolean ynXPathFactoryCache,
    			@RequestParam(value="docBuilderCache", required=false) Boolean ynDocBuilderCache,
    			@RequestParam(value="fileCache", required=false) Boolean ynFileCache
    			) throws IOException {
    	
   		getConfig().setCurrentLogLevel(intLogLevel);
   		getConfig().setXPathImplementation(intXPathImpl);
   		getConfig().setUuidImplementation(intUuidImpl);
   		getConfig().setRandomIntegerImplementation(intRandomIntImpl);
   		getConfig().setFileCacheEnabled(ynFileCache);
   		getConfig().setXPathFactoryCacheEnabled(ynXPathFactoryCache);
   		getConfig().setDocBuilderCacheEnabled(ynDocBuilderCache);
   		getConfig().setProcessingItems(intProcessingItems);
   		getConfig().setProcessingIterations(intProcessingIterations);
   		getConfig().setFixedDelayMilliseconds(intFixedDelayMilliseconds);
    	
    	return getConfig().toXmlString();
    }	
	static Config getConfig() {
		return Config.SINGLETON;
	}
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Controller.class, args);
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

}