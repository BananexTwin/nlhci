package de.unidue.ltl.hci.langid;

public class BaselineLanguageIdentifier 
	implements LanguageIdentifier
{
	private String languageCode;
	
	public BaselineLanguageIdentifier(String languageCode) {
		this.languageCode = languageCode;
	}
	
	public String identifyLanguage(String text) {
		return languageCode;
	}
}
