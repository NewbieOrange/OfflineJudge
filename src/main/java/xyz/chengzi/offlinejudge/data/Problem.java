package xyz.chengzi.offlinejudge.data;

import xyz.chengzi.offlinejudge.runtime.RuntimeCompiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Problem {
    private String name;
    private List<TestCase> testCases;
    private long timeLimit;

    public Problem(String name, List<TestCase> testCases, long timeLimit) {
        this.name = name;
        this.testCases = testCases;
        this.timeLimit = timeLimit;
    }

    public String getName() {
        return name;
    }

    public List<TestCase> getTestCases() {
        return Collections.unmodifiableList(testCases);
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public static Problem loadFromFolder(File folder) throws Exception {
        List<TestCase> testCases = new ArrayList<>();
        for (File testFile : folder.listFiles()) {
            if (testFile.getName().endsWith(".yml")) {
                testCases.add(TestCase.loadFromYaml(testFile));
            } else if (testFile.getName().endsWith(".java")) {
                RuntimeCompiler runtimeCompiler = new RuntimeCompiler();
                Class<?> specialJudgeClass = runtimeCompiler.compile(testFile);
                testCases.add((TestCase) specialJudgeClass.newInstance());
            }
        }
        String[] nameAndTimeLimit = folder.getName().split("-");
        return new Problem(nameAndTimeLimit[0], testCases, Long.parseLong(nameAndTimeLimit[1]));
    }
}
