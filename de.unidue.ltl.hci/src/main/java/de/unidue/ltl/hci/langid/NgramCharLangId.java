package de.unidue.ltl.hci.langid;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.ngrams.util.NGramStringIterable;
import dkpro.toolbox.core.util.CondFreqDist;
import dkpro.toolbox.core.util.FreqDist;

public class NgramCharLangId
	implements LanguageIdentifier
{

	private CondFreqDist<String,String> cfd;
	
	private Set<String> languages;
	
	private int n;
	
	public NgramCharLangId(int n)
		throws Exception
	{
		this.n = n;
		
		languages = new HashSet<>();
		languages.add("German");
		languages.add("English");
		languages.add("French");

		cfd = new CondFreqDist<String, String>();

		CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
				TextReader.class,
				TextReader.PARAM_SOURCE_LOCATION, "src/main/resources/eval_langid/",
				TextReader.PARAM_PATTERNS, new String[]{
					"de/0*.txt",
					"en/0*.txt",
					"fr/0*.txt"
				}
		);
		
		
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
			if (languages.contains(longLanguage)) {
				for (String token : jcas.getDocumentText().split(" ")) {
					int m = n;
					if (n>1) {
						m = n-1;
					}
					for (String ngram : new NGramStringIterable(token.split(""), m, n)) {
						cfd.inc(longLanguage, ngram.toLowerCase());					
					}
				}				
			}
		}
	}
	
	public String identifyLanguage(String text) 
		throws Exception
	{
		System.out.println(text);
		String maxLanguage = "";

		int maxScore = Integer.MAX_VALUE;
		for (String language : cfd.getConditions()) {
			FreqDist<String> fd = cfd.getFrequencyDistribution(language);

			int sum = 0;
			for (String ngram : new NGramStringIterable(text.split(""), n, n)) {
			
				long count = fd.getCount(ngram);
				
				// normalization
				long normCount;
				if (n>1) {
					normCount = fd.getCount(ngram.substring(0, n-1));
				}
				else {
					normCount = fd.getN();
				}
				
				// LaPlace smoothing
				if (count == 0) {
					count = 1;
				}
				if (normCount == 0) {
					normCount = 1;
				}
				sum += Math.log((double) count / normCount);
			}
			
			if (Math.abs(sum) < maxScore) {
				maxLanguage = language;
				maxScore = Math.abs(sum);
			}
		}
		
		return maxLanguage;
	}
}