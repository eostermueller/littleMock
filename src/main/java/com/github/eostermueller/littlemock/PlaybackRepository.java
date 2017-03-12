package com.github.eostermueller.littlemock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * For a "record-and-playback" stub server, this class
 * manages a repository of all configured responses to playback.
 * @author erikostermueller
 *
 */
public class PlaybackRepository {

	ConcurrentHashMap<String,String> cachedPlaybackResponseFiles = new ConcurrentHashMap<String,String>();
	public Map<String,String> getCache() {
		return this.cachedPlaybackResponseFiles;
	}

	public static PlaybackRepository SINGLETON = new PlaybackRepository();
	private StringBuilder humanReadableConfiguration = new StringBuilder();
	
	List<Config> configurations = new CopyOnWriteArrayList<Config>();
	private File rootFolder = null;
	private Map<String,Config> inputCache = new ConcurrentHashMap<String,Config>();
	private Map<String,Document> domCache = new ConcurrentHashMap<String,Document>();

	public File getRootFolder() {
		return rootFolder;
	}
	public void setRootFolder(File rootFolder) {
		this.rootFolder = rootFolder;
	}


	public int getCount() {
		return this.configurations.size();
	}
	/**
	 * When request matches xpath, return 'response' text.
	 * @param xpath
	 * @param response
	 */
	public void add(String xpath, String response) {
		Config c = new Config(xpath,response);
		this.configurations.add(c);
	}

	public void addFile(String xpath, String responseFile) {
		Config c = new Config(xpath,new File(getRootFolder(), responseFile));
		this.configurations.add(c);

		this.humanReadableConfiguration.append(c.humanReadable()).append("\n");
	}
	public String humanReadable() {
		return this.humanReadableConfiguration.toString();
	}


	public String getResponse(String input) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		Config c = null;
		
		switch( Controller.getConfig().getXPathImplementation() ) {
			case 0:
				c = locateConfig_noCaching(input);
				break;
			case 1:
				c = locateConfig_domCaching(input);
				break;
			case 2:
				c = locateConfig_respCaching(input);
				break;
		}
		String rc = null;
		if (c!=null)
			rc = c.getResponseText();
		
		return rc;
	}
	public Config locateConfig_noCaching(String input) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		return getConfigByXPath(input);
	}
	public Config locateConfig_domCaching(String input) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		Document doc = this.domCache.get(input);
		if (doc == null) {
			InputSource is = new InputSource( new StringReader(input));
			doc = this.getDocBuilder().parse(is);
			this.domCache.put(input, doc);
			if (Controller.isDebug())
				Controller.logDebug("added this to domCache [" + input + "]");
		}
		Config c = this.getConfigByXPath(doc);
		return c;
	}
	public Config locateConfig_respCaching(String input) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {

		Config c = this.inputCache.get(input);
		if (Controller.isDebug())
			Controller.logDebug("inputCache lookup [" + (c==null ? false : true)  + "]  using this input as key:[" + input + "]");

		if (c==null) {
			c = getConfigByXPath(input);
			if (c!=null) {
				this.inputCache.put(input, c);
				if (Controller.isDebug())
					Controller.logDebug("added this to inputCache [" + input + "]");
			} else {
				Controller.logAlways("none of the xpaths in application.properties seem to match [" + input + "]");
			}
		}
		return c;
	}	
	/**
	 * For the given http input, return the text for the http response.
	 * @param input
	 * @return
	 * @throws IOException
	 * @throws XPathExpressionException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public Config getConfigByXPath(String input) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		Config rc = null;
		for(Config c : this.configurations) {
			Controller.logDebug("Does xpath [" + c.xpath.getXPath() + "] match input [" + input + "] ?");

			InputSource is = new InputSource( new StringReader(input));
			Document d = this.getDocBuilder().parse(is);
			if (c.xpath.matches(d)) {
				rc = c;
				break;
			}
		}
		return rc;
	}
	private ThreadLocal<DocumentBuilder> myThreadLocalDocBuilder = new ThreadLocal<DocumentBuilder>() {
	    @Override protected DocumentBuilder initialValue() {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	        return db;
	    }
	};    	
	
	public DocumentBuilder getDocBuilder() throws ParserConfigurationException {
		return getDocBuilder( Controller.getConfig().isDocBuilderCacheEnabled() );
	}
	private DocumentBuilder getDocBuilder(boolean ynCache) throws ParserConfigurationException {
		DocumentBuilder db = null;
		if (ynCache)
			return this.myThreadLocalDocBuilder.get();
		else {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
		}
		return db;
	}
	/**
	 * For the given http input, return the text for the http response.
	 * @param input
	 * @return
	 * @throws IOException
	 * @throws XPathExpressionException 
	 */
	public Config getConfigByXPath(Document input) throws IOException, XPathExpressionException {
		Config rc = null;
		for(Config c : this.configurations) {
			Controller.logDebug("Does xpath [" + c.xpath.getXPath() + "] match input [" + input + "] ?");

			if (c.xpath.matches(input)) {
				rc = c;
				break;
			}
		}
		return rc;
	}

	public PlaybackRepository(File root) {
		this.rootFolder = root;
	}

	public PlaybackRepository() {
	}

	public class Config {
		public String humanReadable() {
			return "IF input matches xpath [" + this.xpath.getXPath() + "] THEN return file [" + this.responseFile.getName() + "]";
		}
		
		XPathWrapper xpath = null;
		private String responseText = null;
		private File responseFile = null;

		public Config(String xpath2, String response) {
			xpath = new XPathWrapper(xpath2);
			this.responseText = response;
		}

		public Config(String xpath2, File file) {
			xpath = new XPathWrapper(xpath2);
			this.responseFile = file;
			Controller.logDebug("If HTTP input contains xpath [" + xpath2 + "], then return contents of [" + file.getAbsolutePath() + "]");
		}

		public String getResponseText() throws IOException {
			String rc = null;
			if (responseText != null) {
				//This branch is an ugly shortcut, placed here for unit tests only...
				//I didn't want my unit tests to be uglified with all the stuff required to read from files.
				rc = responseText;
			} else {
				if (Controller.getConfig().isFileCacheEnabled()) {
					rc = PlaybackRepository.SINGLETON.getCache().get(getKey());
					if (rc==null) {
						rc = readPhysicalFile();
						PlaybackRepository.SINGLETON.getCache().put( getKey(), rc);
					}
				} else {
					rc = readPhysicalFile();
				}
			}
			return rc;
		}

		private String getKey() {
			return this.responseFile.getName();
		}

		private String readPhysicalFile() throws IOException {
			String rc = null;
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(this.responseFile));
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			    }
			    rc = sb.toString();
			} catch(Exception e) {
				Controller.logAlways("Exception reading [" + this.responseFile.getAbsolutePath() + "]", new Exception());
			} finally {
				if (br!=null)
					br.close();
			}
			return rc;
		}

	}
}
