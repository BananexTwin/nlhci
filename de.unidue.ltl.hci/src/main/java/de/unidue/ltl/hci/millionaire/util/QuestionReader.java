package de.unidue.ltl.hci.millionaire.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class QuestionReader
{

    public static List<WWMQuestion> getWWMQuestions(File inputFile)
        throws IOException
    {
        List<WWMQuestion> questions = new ArrayList<WWMQuestion>();

        for (String line : FileUtils.readLines(inputFile)) {
            String[] elements = line.split("\t");

            // check if the question is complete
            if (elements.length != 6) {
                throw new IOException("Wrong line: " + line);
            }

            List<String> answers = new ArrayList<String>();
            answers.add(elements[1]);
            answers.add(elements[2]);
            answers.add(elements[3]);
            answers.add(elements[4]);

            WWMQuestion question = new WWMQuestion(
                    elements[0],
                    Integer.parseInt(elements[5]),
                    answers.toArray(new String[0]));
            questions.add(question);
        }

        return questions;
    }

    public static List<WWMQuestion> getEnglishWWMQuestions(File inputFile)
        throws IOException
    {
        List<WWMQuestion> questions = new ArrayList<WWMQuestion>();

        for (String line : FileUtils.readLines(inputFile)) {
            String[] elements = line.split("\t");

            // check if the question is complete
            if (elements.length != 5) {
                continue;
//                throw new IOException("Wrong line: " + line);
            }

            List<String> answers = new ArrayList<String>();
            answers.add(elements[1]);
            answers.add(elements[2]);
            answers.add(elements[3]);
            answers.add(elements[4]);

            WWMQuestion question = new WWMQuestion(elements[0], getCorrectAnswerOffset(answers),
                    answers.toArray(new String[0]));
            questions.add(question);
        }

        return questions;
    }

    private static int getCorrectAnswerOffset(List<String> answers)
    {
        int i = 0;
        for (String answer : answers) {
            if (answer.toUpperCase().equals(answer) || answer.contains("(CORRECT)")) {
                return i;
            }
            i++;
        }

        return -1;
    }
    
    public static List<WWMQuestion> getGermanWWMQuestions(File inputFile)
        throws IOException
    {
        String topic = "";
        
        List<WWMQuestion> questions = new ArrayList<WWMQuestion>();

        WWMQuestion question = null;
        for (String l : FileUtils.readLines(inputFile)) {
            if (l.startsWith("FZ")) {
                String currentQuestionPart = question.getQuestion();
                if (currentQuestionPart == null) {
                    currentQuestionPart = "";
                }
                question.setQuestion(currentQuestionPart + parseFZ(l));
            }
            else if (l.startsWith("Min")) {
                question.setMin(parseMinMax(l));
            }
            else if (l.startsWith("Max")) {
                question.setMax(parseMinMax(l));
            }
            else if (l.startsWith("Antwort")) {
                String[] answer = parseAntwort(l);
                question.addAnswer(answer[0]);
                
                if (answer[1].equals(Boolean.TRUE.toString())) {
                    question.setCorrectAnswerOffset(question.getAnswers().size()-1);
                }
            }
            else if (l.startsWith("Thema")) {
                topic = parseThema(l);
            }
            else if (l.trim().length() == 0) {
                question = new WWMQuestion();
                question.setTopic(topic);
                questions.add(question);
            }
            else {
                // ignore everything else
            }
        }

        return questions;
    }

    private static String parseFZ(String line)
            throws IOException
    {
        String[] keyValue = line.split("=");
        if (keyValue.length < 2) {
            return "";
        }

        String value = keyValue[1].trim();
        if (value.length() == 0) {
            return ""; // this is a line like "FZ1=   "
        }
        
        return value;
    }

    private static String[] parseAntwort(String line)
            throws IOException
    {
        String[] keyValue = line.split("=");
        if (keyValue.length < 2) {
            throw new IOException("Illformed line: " + line);
        }

        String value = keyValue[1].trim();

        Boolean correctAnswer = value.startsWith("1");
        String answer = value.substring(1, value.length());
        
        return new String[] {answer, correctAnswer.toString()};

    }

    private static Integer parseMinMax(String line)
            throws IOException
    {
        String[] keyValue = line.split("=");
        if (keyValue.length < 2) {
            throw new IOException("Illformed line: " + line);
        }

        String value = keyValue[1].trim();

        Integer max = 0;
        try {
            max = Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            return null;
        }
        
        return max;
    }

    private static String parseThema(String line)
            throws IOException
    {
        String[] keyValue = line.split("=");
        if (keyValue.length < 2) {
            throw new IOException("Illformed line: " + line);
        }

        String value = keyValue[1].trim();
        if (value.length() == 0) {
            return ""; // this is a line like "FZ1=   "
        }
        
        return value;
    }
}