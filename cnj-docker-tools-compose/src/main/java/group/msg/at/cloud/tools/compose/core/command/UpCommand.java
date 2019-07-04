package group.msg.at.cloud.tools.compose.core.command;

import group.msg.at.cloud.tools.compose.core.ExecutableRunner;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents the {@code docker-compose up} command which builds, (re)creates, starts,
 * and attaches to containers for a service.
 */
public final class UpCommand extends AbstractCommand<UpCommandResult> {

    private boolean detached = true;
    private boolean noColor = true;

    public UpCommand() {
        super();
    }

    public UpCommand(Logger logger) {
        super(logger);
    }

    public boolean isDetached() {
        return detached;
    }

    public void setDetached(boolean detached) {
        this.detached = detached;
    }

    public boolean isNoColor() {
        return noColor;
    }

    public void setNoColor(boolean noColor) {
        this.noColor = noColor;
    }

    protected void collectCommandLineArguments(List<String> arguments) {
        if (isDetached()) {
            arguments.add("--detach");
        } else {
            this.logger.warn("docker-compose up will be run in blocking mode, which will block your Maven build! Are you sure you want to do this? If not, consider using the detached flag!");
        }
        if (isNoColor()) {
            arguments.add("--no-color");
        }
    }

    @Override
    public UpCommandResult internalCall() throws Exception {
        ExecutableRunner runner = new ExecutableRunner();
        Consumer<String> loggingConsumer = s -> this.logger.info(s);
        ResultParser parsingConsumer = new ResultParser();
        Consumer<String> compositeConsumer = loggingConsumer.andThen(parsingConsumer);
        List<String> arguments = new ArrayList<>();
        arguments.add("docker-compose");
        arguments.add("up");
        collectCommandLineArguments(arguments);
        this.logger.info("running command: " + String.join(" ", arguments));
        runner.run(getComposeFile().getParentFile(), compositeConsumer, arguments.toArray(new String[0]));
        return parsingConsumer.parse();
    }

    private static final class ResultParser implements Consumer<String> {

        private CommandStatusCode statusCode;
        private List<String> statusMessageParts = new ArrayList<>();

        public UpCommandResult parse() {
            UpCommandResult result = new UpCommandResult();
            result.setStatusCode(statusCode);
            result.setStatusMessage(String.join(" ", statusMessageParts));
            return result;
        }

        @Override
        public void accept(String s) {
            statusCode = CommandStatusCode.SUCCESS;
            // TODO: add console line parsing
        }
    }
}
