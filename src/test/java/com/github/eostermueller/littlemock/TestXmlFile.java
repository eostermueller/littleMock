package com.github.eostermueller.littlemock;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.eostermueller.littlemock.xslt.XmlFile;

/**
 * Todo: create test that validates file contents can be read from classpath.
 * @author erikostermueller
 *
 */
public class TestXmlFile {

	@Test
	public void canCreateOneXmlFile() {
		String cfg = 	"xsl.root/01/personnel2.xml";
		XmlFile xml = new XmlFile(cfg);
		assertEquals("xsl.root/01/personnel2.xml", xml.getFullName() );
		assertEquals("personnel2.xml", xml.getName() );
		assertEquals("xml", xml.getExtension() );
		assertEquals("xsl.root/01", xml.getPath() );
			
	}
	@Test
	public void canTrimOutTrailingAndLeadingSpaces() {
		String cfg = 	"   xsl.root/01/personnel2.xml ";
		XmlFile xml = new XmlFile(cfg);
		assertEquals("xsl.root/01/personnel2.xml", xml.getFullName() );
		assertEquals("personnel2.xml", xml.getName() );
		assertEquals("xml", xml.getExtension() );
		assertEquals("xsl.root/01", xml.getPath() );
			
	}
	@Test
	public void canTrimOutCrLf() {
		String cfg = 	"\nxsl.root/01/personnel2.xml\n";
		XmlFile xml = new XmlFile(cfg);
		assertEquals("xsl.root/01/personnel2.xml", xml.getFullName() );
		assertEquals("personnel2.xml", xml.getName() );
		assertEquals("xml", xml.getExtension() );
		assertEquals("xsl.root/01", xml.getPath() );
			
	}
	@Test
	public void canDetectXslt() {
		String cfg = 	"\nxsl.root/01/simple.xsl\n";
		XmlFile xml = new XmlFile(cfg);
		assertEquals("xsl.root/01/simple.xsl", xml.getFullName() );
		assertTrue(xml.isXsltExtention());
	}
	@Test
	public void canDetectNonXslt() {
		String cfg = 	"\nxsl.root/01/personnel2.xml\n";
		XmlFile xml = new XmlFile(cfg);
		assertEquals("xsl.root/01/personnel2.xml", xml.getFullName() );
		assertFalse(xml.isXsltExtention());
	}

}
