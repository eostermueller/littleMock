package com.github.eostermueller.littlemock;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.eostermueller.littlemock.Config;
import com.github.eostermueller.littlemock.perfkey.CodeAndValue;
import com.github.eostermueller.littlemock.perfkey.CodeDoesNotExist;
import com.github.eostermueller.littlemock.perfkey.DuplicateKeyOption;
import com.github.eostermueller.littlemock.perfkey.InvalidOption;
import com.github.eostermueller.littlemock.perfkey.PerformanceKey;

public class TestPerformanceKeys {

	@Test
	public void canParseSingleOption() throws DuplicateKeyOption, InvalidOption {
		CodeAndValue codeAndValue = new CodeAndValue("A2");
		assertEquals("A2", codeAndValue.getCodeAndValue() ); /* sanity check */
		
		assertEquals("A", codeAndValue.getCode() );
		assertEquals("2", codeAndValue.getValue() );
		assertEquals(2, codeAndValue.getIntValue() );
	}

	@Test
	public void canParseExistingKey() throws DuplicateKeyOption, InvalidOption {
		String myPerfKey = "X2,J25,K26,L0,Q,S,A10,B65535,C0,D10"; //taken/altered frm ch12 of tjp
		
		Config c = new Config();
		PerformanceKey pk = new PerformanceKey(c, myPerfKey);
		pk.parse();
		
		/* X2 */ assertEquals(2,c.getXPathImplementation() );
		
		/* J25 */ assertEquals(25,c.getProcessingItems() );
		
		/* K26 */ assertEquals(26,c.getProcessingIterations() );
		
		/* L0 */ assertEquals(0,c.getFixedDelayMilliseconds() );
		
		/* Q */ assertEquals(true, c.isFileCacheEnabled() );
		
		/* S */ assertEquals(0, c.getRandomIntegerImplementation() );
		
		/* A10 */ assertEquals(10, c.getOldGenRequestCountThresholdForPruning() );
		
		/* B65535 */  assertEquals( 65535, c.getOldGenMaxBytes() );
		
		/* C0 */  assertEquals( 0, c.getOldGenMinExpirationMs() );
		
		/* D10 */  assertEquals( 10, c.getOldGenMaxExpirationMs() );
		
	}
	
	@Test
	public void canDetectMissingNumeric() throws InvalidOption {
		String myPerfKey = "A";
		try {
			Config c = new Config();
			PerformanceKey pk = new PerformanceKey(c, myPerfKey);
			pk.parse();
			fail("should have thrown exception indicating that 'e' was an invalid option.");
		} catch (InvalidOption io) {
			assertEquals("A",io.getCodeAndValue() );
			assertNotNull(io.getNumberFormatException() );
		}
	}
	@Test
	public void canDetectDuplicateKey() throws InvalidOption {
		String myPerfKey = "A1,A2";
		
		try {
			
			Config c = new Config();
			PerformanceKey pk = new PerformanceKey(c, myPerfKey);
			pk.parse();
			fail("should have thrown exception indicating that the same code was found twice.");
		} catch (DuplicateKeyOption dpko) {
			assertEquals(myPerfKey,dpko.getRaw());
			assertEquals(dpko.getOldOption(),"A1");
			assertEquals(dpko.getNewOption(),"A2");
		}
		
	}
	@Test
	public void canDetectNonExistentCode() throws InvalidOption {
		String myPerfKey = "e3,A10";
		
		try {
			Config c = new Config();
			PerformanceKey pk = new PerformanceKey(c, myPerfKey);
			pk.parse();
			fail("should have thrown exception indicating that 'e' was an invalid option.");
		} catch (CodeDoesNotExist cdne) {
			assertEquals("e3",cdne.getCodeAndValue() );
			assertEquals("e",cdne.getInvalidCode() );
		}
	}
	
	@Test
	public void canDetectDuplicateBooleanKey() throws InvalidOption {
		//E0 does not exist, let's see if we can detect it.
		String myPerfKey = "X2,Q,J25,K26,Q,S,A10,B65535,C0,D10"; //taken/altered frm ch12 of tjp
		
		try {
			Config c = new Config();
			PerformanceKey pk = new PerformanceKey(c, myPerfKey);
			pk.parse();
			fail("should have thrown exception indicating that the same code was found twice.");
		} catch (DuplicateKeyOption dpko) {
			assertEquals(myPerfKey,dpko.getRaw());
			assertEquals(dpko.getOldOption(),"Q");
			assertEquals(dpko.getNewOption(),"Q");
		}
	}

}
