package group.msg.at.cloud.tools.compose.core.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Abstract base class for all helm command classes that encapsulates some common behaviour.
 *
 * @param <V> concrete helm command subclass
 */
public abstract class AbstractCommand<V> implements Callable<V> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private File currentDirectory;
    private File composeFile;

    public AbstractCommand() {
    }

    public AbstractCommand(Logger logger) {
        this.logger = logger;
    }

    public final File getCurrentDirectory() {
        if (this.currentDirectory == null) {
            try {
                this.currentDirectory = new File(".").getCanonicalFile();
            } catch (IOException ex) {
                throw new UncheckedIOException("Failed to retrieve current directory!", ex);
            }
        }
        return this.currentDirectory;
    }

    public final void setCurrentDirectory(File currentDirectory) {
        Objects.requireNonNull(currentDirectory, "currentDirectory must not be null");
        this.currentDirectory = currentDirectory;
    }

    public final File getComposeFile() {
        return this.composeFile;
    }

    public final void setComposeFile(File composeFile) {
        Objects.requireNonNull(composeFile, "composeFile must not be null");
        this.composeFile = composeFile;
    }

    @Override
    public V call() throws Exception {
        return internalCall();
    }

    protected abstract V internalCall() throws Exception;
}
