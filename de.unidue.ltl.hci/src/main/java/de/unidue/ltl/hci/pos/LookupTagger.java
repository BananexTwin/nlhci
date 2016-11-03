package de.unidue.ltl.hci.pos;

import java.util.ArrayList;
import java.util.List;

import dkpro.toolbox.core.Sentence;
import dkpro.toolbox.core.TaggedToken;
import dkpro.toolbox.core.util.CFD;
import dkpro.toolbox.corpus.BrownCorpus;
import dkpro.toolbox.corpus.analyzed.AnalyzedCorpus;
import dkpro.toolbox.corpus.analyzed.CorpusManager;
import dkpro.toolbox.corpus.analyzed.CorpusManager.CorpusName;

public class LookupTagger {

	private CFD<String, String> wordTagCfd;

	public static void main(String[] args) 
		throws Exception
	{
	    	// split in train/test
		AnalyzedCorpus brown = new AnalyzedCorpus(new BrownCorpus());
	    	List<Sentence> sentences = brown.getSentenceList();    	
     
	    	int splitOffset = (int) Math.round(sentences.size() * 0.005);
	    	List<Sentence> train = sentences.subList(0, splitOffset);
	    	List<Sentence> test = sentences.subList(splitOffset, sentences.size());
	    	System.out.println("Train size: " + train.size());
	    	System.out.println("Test size: " + test.size());
	    	
	    AnalyzedCorpus moby = CorpusManager.getCorpus(CorpusName.MobyDick);

	    	LookupTagger tagger = new LookupTagger();
		tagger.train(sentences);
		tagger.evaluate(moby.getSentenceList());
	}
	
	public void train(List<Sentence> sentences) 
		throws Exception
	{
        wordTagCfd = new CFD<String, String>();
        for (Sentence sentence : sentences)
        {
	        	for (TaggedToken taggedToken : sentence.getTaggedTokens()) 
	        	{
	            wordTagCfd.inc(
		        		taggedToken.getToken().toLowerCase(),
		        		taggedToken.getTag().getCanonicalTag()
		        	);
	        }
        }
    }
	
	public List<String> tag(Sentence sentence) {
		List<String> tags = new ArrayList<String>();
		
		for (String token : sentence.getTokens()) {
			String condition = token.toLowerCase();
			String predictedTag;
			if (wordTagCfd.hasCondition(condition)) {
				predictedTag = wordTagCfd.getFrequencyDistribution(condition).getSampleWithMaxFreq();		
			}
			else {
				predictedTag = "NN";
			}
			tags.add(predictedTag);
		}
		
		return tags;
	}
	
    public void evaluate(List<Sentence> sentences)
        throws Exception
    {       
        int correct = 0;
        int n = 0;
        
        for (Sentence sentence : sentences) {
            List<String> predictedTags = this.tag(sentence);
            List<TaggedToken> goldTags = sentence.getTaggedTokens();
            
            for (int i=0; i<predictedTags.size(); i++)
	        	{
	            	String predicted = predictedTags.get(i);
	            	String gold = goldTags.get(i).getTag().getCanonicalTag();
	            	if (predicted.equals(gold)) {
	            		correct++;
	            	}
	            	n++;
	        	}
        }
        System.out.println((double) correct / n);
    }
}
