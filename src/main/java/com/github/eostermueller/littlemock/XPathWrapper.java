package com.github.eostermueller.littlemock;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class XPathWrapper {
	
	public XPathWrapper(String xpath) { 
		this.xpath = xpath;
	}
	private String xpath = null;
	public String getXPath() {
		return this.xpath;
	}
	boolean matches(Document document) throws XPathExpressionException {
		
		XPath xpath = null;
		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
		
		Object xpathResult = xpath.evaluate(this.getXPath(), document, XPathConstants.BOOLEAN);
		
		Boolean b = (Boolean)xpathResult;
		return b.booleanValue();
	}
}
