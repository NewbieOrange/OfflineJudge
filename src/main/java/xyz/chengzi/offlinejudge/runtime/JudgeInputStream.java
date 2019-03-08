package xyz.chengzi.offlinejudge.runtime;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class JudgeInputStream extends InputStream {
    private ByteArrayInputStream inputStream;

    public void offer(String... lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line).append('\n');
        }
        inputStream = new ByteArrayInputStream(builder.toString().getBytes());
    }

    public void reset() {
        inputStream = null;
    }

    @Override
    public int read() {
        if (inputStream == null) {
            return -1;
        }
        return inputStream.read();
    }
}
