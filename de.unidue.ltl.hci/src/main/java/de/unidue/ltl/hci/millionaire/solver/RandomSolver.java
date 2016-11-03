package de.unidue.ltl.hci.millionaire.solver;

import java.util.Random;

import de.unidue.ltl.hci.millionaire.util.WWMQuestion;

public class RandomSolver
    implements WWMSolver
{

    private Random random;
    
    public RandomSolver()
    {
        random = new Random(); 
    }
    
    public int getCorrectAnswerOffset(WWMQuestion question, boolean verbose)
        throws Exception
    {
        return random.nextInt(4);
    }
}
