package de.unidue.ltl.hci.millionaire.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WWMQuestion
{

    private String question;
    private List<String> answers;
    private int correctAnswerOffset;
    private int min;
    private int max;
    private String topic;
    
    public WWMQuestion()
    {
        this.answers = new ArrayList<String>();
        // empty constructor - do everything via setters
    }
    
    public WWMQuestion(String question, int correctAnswerOffset, String ... answers)
    {
        super();
        this.question = question;
        this.answers = Arrays.asList(answers);
        this.correctAnswerOffset = correctAnswerOffset;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getQuestion());
        sb.append("\t");
        for (String answer : getAnswers()) {
            sb.append(answer);
            sb.append("\t");
        }
        sb.append(getCorrectAnswerOffset());
        
        return sb.toString();
    }

    public String getQuestion()
    {
        return question;
    }
    public void setQuestion(String question)
    {
        this.question = question;
    }
    public List<String> getAnswers()
    {
        return answers;
    }
    public void addAnswer(String answer)
    {
        this.answers.add(answer);
    }
    public void setAnswers(List<String> answers)
    {
        this.answers = answers;
    }
    public int getCorrectAnswerOffset()
    {
        return correctAnswerOffset;
    }
    public void setCorrectAnswerOffset(int correctAnswerOffset)
    {
        this.correctAnswerOffset = correctAnswerOffset;
    }
    public int getMin()
    {
        return min;
    }
    public void setMin(int min)
    {
        this.min = min;
    }
    public int getMax()
    {
        return max;
    }
    public void setMax(int max)
    {
        this.max = max;
    }
    public String getTopic()
    {
        return topic;
    }
    public void setTopic(String topic)
    {
        this.topic = topic;
    }
}