package de.unidue.ltl.hci;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import de.unidue.ltl.hci.langid.BaselineLanguageIdentifier;
import de.unidue.ltl.hci.langid.LanguageIdentifier;
import de.unidue.ltl.hci.langid.NgramCharLangId;

/**
 * A simple implementation of a natural language human-computer interaction system like Siri, Cortana or ok-Google.
 */
public class MySiri
{

    public static void main(String[] args)
        throws Exception
    {
    	// we ignore speech input for now, but assume that we already have the text input

    	MySiri siri = new MySiri();

    	siri.ask("What is your name?");

    	siri.interactive();
    }

    private LanguageIdentifier langIdent;

    public MySiri() {

    	try {
			langIdent = new NgramCharLangId(3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}


    /**
     * Ask Siri a question
     */
    public String ask(String question)
    	throws Exception
    {
    	// first we want to identify the language
    	System.out.println(langIdent.identifyLanguage(question));

    	return "Go help yourself!";
    }

    /**
     * Interactive mode
     */
    public void interactive()
    	throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("How can I help you?");
        String line = "";
        while((line = reader.readLine()) != null) {

        	ask(line);

        	System.out.println("why are you asking me '" + line + "'");
        }
    }
}