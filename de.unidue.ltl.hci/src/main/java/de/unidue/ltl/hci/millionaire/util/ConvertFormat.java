package de.unidue.ltl.hci.millionaire.util;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class ConvertFormat
{

    public static void main(String[] args)
        throws Exception
    {
        convertEnglish("src/main/resources/millionaire/en/wwmQuestionsRound1_withAnswers.txt");
        convertEnglish("src/main/resources/millionaire/en/wwmQuestionsRound2_withAnswers.txt");
        convertGerman("src/main/resources/millionaire/de/allg02.txt");
    }
    
    private static void convertEnglish(String file)
        throws Exception
    {
        StringBuilder sb = new StringBuilder();
        for (WWMQuestion question : QuestionReader.getEnglishWWMQuestions(new File(file))) {
            sb.append(question.toString() + "\n");
        }
        String parts[] = file.split("/");
        FileUtils.writeStringToFile(new File("target/" + parts[parts.length-1]), sb.toString());
    }
    
    private static void convertGerman(String file)
        throws Exception
    {
        StringBuilder sb = new StringBuilder();
        for (WWMQuestion question : QuestionReader.getGermanWWMQuestions(new File(file))) {
            sb.append(question.toString() + "\n");
        }
        String parts[] = file.split("/");
        FileUtils.writeStringToFile(new File("target/" + parts[parts.length-1]), sb.toString());
        
    }
}
