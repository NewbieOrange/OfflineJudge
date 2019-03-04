package xyz.chengzi.offlinejudge.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TestCase {
    public enum InputType {
        SYSTEM_IN, ARGS
    }

    private String[] inputLines, answerLines;

    public TestCase(String[] inputLines, String[] answerLines) {
        this.inputLines = inputLines;
        this.answerLines = answerLines;
    }

    public String[] getInputLines() {
        return inputLines;
    }

    public String[] getAnswerLines() {
        return answerLines;
    }

    public InputType getInputType() {
        return InputType.ARGS;
    }

    public boolean isAnswerCorrect(String answer) {
        String[] userLines = answer.split("\n");
        if (userLines.length < answerLines.length || userLines.length > answerLines.length * 2) {
            return false;
        }
        for (int i = 0; i < answerLines.length; i++) {
            if (!answerLines[i].equals(userLines[i].trim())) {
                return false;
            }
        }
        for (int i = answerLines.length; i < userLines.length; i++) {
            if (!userLines[i].trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static TestCase loadFromFolder(File folder) throws Exception {
        File in = new File(folder, "in.txt"), out = new File(folder, "out.txt");
        BufferedReader inReader = new BufferedReader(new FileReader(in));
        BufferedReader outReader = new BufferedReader(new FileReader(out));
        List<String> inputLines = new ArrayList<>(), answerLines = new ArrayList<>();
        String line;
        while ((line = inReader.readLine()) != null) {
            if (!line.isEmpty()) {
                inputLines.add(line);
            }
        }
        while ((line = outReader.readLine()) != null) {
            if (!line.isEmpty()) {
                answerLines.add(line);
            }
        }
        return new TestCase(inputLines.toArray(new String[0]), answerLines.toArray(new String[0]));
    }
}
