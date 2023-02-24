package group.msg.at.cloud.tools.compose.core.command;

import group.msg.at.cloud.tools.compose.core.DetachedExecutableRunner;
import group.msg.at.cloud.tools.compose.core.ExecutableRunner;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents the {@code docker-compose logs} command which shows the output of running containers.
 */
public final class LogsCommand extends AbstractCommand<LogsCommandResult> {

    private boolean noColor = true;
    private boolean follow = true;
    private final List<String> loggedServices = new ArrayList<>();

    public LogsCommand() {
        super();
    }

    public LogsCommand(Logger logger) {
        super(logger);
    }

    public boolean isNoColor() {
        return noColor;
    }

    public void setNoColor(boolean noColor) {
        this.noColor = noColor;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public List<String> getLoggedServices() {
        return Collections.unmodifiableList(loggedServices);
    }

    public void setLoggedServices(List<String> loggedServices) {
        this.loggedServices.addAll(loggedServices);
    }

    private void collectCommandLineArguments(List<String> arguments) {
        if (isFollow()) {
            arguments.add("--follow");
        }
        if (isNoColor()) {
            arguments.add("--no-color");
        }
        this.loggedServices.forEach(s -> arguments.add(s));
    }

    @Override
    public LogsCommandResult internalCall() throws Exception {
        Consumer<String> loggingConsumer = s -> this.logger.info(s);
        ResultParser parsingConsumer = new ResultParser();
        Consumer<String> compositeConsumer = loggingConsumer.andThen(parsingConsumer);
        List<String> arguments = new ArrayList<>();
        arguments.add("docker");
        arguments.add("compose");
        arguments.add("logs");
        collectCommandLineArguments(arguments);
        this.logger.info("running command: " + String.join(" ", arguments));
        LogsCommandResult result = null;
        if (!isFollow()) {
            ExecutableRunner runner = new ExecutableRunner();
            runner.run(getComposeFile().getParentFile(), compositeConsumer, arguments.toArray(new String[0]));
            result = parsingConsumer.parse();
        } else {
            DetachedExecutableRunner runner = new DetachedExecutableRunner();
            runner.run(getComposeFile().getParentFile(), compositeConsumer, arguments.toArray(new String[0]));
            result = new LogsCommandResult();
            result.setStatusCode(CommandStatusCode.RUNNING);
        }
        return result;
    }

    private static final class ResultParser implements Consumer<String> {

        private CommandStatusCode statusCode;
        private final List<String> statusMessageParts = new ArrayList<>();

        public LogsCommandResult parse() {
            LogsCommandResult result = new LogsCommandResult();
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
