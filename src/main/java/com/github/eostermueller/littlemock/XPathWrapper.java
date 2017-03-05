package com.github.eostermueller.littlemock;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XPathWrapper {
	
	Object domLock = new Object();
	volatile Document inputDoc = null;
	
	public XPathWrapper(String xpath) { 
		this.xpath = xpath;
		sequence = ++currentSequence;
	}
	private static int currentSequence = 0;
	int sequence = 0;
	private String xpath = null;
	public String getXPath() {
		return this.xpath;
	}
	boolean matches(InputSource input) {
		Controller.logDebug("Does input [" + input + "] match xpath [" + this.xpath + "]");
		boolean rc = false;
		try {
			
			Document document = null;
			document = XPathWrapper.parse(input);
			rc = matches(document);
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Controller.logInfo("XPath match result [" + Controller.rpad(""+rc,5) 
				+ "] for seq[" + Controller.lpad(""+sequence,3) + "][" 
				+ Controller.rpad(xpath,60) + "] input[" + input + "]");
		return rc;
	}
	boolean matches(Document document) throws XPathExpressionException {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		Object xpathResult = xpath.evaluate(this.getXPath(), document, XPathConstants.BOOLEAN);
		
		Boolean b = (Boolean)xpathResult;
		return b.booleanValue();
	}
	public static Document parse(InputSource input) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(input);
		return document;
	}
	/**
	 * https://en.wikipedia.org/wiki/Double-checked_locking
	 * @param input
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document getCachedDom(InputSource input) throws ParserConfigurationException, SAXException, IOException {
		
		Document document = inputDoc;
		if (document==null) {
			synchronized(this.domLock) {
				if (document==null) {
					this.inputDoc = this.parse(input);
				}
			}
		}
		return this.inputDoc;
	}
}
