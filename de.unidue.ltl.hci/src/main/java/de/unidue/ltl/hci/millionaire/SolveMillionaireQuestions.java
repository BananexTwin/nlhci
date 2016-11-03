package de.unidue.ltl.hci.millionaire;

import java.io.File;
import java.util.List;

import de.unidue.ltl.hci.millionaire.solver.RandomSolver;
import de.unidue.ltl.hci.millionaire.solver.WWMSolver;
import de.unidue.ltl.hci.millionaire.solver.solution.SimpleWikipediaSolverAnswerLookup;
import de.unidue.ltl.hci.millionaire.solver.solution.SimpleWikipediaSolverQuestionLookup;
import de.unidue.ltl.hci.millionaire.util.QuestionReader;
import de.unidue.ltl.hci.millionaire.util.WWMQuestion;

public class SolveMillionaireQuestions
{
    public static void main(String[] args)
        throws Exception
    {
        String questionFile = "src/main/resources/millionaire/en/wwm_easy.txt";
//        String questionFile = "src/main/resources/millionaire/en/wwmQuestionsRound1_withAnswers_small.txt";
//        String questionFile = "src/main/resources/millionaire/en/wwmQuestionsRound1_withAnswers.txt";
        
        WWMSolver random = new RandomSolver();
        WWMSolver questionLookup = new SimpleWikipediaSolverQuestionLookup();
        WWMSolver answerLookup = new SimpleWikipediaSolverAnswerLookup();

        System.out.println("###  Random  ###");
        solve(questionFile, random, false);
        System.out.println();
        
        System.out.println("###  Question Lookup  ###");
        solve(questionFile, questionLookup, false);
        System.out.println();
        
        System.out.println("###  Answer Lookup  ###");
        solve(questionFile, answerLookup, false);
        System.out.println();
    }
    
    private static void solve(String questionFile, WWMSolver solver, boolean verbose) 
    	throws Exception
    {
        int n = 0;
        int correct = 0;
        
        List<WWMQuestion> questions = QuestionReader.getWWMQuestions(new File(questionFile));
        for (WWMQuestion question : questions) {
            int solverOffset = solver.getCorrectAnswerOffset(question, verbose);
        
            if (question.getCorrectAnswerOffset() == solverOffset) {
                correct++;
            }
            
            n++;
        }
        
        System.out.println("Correctly solved: " + (double) correct / n);
    }
}