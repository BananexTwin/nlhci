package de.unidue.ltl.hci.millionaire.solver;

import de.unidue.ltl.hci.millionaire.util.WWMQuestion;

public interface WWMSolver
{

    public int getCorrectAnswerOffset(WWMQuestion question, boolean verbose) throws Exception;
    
}
