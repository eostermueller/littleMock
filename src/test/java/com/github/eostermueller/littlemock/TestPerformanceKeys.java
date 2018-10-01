package com.github.eostermueller.littlemock;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.eostermueller.littlemock.Config;
import com.github.eostermueller.littlemock.perfkey.CodeAndValue;
import com.github.eostermueller.littlemock.perfkey.CodeDoesNotExist;
import com.github.eostermueller.littlemock.perfkey.DuplicateKeyOption;
import com.github.eostermueller.littlemock.perfkey.InvalidCode;
import com.github.eostermueller.littlemock.perfkey.PerformanceKey;

public class TestPerformanceKeys {

	@Test
	public void canParseSingleOption() throws DuplicateKeyOption, InvalidCode {
		CodeAndValue codeAndValue = new CodeAndValue("A2");
		assertEquals("A2", codeAndValue.getCodeAndValue() ); /* sanity check */
		
		assertEquals("A", codeAndValue.getCode() );
		assertEquals("2", codeAndValue.getValue() );
		assertEquals(2, codeAndValue.getIntValue() );
	}

	@Test public void canGetKeyFromDefaultConfig() throws InvalidCode {
		Config c = new Config();
		PerformanceKey pk = new PerformanceKey(c);
		String perfKey = pk.getKey();
		
		//assertEquals("X0,J100,K100,L0,Q0,S0,A0,B1024,C0,D60000", perfKey);
                assertEquals("X0,J100,K100,L0,M0,Q0,R0,S0,T0,A0,B1024,C0,D60000", perfKey);
	}
	@Test public void canGetKeyFromSomeConfig() throws InvalidCode {
		Config c = new Config();
		/* X2 */ c.setXPathImplementation( 2 );
		
		/* J25 */ c.setProcessingItems(25);
		
		/* K26 */ c.setProcessingIterations( 26 );
		
		/* L0 */ c.setFixedDelayMilliseconds( 0 );
		
		/* Q */ c.setFileCacheEnabled( true );
		
		/* S */ c.setRandomIntegerImplementation( 1 );
		
		/* A10 */ c.setOldGenRequestCountThresholdForPruning(10);
		
		/* B65535 */  c.setOldGenMaxBytes( 65535 );
		
		/* C0 */  c.setOldGenMinExpirationMs( 0 );
		
		/* D10 */  c.setOldGenMaxExpirationMs(10);
		
		PerformanceKey pk = new PerformanceKey(c);
		String perfKey = pk.getKey();
		
		//assertEquals("X2,J25,K26,L0,Q,S1,A10,B65535,C0,D10", perfKey);
                assertEquals("X2,J25,K26,L0,M0,Q1,R0,S1,T0,A10,B65535,C0,D10", perfKey);
	}
	
	@Test
	public void canParseExistingKey() throws DuplicateKeyOption, InvalidCode {
		String myPerfKey = "X2,J25,K26,L0,Q,S,A10,B65535,C0,D10"; //taken/altered frm ch12 of tjp
		
		Config c = new Config();
		PerformanceKey pk = new PerformanceKey(c, myPerfKey);
		pk.apply();
		
		/* X2 */ assertEquals(2,c.getXPathImplementation() );
		
		/* J25 */ assertEquals(25,c.getProcessingItems() );
		
		/* K26 */ assertEquals(26,c.getProcessingIterations() );
		
		/* L0 */ assertEquals(0,c.getFixedDelayMilliseconds() );
		
		/* Q */ assertEquals(true, c.isFileCacheEnabled() );
		
		/* S */ assertEquals(1, c.getRandomIntegerImplementation() );
		
		/* A10 */ assertEquals(10, c.getOldGenRequestCountThresholdForPruning() );
		
		/* B65535 */  assertEquals( 65535, c.getOldGenMaxBytes() );
		
		/* C0 */  assertEquals( 0, c.getOldGenMinExpirationMs() );
		
		/* D10 */  assertEquals( 10, c.getOldGenMaxExpirationMs() );
		
	}
	
	@Test
	public void canDetectMissingNumeric() throws InvalidCode {
		String myPerfKey = "A";
		try {
			Config c = new Config();
			PerformanceKey pk = new PerformanceKey(c, myPerfKey);
			pk.apply();
			fail("should have thrown exception indicating that 'e' was an invalid option.");
		} catch (InvalidCode io) {
			assertEquals("A",io.getCodeAndValue() );
			assertNotNull(io.getNumberFormatException() );
		}
	}
	@Test
	public void canDetectDuplicateKey() throws InvalidCode {
		String myPerfKey = "A1,A2";
		
		try {
			
			Config c = new Config();
			PerformanceKey pk = new PerformanceKey(c, myPerfKey);
			pk.apply();
			fail("should have thrown exception indicating that the same code was found twice.");
		} catch (DuplicateKeyOption dpko) {
			assertEquals(myPerfKey,dpko.getRaw());
			assertEquals(dpko.getOldOption(),"A1");
			assertEquals(dpko.getNewOption(),"A2");
		}
		
	}
	@Test
	public void canDetectNonExistentCode() throws InvalidCode {
		String myPerfKey = "e3,A10";
		
		try {
			Config c = new Config();
			PerformanceKey pk = new PerformanceKey(c, myPerfKey);
			pk.apply();
			fail("should have thrown exception indicating that 'e' was an invalid option.");
		} catch (CodeDoesNotExist cdne) {
			assertEquals("e3",cdne.getCodeAndValue() );
			assertEquals("e",cdne.getInvalidCode() );
		}
	}
	
	@Test
	public void canDetectDuplicateBooleanKey() throws InvalidCode {
		//E0 does not exist, let's see if we can detect it.
		String myPerfKey = "X2,Q,J25,K26,Q,S,A10,B65535,C0,D10"; //taken/altered frm ch12 of tjp
		
		try {
			Config c = new Config();
			PerformanceKey pk = new PerformanceKey(c, myPerfKey);
			pk.apply();
			fail("should have thrown exception indicating that the same code was found twice.");
		} catch (DuplicateKeyOption dpko) {
			assertEquals(myPerfKey,dpko.getRaw());
			assertEquals(dpko.getOldOption(),"Q");
			assertEquals(dpko.getNewOption(),"Q");
		}
	}

}
