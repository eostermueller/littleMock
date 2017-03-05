package com.github.eostermueller.littlemock;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class Controller implements EnvironmentAware {

	private static AtomicInteger currentLogLevel = new AtomicInteger(0); 
	private static AtomicInteger xpathImplementation = new AtomicInteger(0); 
	private static AtomicBoolean fileCacheEnabled = new AtomicBoolean(false);
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
    		Controller.logInfo("####");
    		Controller.logInfo("HTTP rq size: [" + lpad(""+input.length(),7) + "] bytes. Looking for match.");
    	    rc = this.getRepository().getResponse(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        return rc;
    }
    @RequestMapping(value="/config", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String config(
    			@RequestParam(value="logLevel", required=false) Integer intLogLevel,
    			@RequestParam(value="xpathImplementation", required=false) Integer intXPathImpl,
    			@RequestParam(value="fileCache", required=false) Boolean ynFileCache
    			) throws IOException {
    	
    	if (intLogLevel!=null) {
    		this.setLogLevel(intLogLevel);
    		Controller.logAlways("Log level set to [" + this.getLogLevel() + "]");
    	}
    	
    	if (intXPathImpl!=null) {
    		Controller.setXPathImpl(intXPathImpl.intValue());
    		Controller.logAlways("xpath implementation set to [" + Controller.getXPathImpl() + "]");
    	}

    	if (ynFileCache!=null) {
    		Controller.setFileCacheEnabled(ynFileCache.booleanValue());
    		Controller.logAlways("fileCache set to [" + Controller.isFileCacheEnabled() + "]");
    	}
    	
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append("<config>");
    	sb.append("\n    <logLevel>").append(""+Controller.getLogLevel() ).append("    </logLevel>");
    	sb.append("\n    <fileCache>").append(""+Controller.isFileCacheEnabled() ).append("    </fileCache>");
    	sb.append("\n    <xpathImplementation>").append(""+Controller.getXPathImpl() ).append("    </xpathImplementation>");
    	sb.append("\n</config>");
    	return sb.toString();
    }	
	String httpServletRequestToString(HttpServletRequest request) throws Exception {

        ServletInputStream mServletInputStream = request.getInputStream();
        byte[] httpInData = new byte[request.getContentLength()];
        int retVal = -1;
        StringBuilder stringBuilder = new StringBuilder();

        while ((retVal = mServletInputStream.read(httpInData)) != -1) {
            for (int i = 0; i < retVal; i++) {
                stringBuilder.append(Character.toString((char) httpInData[i]));
            }
        }

        return stringBuilder.toString();
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Controller.class, args);
    }
    
    /**
     * @stolenFrom:  https://en.wikipedia.org/wiki/Double-checked_locking and https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
     * @return
     */
    private PlaybackRepository getRepository() {
    	PlaybackRepository result = this.repo;
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
				Controller.logAlways("Loading sequence [" + seq + "] xpath[" + xpath + "] data file [" + responseTextFileName + "]" );
				
			} else {
				Controller.logAlways("Either [" + xpathPropName + "] or [" + responseTextFilePropName + "] was not found in application.properties.  No more properties will be loaded.");
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
	public void setLogLevel(int logLevel) {
		Controller.currentLogLevel.set(logLevel);
	}
	public static String rpad(String str, int num) {
		    return String.format("%1$-" + num + "s", str);
	 }
	public static String lpad(String str, int num) {
	    return String.format("%1$" + num + "s", str);
 }
	public static int getLogLevel() {
		return Controller.currentLogLevel.get();
	}
	public static boolean isDebug() {
		boolean yn = false;
		if( Controller.getLogLevel() >= 2 )
			yn = true;
		return yn;
	}
	public static boolean isInfo() {
		boolean yn = false;
		if( Controller.getLogLevel() >= 1 )
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
	public static void setFileCacheEnabled(boolean val) {
		Controller.fileCacheEnabled.set(val);
	}
	public static boolean isFileCacheEnabled() {
		return Controller.fileCacheEnabled.get();
	}
	public static int getXPathImpl() {
		return Controller.xpathImplementation.intValue();
	}
	private static void setXPathImpl(int intValue) {
		Controller.xpathImplementation.set(intValue);
		
	}
}