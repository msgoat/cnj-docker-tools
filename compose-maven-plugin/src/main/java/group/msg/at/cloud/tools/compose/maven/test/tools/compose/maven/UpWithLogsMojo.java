/*
 * compose-maven-plugin:UpMojo.java
 * (c) Copyright msg systems ag Automotive Technology 2017
 */
package group.msg.at.cloud.tools.compose.maven.test.tools.compose.maven;

import group.msg.at.cloud.tools.compose.core.command.CommandStatusCode;
import group.msg.at.cloud.tools.compose.core.command.UpWithLogsCommand;
import group.msg.at.cloud.tools.compose.core.command.UpWithLogsCommandResult;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * {@code Mojo} that runs all containers in the specified docker-compose file and logs their output using the current
 * Maven logger.
 */
@Mojo(name = "upWithLogs", requiresProject = true)
public final class UpWithLogsMojo extends AbstractComposeMojo {

    @Parameter(property = "compose.noColor", defaultValue = "true", readonly = false, required = false)
    private boolean noColor;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        UpWithLogsCommand upWithLogs = new UpWithLogsCommand(new Slf4jMavenLogAdapter(getLog()));
        upWithLogs.setNoColor(this.noColor);
        upWithLogs.setComposeFile(this.composeFile);
        UpWithLogsCommandResult result = null;
        try {
            result = upWithLogs.call();
        } catch (Exception ex) {
            throw new MojoExecutionException(String.format("Failed to run all containers in %s", this.composeFile), ex);
        }
        if (CommandStatusCode.FAILURE.equals(result.getStatusCode())) {
            String msg = String.format("Failed to run all containers in [%s]: %s %s", this.composeFile, result.getStatusCode(), result.getStatusMessage());
            error(msg);
            throw new MojoExecutionException(msg);
        }
    }
}
