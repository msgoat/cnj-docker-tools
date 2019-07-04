package group.msg.at.cloud.tools.compose.core.command;

import org.slf4j.Logger;

import java.util.List;

/**
 * Represents the docker-compose command sequence {@code docker-compose up -d} before {@code docker-compose logs -d}
 * which builds, (re)creates, starts, and attaches to containers for a service and show the output of the running containers.
 */
public final class UpWithLogsCommand extends AbstractCommand<UpWithLogsCommandResult> {

    private final UpCommand up;
    private final LogsCommand logs;

    public UpWithLogsCommand() {
        super();
        this.up = new UpCommand();
        this.logs = new LogsCommand();
    }

    public UpWithLogsCommand(Logger logger) {
        super(logger);
        this.up = new UpCommand(logger);
        this.logs = new LogsCommand(logger);
    }

    public boolean isNoColor() {
        return this.up.isNoColor();
    }

    public void setNoColor(boolean noColor) {
        this.up.setNoColor(noColor);
        this.logs.setNoColor(noColor);
    }

    public List<String> getLoggedServices() {
        return this.logs.getLoggedServices();
    }

    public void setLoggedServices(List<String> loggedServices) {
        this.logs.setLoggedServices(loggedServices);
    }

    @Override
    public UpWithLogsCommandResult internalCall() throws Exception {
        UpWithLogsCommandResult result = new UpWithLogsCommandResult();
        this.up.setCurrentDirectory(getCurrentDirectory());
        this.up.setComposeFile(getComposeFile());
        this.up.setDetached(true);
        UpCommandResult upResult = this.up.call();
        if (CommandStatusCode.SUCCESS.equals(upResult.getStatusCode())) {
            this.logs.setCurrentDirectory(getCurrentDirectory());
            this.logs.setComposeFile(getComposeFile());
            this.logs.setFollow(true);
            LogsCommandResult logsResult = this.logs.call();
            result.setStatusCode(logsResult.getStatusCode());
            result.setStatusMessage(logsResult.getStatusMessage());
        } else {
            result.setStatusCode(upResult.getStatusCode());
            result.setStatusMessage(upResult.getStatusMessage());
        }
        return result;
    }
}
