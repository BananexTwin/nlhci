package de.unidue.ltl.hci.langid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dkpro.toolbox.core.util.CondFreqDist;
import dkpro.toolbox.core.util.FreqDist;
import dkpro.toolbox.corpus.categorized.CategorizedCorpus;
import dkpro.toolbox.corpus.categorized.UdhrCorpus;

public class UnigramCharLangId
	implements LanguageIdentifier
{

	private CondFreqDist<String,String> cfd;
	
	private Set<String> languages;
		
	public UnigramCharLangId()
		throws Exception
	{		
		languages = new HashSet<>();
		languages.add("German");
		languages.add("English");
		languages.add("French");

		cfd = new CondFreqDist<String, String>();

		CategorizedCorpus corpus = new UdhrCorpus();
		
		// sort for better overview
		List<String> sortedLanguages = new ArrayList<>(corpus.getCategories());
		Collections.sort(sortedLanguages);
		
		for (String language : sortedLanguages) {
			System.out.println(language);
			if (languages.contains(language)) {
				for (String token : corpus.getTokens(language)) {
					for (String character : token.split("")) {
						cfd.inc(language, character.toLowerCase());					
					}
				}				
			}
		}
	}
	
	public String identifyLanguage(String text) 
		throws Exception
	{
		String maxLanguage = "";

		int maxScore = Integer.MAX_VALUE;
		for (String language : cfd.getConditions()) {
			FreqDist<String> fd = cfd.getFrequencyDistribution(language);

			int sum = 0;
			for (String ngram : text.split("")) {
			
				long count = fd.getCount(ngram);
								
				if (count == 0) {
					count = 1;
				}

				sum += Math.log((double) count);
			}
			
			if (Math.abs(sum) < maxScore) {
				maxLanguage = language;
				maxScore = Math.abs(sum);
			}
		}
		
		return maxLanguage;
	}
}