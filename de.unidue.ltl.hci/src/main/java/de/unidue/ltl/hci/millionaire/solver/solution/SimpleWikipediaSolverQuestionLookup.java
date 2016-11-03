package de.unidue.ltl.hci.millionaire.solver.solution;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;
import de.tudarmstadt.ukp.dkpro.core.ngrams.util.NGramStringIterable;
import de.unidue.ltl.hci.millionaire.solver.WWMSolver;
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
        if (verbose) {
            System.out.println(wwm);
        }
        
        FrequencyDistribution<String> fd = new FrequencyDistribution<String>();
        for (String token : new NGramStringIterable(question.split(" "), 1, 2)) {
            for (String wikiArticle : wiki.getWikiContent(token)) {
                for (String articleTokens : wikiArticle.split(" ")) {
                    fd.inc(articleTokens.toLowerCase());
                }
            }
        }
            
        int answerOffset = 0;
        double maxAnswerSum = 0.0;

        int i=0;
        double answerSum = 0;
        for (String answer : wwm.getAnswers()) {
            for (String answerToken : answer.split(" ")) {
                answerSum += (double) fd.getCount(answerToken.toLowerCase()) / answer.split(" ").length;
            }
            
            if (verbose) {
                System.out.println((i==wwm.getCorrectAnswerOffset()) + "\t" + answer + "\t" + answerSum);
            }
            if (answerSum > maxAnswerSum) {
                maxAnswerSum = answerSum;
                answerOffset = i;
            }
            
            i++;
        }
        
        if (verbose) {
            System.out.println("Selected: " + (answerOffset+1) + "\t" + (answerOffset==wwm.getCorrectAnswerOffset()));
            System.out.println();        	
        }
        
        return answerOffset;
    }
}
