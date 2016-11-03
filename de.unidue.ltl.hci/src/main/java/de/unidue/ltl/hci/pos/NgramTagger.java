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

public class NgramTagger {

	private CFD<String, String> wordTagCfd;

	public static void main(String[] args) 
		throws Exception
	{
	    	// split in train/test
		AnalyzedCorpus brown = new AnalyzedCorpus(new BrownCorpus());
    	List<Sentence> sentences = brown.getSentenceList();    	
 
    	int splitOffset = (int) Math.round(sentences.size() * 0.8);
    	List<Sentence> train = sentences.subList(0, splitOffset);
    	List<Sentence> test = sentences.subList(splitOffset, sentences.size());
    	System.out.println("Train size: " + train.size());
    	System.out.println("Test size: " + test.size());
    	
	    AnalyzedCorpus moby = CorpusManager.getCorpus(CorpusName.MobyDick);

	    NgramTagger tagger = new NgramTagger();
		tagger.train(train);
	    System.out.println("Test on train");
		tagger.evaluate(train);
	    System.out.println("Test on test");
		tagger.evaluate(test);
	    System.out.println("Test on Moby");
		tagger.evaluate(moby.getSentenceList());		
	}
	
	public void train(List<Sentence> sentences) 
		throws Exception
	{
        wordTagCfd = new CFD<String, String>();
        for (Sentence sentence : sentences)
        {
			String previousTag = "<s>";
    		for (TaggedToken taggedToken : sentence.getTaggedTokens())
        	{
    			String token = taggedToken.getToken().toLowerCase();
    			String tag = taggedToken.getTag().getCanonicalTag();
	            wordTagCfd.inc(
		        		previousTag + "/" + token,
		        		tag
	            	);
	            wordTagCfd.inc(
		        		token,
		        		tag
	            	);
	            
	            previousTag = tag;
	        }
        }
    }
	
	public List<String> tag(Sentence sentence) {
		List<String> tags = new ArrayList<String>();
		
		String previousTag = "<s>";
		for (String token : sentence.getTokens()) {
			token = token.toLowerCase();
			
			String context = previousTag + "/" + token;
			
			String predictedTag;
			// bigram
			if (wordTagCfd.hasCondition(context)) {
				predictedTag = wordTagCfd.getFrequencyDistribution(context).getSampleWithMaxFreq();		
			}
			// unigram backoff
			else if (wordTagCfd.hasCondition(token)) {
				predictedTag = wordTagCfd.getFrequencyDistribution(token).getSampleWithMaxFreq();		
			}
			// default backoff
			else {
				predictedTag = "NN";
			}
			tags.add(predictedTag);
			
			previousTag = predictedTag;
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
