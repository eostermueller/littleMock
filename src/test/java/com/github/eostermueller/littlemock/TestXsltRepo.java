package com.github.eostermueller.littlemock;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.eostermueller.littlemock.xslt.XmlFile;
import com.github.eostermueller.littlemock.xslt.XsltFile;
import com.github.eostermueller.littlemock.xslt.XsltRepo;

public class TestXsltRepo {

	@Test
	public void canDetectXmlAndXsltFiles() {
		XsltRepo repo2 = new XsltRepo();
		String cfg = 	"xsl.root/01/personnel2.xml;xsl.root/01/personnel.xml;xsl.root/01/simple.xsl";
		String[] parts = cfg.split(";");
		for(String oneFileName : parts) {
			XmlFile x = new XmlFile(oneFileName);
			repo2.addFile(x);
		}

		boolean ynP1 = false;
		boolean ynP2 = false;
		
		for(XmlFile xml : repo2.getXmlFiles() ) {
			if (xml.getFullName().equals("xsl.root/01/personnel.xml"))
				ynP1 = true;
			if (xml.getFullName().equals("xsl.root/01/personnel2.xml"))
				ynP2 = true;
		}
		XsltFile xsltFile = repo2.getXslt();
		assertNotNull(xsltFile);
		assertEquals("xsl.root/01/simple.xsl", xsltFile.getFullName());
		
		assertTrue(ynP1);
		assertTrue(ynP2);

	}

}
