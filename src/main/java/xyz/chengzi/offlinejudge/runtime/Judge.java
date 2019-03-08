package xyz.chengzi.offlinejudge.runtime;

import xyz.chengzi.offlinejudge.OfflineJudge;
import xyz.chengzi.offlinejudge.data.Problem;
import xyz.chengzi.offlinejudge.data.TestCase;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Judge {
    public enum JudgeResult {
        ACCEPTED, COMPILE_ERROR, RUNTIME_ERROR, TIME_LIMIT_EXCEEDED, WRONG_ANSWER
    }

    private static final RuntimeCompiler COMPILER = new RuntimeCompiler();
    private Problem problem;
    private File sourceFile;

    public Judge(Problem problem, File sourceFile) {
        this.problem = problem;
        this.sourceFile = sourceFile;
    }

    public List<JudgeResult> judge() throws Exception {
        Class<?> sourceClass = COMPILER.compile(sourceFile);
        if (sourceClass == null) {
            return Collections.singletonList(JudgeResult.COMPILE_ERROR);
        }

        JudgeInputStream judgeInputStream = new JudgeInputStream();
        ByteArrayOutputStream outputCollectStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputCollectStream, true);
        System.setIn(judgeInputStream);
        System.setOut(printStream);
        System.setErr(printStream);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        List<JudgeResult> judgeResults = new ArrayList<>();
        Thread workerThread = new Thread(() -> {
            for (TestCase testCase : problem.getTestCases()) {
                JudgeResult caseResult = JudgeResult.ACCEPTED;

                String[] inputs = testCase.getInputs();
                if (testCase.getInputType() == TestCase.InputType.SYSTEM_IN) {
                    judgeInputStream.offer(inputs);
                    inputs = new String[0];
                }
                if (!runMainMethod(sourceClass, inputs)) {
                    caseResult = JudgeResult.RUNTIME_ERROR;
                } else if (!testCase.isAnswerCorrect(outputCollectStream.toString())) {
                    caseResult = JudgeResult.WRONG_ANSWER;
                }

                if (Thread.currentThread().isInterrupted()) {
                    break; // Time limit exceeded, skip remaining cases.
                }
                judgeResults.add(caseResult);
                judgeInputStream.reset();
                outputCollectStream.reset();
            }
            countDownLatch.countDown();
        });

        // Set the security manager after creating worker thread to avoid the check.
        long unlockCode = System.nanoTime();
        JudgeSecurityManager judgeSecurityManager = new JudgeSecurityManager(unlockCode);
        System.setSecurityManager(judgeSecurityManager);
        workerThread.start();

        if (!countDownLatch.await(problem.getTimeLimit(), TimeUnit.MILLISECONDS)) {
            workerThread.stop(); // Forcefully stop the thread, should be safe for this circumstance.
            judgeResults.add(JudgeResult.TIME_LIMIT_EXCEEDED);
        }
        judgeSecurityManager.unlock(unlockCode);
        System.setSecurityManager(null);
        System.setIn(OfflineJudge.CONSOLE_IN);
        System.setOut(OfflineJudge.CONSOLE_OUT);
        System.setErr(OfflineJudge.CONSOLE_ERR);
        return judgeResults;
    }

    private static boolean runMainMethod(Class<?> clazz, String[] args) {
        try {
            Method mainMethod = clazz.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
            return true;
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getCause();
            if (cause != null && !(cause instanceof ThreadDeath)) { // Ignore for Thread.stop() due to TLE.
                cause.printStackTrace(OfflineJudge.CONSOLE_ERR); // Print stacktrace to console.
            }
        } catch (Exception e) {
            e.printStackTrace(OfflineJudge.CONSOLE_ERR);
        }
        return false;
    }
}
