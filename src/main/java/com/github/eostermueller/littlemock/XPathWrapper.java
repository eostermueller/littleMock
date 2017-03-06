package com.github.eostermueller.littlemock;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

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
	private ThreadLocal<XPath> myThreadLocalXPath = new ThreadLocal<XPath>() {
	    @Override protected XPath initialValue() {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
	    	
	        return xpath;
	    }
	};    	
	XPath getXPathObject() throws XPathExpressionException {
		return getXPathObject(Controller.xpathFactoryCache());
	}
	XPath getXPathObject(boolean ynCache) throws XPathExpressionException {
		XPath xpath = null;
		if (ynCache )
			xpath = myThreadLocalXPath.get();
		else {
			XPathFactory factory = XPathFactory.newInstance();
			xpath = factory.newXPath();
		}
		return xpath;
			 
	}
	boolean matches(Document document) throws XPathExpressionException {
		return matches(document, this.getXPathObject() );
	}
	boolean matches(Document document, XPath xpath) throws XPathExpressionException {
		
		Object xpathResult = xpath.evaluate(this.getXPath(), document, XPathConstants.BOOLEAN);
		
		Boolean b = (Boolean)xpathResult;
		return b.booleanValue();
	}
}
