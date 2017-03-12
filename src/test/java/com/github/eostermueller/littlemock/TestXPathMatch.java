package com.github.eostermueller.littlemock;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TestXPathMatch {
	StringBuilder sb = null;
	@Test
	public void canFindResponseForXPathMatch() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		XPathWrapper xpr = new XPathWrapper("/foo/bar/hello/world");
		
		String input_01 = "<foo><bar><hello><world/></hello></bar></foo>";
		
		InputSource is = new InputSource(new StringReader(input_01));
		Document d = PlaybackRepository.SINGLETON.getDocBuilder().parse(is);
		assertTrue( xpr.matches(d));
	}
	@Test
	public void canMatchXPathWithoutNamespaces() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
//		XPathWrapper xpr = new XPathWrapper("/allergy:allergy/core:informationSource/core:informant/core:person/core:telecom");
		XPathWrapper xpr = new XPathWrapper("/allergy/informationSource/informant/person/telecom");
		
		String input_03 = sb.toString();
		
		InputSource is = new InputSource(new StringReader(input_03));
		Document d = PlaybackRepository.SINGLETON.getDocBuilder().parse(is);
		assertTrue( xpr.matches(d));
	}

	@Test
	public void canDetectXPathMiss() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		XPathWrapper c = new XPathWrapper("/foo/other");
		
		String input_01 = "<foo><bar><hello><world/></hello></bar></foo>";
		
		InputSource is = new InputSource(new StringReader(input_01));
		Document d = PlaybackRepository.SINGLETON.getDocBuilder().parse(is);
		
		assertFalse( c.matches(d));
	}
	
	@Before
	public void init() {
		sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<allergy>");
			sb.append("  <product code=\"\" codeSystem=\"\" displayName=\"\" version=\"\"> MyTypeOfAllerge</product>");
			sb.append("  <type code=\"\" codeSystem=\"\" displayName=\"\" version=\"\">420134006</type>");
			sb.append("  <date>2001-12-31T12:00:00</date>");
			sb.append("  <reaction code=\"\" codeSystem=\"\" displayName=\"\" version=\"\">reaction</reaction>");
			sb.append("    <severity code=\"\" codeSystem=\"\" displayName=\"\" version=\"\">reaction</severity>");
			sb.append("    <informationSource >");
			sb.append("    	<author>");
			sb.append("		    <name>");
			sb.append("		      <given>given</given>");
			sb.append("		      <given>given</given>");
			sb.append("		      <given>given</given>");
			sb.append("		      <given>given</given>");
			sb.append("		      <lastname>lastname</lastname>");
			sb.append("		    </name>");
			sb.append("		    <address>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("		      <city>Tombaugh</city>");
			sb.append("		      <stateOrProvince>CA</stateOrProvince>");
			sb.append("		      <zip>72953</zip>");
			sb.append("		      <country>USA</country>");
			sb.append("		    </address>");
			sb.append("		    <telecom prefered=\"false\" use=\"\" value=\"\"/>");
			sb.append("    	</author>");
			sb.append("    	<date>2001-12-31T12:00:00</date>");
			sb.append("    	<reference></reference>");
			sb.append("    	<informant>");
			sb.append("		  <person>");
			sb.append("		    <name>");
			sb.append("		      <given>given</given>");
			sb.append("		      <given>given</given>");
			sb.append("		      <given>given</given>");
			sb.append("		      <given>given</given>");
			sb.append("		      <given>given</given>");
			sb.append("		      <given>given</given>");
			sb.append("		      <given>given</given>");
			sb.append("		      <lastname>lastname</lastname>");
			sb.append("		    </name>");
			sb.append("		    <address>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("			  <streetAddress>10531 Pluto Way</streetAddress>");
			sb.append("		      <city>Tombaugh</city>");
			sb.append("		      <stateOrProvince>CA</stateOrProvince>");
			sb.append("		      <zip>72953</zip>");
			sb.append("		      <country>USA</country>");
			sb.append("		    </address>");
			sb.append("		    <telecom prefered=\"false\" use=\"\" value=\"\"/>");
			sb.append("		  </person>");
			sb.append("    	</informant>    ");
			sb.append("    </informationSource>");
			sb.append("    <description>");
			sb.append("    	<text>      ");
			sb.append("      This section is used to describe a patients allergies and intolerances to various substances.  The section defines a number of ");
			sb.append("      codedValue sections which are constrained to particular coding system (SNOMED CT) which are described below.");
			sb.append("      ");
			sb.append("      The date element is used to state the date of the last reaction to the allergy or intolerance.");
			sb.append("      ");
			sb.append("      The product element is a coded value used to describe the substance the patient has the allergy or intolerance too.  There currently is ");
			sb.append("      not a constraint defined on the code system or code values which can be used to coded the product. At the very least a free text description");
			sb.append("      the substance must be supplied as the value of the product element with implementers free to choose the coding system of their choice.");
			sb.append("      ");
			sb.append("      ");
			sb.append("      This section also contains optional informationSource and description elements which are defined and documented in the ");
			sb.append("      abstractSection type declared in the hdata core.xsd schema file.");
			sb.append("    	</text>");
			sb.append("    	  <codedValue code=\"\" codeSystem=\"\" displayName=\"\" version=\"\">reaction</codedValue>    	");
			sb.append("    </description>");
			sb.append("</allergy>");
			System.out.println( sb.toString() );
	}
}
