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
        for (File subFolder : folder.listFiles()) {
            if (subFolder.getName().endsWith(".yml")) {
                testCases.add(TestCase.loadFromYaml(subFolder));
            } else if (subFolder.getName().endsWith(".java")) {
                RuntimeCompiler runtimeCompiler = new RuntimeCompiler();
                Class<?> specialJudgeClass = runtimeCompiler.compile(subFolder);
                testCases.add((TestCase) specialJudgeClass.newInstance());
            }
        }
        String[] nameAndTimeLimit = folder.getName().split("-");
        return new Problem(nameAndTimeLimit[0], testCases, Long.parseLong(nameAndTimeLimit[1]));
    }
}
