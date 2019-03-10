package xyz.chengzi.offlinejudge;

import xyz.chengzi.offlinejudge.data.Problem;
import xyz.chengzi.offlinejudge.data.ProblemSet;
import xyz.chengzi.offlinejudge.runtime.Judge;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

public class OfflineJudge {
    public static final InputStream CONSOLE_IN = System.in;
    public static final PrintStream CONSOLE_OUT = System.out;
    public static final PrintStream CONSOLE_ERR = System.err;

    public static void main(String[] args) throws Exception {
        ProblemSet problemSet = ProblemSet.loadFromFolder(new File(args[0]));
        int score = 0, fullScore = 0;

        System.out.println("Judged by OfflineJudge: " + new Date());
        System.out.println("--> https://github.com/NewbieOrange/OfflineJudge");
        for (Problem problem : problemSet) {
            Judge judge = new Judge(problem, new File("student/" + problem.getName() + ".java"));
            List<Judge.JudgeResult> judgeResults = judge.judge();
            for (Judge.JudgeResult judgeResult : judgeResults) {
                if (judgeResult == Judge.JudgeResult.ACCEPTED) {
                    score += 5;
                }
            }
            fullScore += 5 * problem.getTestCases().size();
            System.out.println(problem.getName() + ": " + judgeResults);
        }
        System.out.println("Total Assignment Score: " + score + " / " + fullScore);
    }
}
