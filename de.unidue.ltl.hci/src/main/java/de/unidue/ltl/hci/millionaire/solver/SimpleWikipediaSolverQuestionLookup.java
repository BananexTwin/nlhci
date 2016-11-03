package de.unidue.ltl.hci.millionaire.solver;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;
import de.unidue.ltl.hci.millionaire.util.WWMQuestion;
import de.unidue.ltl.hci.millionaire.util.WikipediaAccess;


public class SimpleWikipediaSolverQuestionLookup
    implements WWMSolver
{

    private WikipediaAccess wiki;
    
    public SimpleWikipediaSolverQuestionLookup()
        throws Exception
    {
        wiki = new WikipediaAccess("http://en.wikipedia.org/w/api.php"); 
    }
    
    public int getCorrectAnswerOffset(WWMQuestion wwm, boolean verbose)
        throws Exception
    {	
        String question = wwm.getQuestion();
        System.out.println(wwm);

        // example for accessing Wikipedia contents
        System.out.println(wiki.getWikiContent("Germany").get(0).substring(0, 20));
        
        // TODO create frequency distribution from articles representing tokens in question
        FrequencyDistribution<String> fd = new FrequencyDistribution<String>();
        
        // TODO score which answer matches best with question
        int answerOffset = 0;
        double maxAnswerSum = 0.0;
        
        if (verbose) {
            System.out.println("Selected: " + (answerOffset+1) + "\t" + (answerOffset==wwm.getCorrectAnswerOffset()));
            System.out.println();        	
        }
        
        return answerOffset;
    }
}
