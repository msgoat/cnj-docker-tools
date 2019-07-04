package group.msg.at.cloud.tools.compose.core;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class DetachedExecutableRunner {

    public static final int INTERRUPTED_EXIT_CODE = -666;

    public Process run(File currentDirectory, Consumer<String> outputProcessor, String... command) {
        Process process = null;
        try {
            process = new ProcessBuilder().command(command).directory(currentDirectory).redirectErrorStream(true).start();
        } catch (IOException ex) {
            throw new UncheckedIOException(String.format("I/O Error while executing command [%s] in directory [%s]", command[0], currentDirectory.getAbsolutePath()), ex);
        }
        Executor backgroundProcessor = Executors.newSingleThreadExecutor();
        backgroundProcessor.execute(new StreamGobbler(process.getInputStream(), outputProcessor));
        return process;
    }
}
