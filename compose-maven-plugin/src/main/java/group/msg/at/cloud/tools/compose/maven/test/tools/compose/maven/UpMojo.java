/*
 * compose-maven-plugin:UpMojo.java
 * (c) Copyright msg systems ag Automotive Technology 2017
 */
package group.msg.at.cloud.tools.compose.maven.test.tools.compose.maven;

import group.msg.at.cloud.tools.compose.core.command.CommandStatusCode;
import group.msg.at.cloud.tools.compose.core.command.UpCommand;
import group.msg.at.cloud.tools.compose.core.command.UpCommandResult;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * {@code Mojo} that runs all containers in the specified docker-compose file.
 */
@Mojo(name = "up", requiresProject = true)
public final class UpMojo extends AbstractComposeMojo {

    @Parameter(property = "compose.noColor", defaultValue = "true", readonly = false, required = false)
    private boolean noColor;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        UpCommand up = new UpCommand(new Slf4jMavenLogAdapter(getLog()));
        up.setNoColor(this.noColor);
        up.setComposeFile(this.composeFile);
        UpCommandResult result = null;
        try {
            result = up.call();
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
