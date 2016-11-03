package de.unidue.ltl.hci.langid.eval;

import java.io.File;
import java.net.URI;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.unidue.ltl.hci.langid.LanguageIdentifier;
import de.unidue.ltl.hci.langid.NgramCharLangId;

public class LandIdentEvaluator {

	public static void main(String[] args) 
		throws Exception
	{
		CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
				TextReader.class,
				TextReader.PARAM_SOURCE_LOCATION, "src/main/resources/eval_langid/",
				TextReader.PARAM_PATTERNS, new String[]{"de/0*.txt","en/0*.txt","fr/0*.txt"}
		);
		
		int n = 0;
		int correct = 0;
		
//		LanguageIdentifier languageIdentifier = new BaselineLanguageIdentifier("en");
//		LanguageIdentifier languageIdentifier = new UnigramCharLangId();
		LanguageIdentifier languageIdentifier = new NgramCharLangId(3);
		
		for (JCas jcas : new JCasIterable(reader)) {
			File file = new File(new URI(DocumentMetaData.get(jcas).getDocumentUri()));
			String[] parts = file.getParent().split("/");
			String language = parts[parts.length-1];
			String longLanguage = "";
			switch (language) {
				case "de" : longLanguage = "German"; break;
				case "en" : longLanguage = "English"; break;
				case "fr" : longLanguage = "French"; break;
				default: longLanguage = "German"; break;
			}
			
			String predictedLanguage = languageIdentifier.identifyLanguage(jcas.getDocumentText());
			
			System.out.println(predictedLanguage + " - " + longLanguage);
			
			if (predictedLanguage.equals(longLanguage)) {
				correct++;
			}
			
			n++;
		}
		
		System.out.println("Accuracy: " + (double) correct / n);
	}
}