package xyz.chengzi.offlinejudge.data;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;

public class TestCase {
    public enum InputType {
        SYSTEM_IN, ARGS
    }

    private String[] inputs, expectedAnswers;
    private InputType inputType;

    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    public String[] getInputs() {
        return inputs;
    }

    public void setInputs(String[] inputs) {
        this.inputs = inputs;
    }

    public String[] getExpectedAnswers() {
        return expectedAnswers;
    }

    public void setExpectedAnswers(String[] expectedAnswers) {
        this.expectedAnswers = expectedAnswers;
    }

    public boolean isAnswerCorrect(String answer) {
        String[] userLines = answer.split("\n");
        if (userLines.length < expectedAnswers.length || userLines.length > expectedAnswers.length * 2) {
            return false;
        }
        for (int i = 0; i < expectedAnswers.length; i++) {
            if (!expectedAnswers[i].equals(userLines[i].trim())) {
                return false;
            }
        }
        for (int i = expectedAnswers.length; i < userLines.length; i++) {
            if (!userLines[i].trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static TestCase loadFromYaml(File yamlFile) throws Exception {
        Yaml yaml = new Yaml(new Constructor(TestCase.class));
        try (FileInputStream fileInputStream = new FileInputStream(yamlFile)) {
            return yaml.load(fileInputStream);
        }
    }
}
