package com.github.eostermueller.littlemock;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class TestXPathRepo {
	PlaybackRepository repo = new PlaybackRepository();
	private static String RESPONSE_01 = "<response>foo-bar-hello-world</response>";
	private static String RESPONSE_02 = "<response>other</response>";
	private static String RESPONSE_03 = "<response>otherMessage</response>";

	@Before
	public void setup() {
		
		repo.add("/foo/bar/hello/world", RESPONSE_01);
		repo.add("/foo/other", RESPONSE_02);
		repo.add("/foo/otherMessage", RESPONSE_03);
		
	}
	
	@Test
	public void canFindResponseForThisRequest() throws IOException {
		String input_01 = "<foo><bar><hello><world/></hello></bar></foo>";
		String resp = repo.getResponse( input_01 );
		assertEquals(RESPONSE_01,resp);
	}
	
	@Test
	public void canFindResponseForOtherRequest() throws IOException {
		String input_02 = "<foo><other/></foo>";
		String resp = repo.getResponse( input_02 );
		assertEquals(RESPONSE_02,resp);
	}
	@Test
	public void canFindResponseForOtherMessageRequest() throws IOException {
		String input_03 = "<foo><otherMessage/></foo>";
		String resp = repo.getResponse( input_03 );
		assertEquals(RESPONSE_03,resp);
	}

}
