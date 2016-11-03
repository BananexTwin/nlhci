package de.unidue.ltl.hci.millionaire.solver.solution;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;
import de.unidue.ltl.hci.millionaire.solver.WWMSolver;
import de.unidue.ltl.hci.millionaire.util.WWMQuestion;
import de.unidue.ltl.hci.millionaire.util.WikipediaAccess;


public class SimpleWikipediaSolverAnswerLookup
    implements WWMSolver
{

    private WikipediaAccess wiki;
    
    public SimpleWikipediaSolverAnswerLookup()
        throws Exception
    {
        wiki = new WikipediaAccess("http://en.wikipedia.org/w/api.php"); 
    }
    
    public int getCorrectAnswerOffset(WWMQuestion wwm, boolean verbose)
        throws Exception
    {
        int answerOffset = 0;
        double maxQuestionSum = 0.0;
        
        String question = wwm.getQuestion();
        if (verbose) {
            System.out.println(wwm);
        }

        int i=0;
        for (String answer : wwm.getAnswers()) {
            String[] tokens = answer.split(" ");
            FrequencyDistribution<String> fd = new FrequencyDistribution<String>();
            for (String token : tokens) {
                for (String wikiArticle : wiki.getWikiContent(token.toLowerCase())) {
                    for (String articleTokens : wikiArticle.split(" ")) {
                        fd.inc(articleTokens.toLowerCase());
                    }
                }
            }
            
            double questionSum = 0;
            for (String questionToken : question.split(" ")) {
//                System.out.println(questionToken.toLowerCase() + "-" + fd.getCount(questionToken.toLowerCase()));
                questionSum += (double) fd.getCount(questionToken.toLowerCase()) / question.split(" ").length;
            }
            
            if (verbose) {
                System.out.println((i==wwm.getCorrectAnswerOffset()) + "\t" + answer + "\t" + questionSum);
            }
            if (questionSum > maxQuestionSum) {
                maxQuestionSum = questionSum;
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
