package de.unidue.ltl.hci.langid;

public interface LanguageIdentifier {

	/**
	 * @param text The input text
	 * @return The ISO two-letter language code of the text's language
	 */
	public String identifyLanguage(String text) throws Exception;
}
