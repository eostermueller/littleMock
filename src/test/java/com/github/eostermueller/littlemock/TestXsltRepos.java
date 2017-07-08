package com.github.eostermueller.littlemock;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.eostermueller.littlemock.xslt.XmlFile;
import com.github.eostermueller.littlemock.xslt.XsltRepo;
import com.github.eostermueller.littlemock.xslt.XsltRepos;

public class TestXsltRepos {

	@Test
	public void canDivideFilesIntoSeparateReposAkaPaths() {
		String cfg1 = 	"xsl.root/01/fred.xml;xsl.root/01/barney.xml;xsl.root/01/wilma.xsl";
		String cfg2 = 	"xsl.root/02/george.xml;xsl.root/02/jane.xml;xsl.root/02/rosie.xsl";
		
		XsltRepos repositories = new XsltRepos(cfg1 + ";" + cfg2);
		
		boolean ynP1 = false;
		boolean ynP2 = false;
		int count = 0;
		for(XsltRepo repo : repositories.getRepos()) {
			
			if (repo.getPath().equals("xsl.root/01")) {
				ynP1 = true;
			} else if (repo.getPath().equals("xsl.root/02")) {
				ynP2 = true;
			}
			count++;
		}
		assertTrue(ynP1);
		assertTrue(ynP2);
		assertEquals(2,count);

	}

	/**
	 * This test is nearly identical to the above test.
	 * Repo count does not change, because count of unique paths stays the same.
	 * 
	 * It just adds more xml files to each of the 2 same repos from above.
	 * repo count does not change, even tho file count has gone up.
	 * 
	 */
	@Test
	public void canDivideFilesIntoSeparateReposAkaPaths2() {
		String cfg1 = 	"xsl.root/01/fred3.xml;xsl.root/01/fred2.xml;xsl.root/01/fred.xml;xsl.root/01/barney.xml;xsl.root/01/wilma.xsl";
		String cfg2 = 	"xsl.root/02/george5.xml;xsl.root/02/george4.xml;xsl.root/02/george3.xml;xsl.root/02/george.xml;xsl.root/02/jane.xml;xsl.root/02/rosie.xsl";
		
		XsltRepos repositories = new XsltRepos(cfg1 + ";" + cfg2);
		
		boolean ynP1 = false;
		boolean ynP2 = false;
		int count = 0;
		for(XsltRepo repo : repositories.getRepos()) {
			
			if (repo.getPath().equals("xsl.root/01")) {
				ynP1 = true;
			} else if (repo.getPath().equals("xsl.root/02")) {
				ynP2 = true;
			}
			count++;
		}
		assertTrue(ynP1);
		assertTrue(ynP2);
		assertEquals(2,count);
		

	}
	/**
	 * This test is nearly identical to the above test.
	 * Repo count does not change, because count of unique paths stays the same.
	 * 
	 * It just adds more xml files to each of the 2 same repos from above.
	 * repo count does not change, even tho file count has gone up.
	 * 
	 */
	@Test
	public void canDivideFilesIntoSeparateReposAkaPaths3() {
		String cfg1 = 	"	xsl.root/01/personnel.xml;  	xsl.root/01/simple.xsl;  		xsl.root/02/book.xml;			xsl.root/02/to-html.xsl;		xsl.root/03/sales.xml;			xsl.root/03/to-html.xsl;		xsl.root/04/sales.xml;			xsl.root/04/to-svg.xsl;			xsl.root/05/foo.xml;			xsl.root/05/foo.xsl;			xsl.root/06/birds.xml;			xsl.root/06/birds.xsl;    ";
		
		XsltRepos repositories = new XsltRepos(cfg1);
		
		int count = 0;
		for(XsltRepo repo : repositories.getRepos()) {
		
			assertTrue(repo.getPath().length() > 0);
			for(XmlFile xml : repo.getXmlFiles()) {
				assertNotNull(xml);
				
			}
			count++;
		}
		assertEquals(6,count);
		

	}

}
