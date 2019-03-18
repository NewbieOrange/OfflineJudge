package xyz.chengzi.offlinejudge;

import xyz.chengzi.offlinejudge.data.Problem;
import xyz.chengzi.offlinejudge.data.ProblemSet;
import xyz.chengzi.offlinejudge.runtime.Judge;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class OfflineJudge {
    public static final InputStream CONSOLE_IN = System.in;
    public static final PrintStream CONSOLE_OUT = System.out;
    public static final PrintStream CONSOLE_ERR = System.err;
    public static Scanner SCANNER;

    public static void main(String[] args) throws Exception {
        ProblemSet problemSet = ProblemSet.loadFromFolder(new File(args[0]));
        for (File subFolder : new File("student/").listFiles()) {
            String studentName = subFolder.getName();
            judge(problemSet, studentName);
        }
    }

    public static void judge(ProblemSet problemSet, String studentName) throws Exception {
        System.out.println("Judged by OfflineJudge: " + new Date() + ", " + studentName);
        System.out.println("--> https://github.com/NewbieOrange/OfflineJudge");
        int score = 0;
        for (Problem problem : problemSet) {
            File srcFile = new File("student/" + studentName + "/Submission attachment(s)/" + problem.getName() + ".java");
            overrideScanner(srcFile);
            Judge judge = new Judge(problem, srcFile);
            List<Judge.JudgeResult> judgeResults = judge.judge();
            if (problem.getName().equals("A1Q5") && judgeResults.get(0) == Judge.JudgeResult.ACCEPTED) {
                score += 20;
            } else {
                for (Judge.JudgeResult judgeResult : judgeResults) {
                    if (judgeResult == Judge.JudgeResult.ACCEPTED) {
                        score += 4;
                    }
                }
            }
            System.out.println(problem.getName() + ": " + judgeResults);
        }
        System.out.println("Total Assignment Score: " + score + " / " + 100);
    }

    public static void overrideScanner(File file) throws Exception {
        Files.writeString(file.toPath(), Files.readString(file.toPath())
                .replace("new Scanner(System.in)", "xyz.chengzi.offlinejudge.OfflineJudge.SCANNER"));
    }
}
