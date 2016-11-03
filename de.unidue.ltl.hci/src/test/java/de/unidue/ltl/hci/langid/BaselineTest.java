package de.unidue.ltl.hci.langid;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaselineTest {

	@Test
	public void baselineTest() 
		 throws Exception
	{
		LanguageIdentifier langIdent = new BaselineLanguageIdentifier("de");
		assertEquals("de", langIdent.identifyLanguage("die der das"));
		assertEquals("de", langIdent.identifyLanguage(""));
		assertEquals("de", langIdent.identifyLanguage(null));
	}
}
