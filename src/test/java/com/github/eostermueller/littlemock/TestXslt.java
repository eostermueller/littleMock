package com.github.eostermueller.littlemock;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.eostermueller.littlemock.xslt.XsltProcessor;
import com.github.eostermueller.littlemock.xslt.XsltRepo;

/**
 * Need to turn the System.out.println() into assertions.
 * ....but for now this does some basic validation that xslt works.
 * @author erikostermueller
 *
 */
public class TestXslt {

	XsltProcessor xsltProcessor = null;
	
	@Before
	public void setup() {
		String cfg1 = 	"	xsl.root/01/personnel.xml;  	xsl.root/01/simple.xsl;  		xsl.root/02/book.xml;			xsl.root/02/to-html.xsl;		xsl.root/03/sales.xml;			xsl.root/03/to-html.xsl;		xsl.root/04/sales.xml;			xsl.root/04/to-svg.xsl;			xsl.root/05/foo.xml;			xsl.root/05/foo.xsl;			xsl.root/06/birds.xml;			xsl.root/06/birds.xsl;    ";
		xsltProcessor = new XsltProcessor(cfg1);
		System.out.println(xsltProcessor.getHumanReadableFileList());
	}
	@Test
	public void readFiles() {
		for(XsltRepo repo : this.xsltProcessor.getRepos().getRepos()) {
			System.out.println(" file [" + convertStreamToString( repo.getXslt().getFileContents() ) + "]" );
		}
		
	}
	@Test
	public void testPooled() {
		String rc = this.xsltProcessor.pooledTransformerXslt();
		System.out.println(rc);
	}
	@Test
	public void testUnPooled() {
		String rc = this.xsltProcessor.unPooledTransformerXslt();
		System.out.println(rc);
	}
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
