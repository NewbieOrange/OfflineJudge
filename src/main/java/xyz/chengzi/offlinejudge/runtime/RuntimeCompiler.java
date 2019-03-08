package xyz.chengzi.offlinejudge.runtime;

import org.apache.commons.io.FileUtils;
import xyz.chengzi.offlinejudge.OfflineJudge;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class RuntimeCompiler {
    private JavaCompiler compiler;
    private StandardJavaFileManager fileManager;
    private File tempFolder;

    public RuntimeCompiler() {
        compiler = ToolProvider.getSystemJavaCompiler();
        fileManager = compiler.getStandardFileManager(null, Locale.getDefault(), Charset.forName("UTF-8"));
        tempFolder = new File("tmp_" + (1000 + System.nanoTime() % 9000));
        tempFolder.mkdir();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> FileUtils.deleteQuietly(tempFolder)));
    }

    public Class<?> compile(File sourceFile) {
        try {
            JavaFileObject fileObject = fileManager.getJavaFileObjects(sourceFile).iterator().next();
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null,
                    Arrays.asList("-d", "./" + tempFolder.getName()), null, Collections.singletonList(fileObject));
            if (task.call()) {
                String fileName = sourceFile.getName();
                return new URLClassLoader(new URL[]{tempFolder.toURL()})
                        .loadClass(fileName.substring(0, fileName.lastIndexOf('.')));
            }
        } catch (Exception ignored) { // Most likely to be a compile error due to incorrect user code
        }
        return null;
    }
}
