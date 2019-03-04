package xyz.chengzi.offlinejudge.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ProblemSet implements Iterable<Problem> {
    private List<Problem> problems;

    public ProblemSet(List<Problem> problems) {
        this.problems = problems;
    }

    public List<Problem> getProblems() {
        return Collections.unmodifiableList(problems);
    }

    @Override
    public Iterator<Problem> iterator() {
        return getProblems().iterator();
    }

    public static ProblemSet loadFromFolder(File folder) throws Exception {
        List<Problem> problems = new ArrayList<>();
        for (File subFolder : folder.listFiles()) {
            if (subFolder.isDirectory()) {
                problems.add(Problem.loadFromFolder(subFolder));
            }
        }
        return new ProblemSet(problems);
    }
}
