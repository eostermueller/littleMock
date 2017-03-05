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
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(input);
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			Object xpathResult = xpath.evaluate(this.getXPath(), document, XPathConstants.BOOLEAN);
			
			Boolean b = (Boolean)xpathResult;
			rc = b.booleanValue();
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
}
