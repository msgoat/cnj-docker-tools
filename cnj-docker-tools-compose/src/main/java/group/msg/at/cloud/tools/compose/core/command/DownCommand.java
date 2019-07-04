package group.msg.at.cloud.tools.compose.core.command;

import group.msg.at.cloud.tools.compose.core.ExecutableRunner;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents the {@code docker-compose down} command which stops containers and removes containers,
 * networks, volumes, and images created by {@code docker-compose up}.
 */
public final class DownCommand extends AbstractCommand<DownCommandResult> {

    private RemoveImageTypes removeImagesOfType;
    private boolean removeVolumes;
    private boolean removeOrphans;
    public DownCommand() {
        super();
    }

    public DownCommand(Logger logger) {
        super(logger);
    }

    public Optional<RemoveImageTypes> getRemoveImagesOfType() {
        return Optional.ofNullable(removeImagesOfType);
    }

    public void setRemoveImagesOfType(RemoveImageTypes removeImagesOfType) {
        this.removeImagesOfType = removeImagesOfType;
    }

    public boolean isRemoveVolumes() {
        return removeVolumes;
    }

    public void setRemoveVolumes(boolean removeVolumes) {
        this.removeVolumes = removeVolumes;
    }

    public boolean isRemoveOrphans() {
        return removeOrphans;
    }

    public void setRemoveOrphans(boolean removeOrphans) {
        this.removeOrphans = removeOrphans;
    }

    protected void collectCommandLineArguments(List<String> arguments) {
        getRemoveImagesOfType().ifPresent(rmi -> { arguments.add("--rmi"); arguments.add("\"" + rmi + "\""); });
        if (isRemoveVolumes()) {
            arguments.add("--volumes");
        }
        if (isRemoveOrphans()) {
            arguments.add("--remove-orphans");
        }
    }

    @Override
    public DownCommandResult internalCall() throws Exception {
        ExecutableRunner runner = new ExecutableRunner();
        Consumer<String> loggingConsumer = s -> this.logger.info(s);
        ResultParser parsingConsumer = new ResultParser();
        Consumer<String> compositeConsumer = loggingConsumer.andThen(parsingConsumer);
        List<String> arguments = new ArrayList<>();
        arguments.add("docker-compose");
        arguments.add("down");
        collectCommandLineArguments(arguments);
        this.logger.info("running command: " + String.join(" ", arguments));
        runner.run(getComposeFile().getParentFile(), compositeConsumer, arguments.toArray(new String[0]));
        return parsingConsumer.parse();
    }

    public enum RemoveImageTypes {
        all,
        local
    }

    private static final class ResultParser implements Consumer<String> {

        private CommandStatusCode statusCode;
        private List<String> statusMessageParts = new ArrayList<>();

        public DownCommandResult parse() {
            DownCommandResult result = new DownCommandResult();
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
