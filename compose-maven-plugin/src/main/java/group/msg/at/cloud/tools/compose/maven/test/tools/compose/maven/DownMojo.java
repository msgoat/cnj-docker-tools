/*
 * at41-tools-kubectl-maven-plugin:UpMojo.java
 * (c) Copyright msg systems ag Automotive Technology 2017
 */
package group.msg.at.cloud.tools.compose.maven.test.tools.compose.maven;

import group.msg.at.cloud.tools.compose.core.command.CommandStatusCode;
import group.msg.at.cloud.tools.compose.core.command.DownCommand;
import group.msg.at.cloud.tools.compose.core.command.DownCommandResult;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * {@code Mojo} that stops and cleans up all running containers in the specified docker-compose file.
 */
@Mojo(name = "down", requiresProject = true)
public final class DownMojo extends AbstractComposeMojo {

    @Parameter(property = "compose.removeImagesOfType", readonly = true, required = false)
    protected DownCommand.RemoveImageTypes removeImagesOfType;

    @Parameter(property = "compose.removeOrphans", defaultValue = "false", readonly = true, required = false)
    protected boolean removeOrphans;

    @Parameter(property = "compose.removeVolumes", defaultValue = "false", readonly = true, required = false)
    protected boolean removeVolumes;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        DownCommand down = new DownCommand(new Slf4jMavenLogAdapter(getLog()));
        down.setComposeFile(this.composeFile);
        if (this.removeImagesOfType != null) {
            down.setRemoveImagesOfType(this.removeImagesOfType);
        }
        down.setRemoveOrphans(this.removeOrphans);
        down.setRemoveVolumes(this.removeVolumes);
        DownCommandResult result = null;
        try {
            result = down.call();
        } catch (Exception ex) {
            throw new MojoExecutionException(String.format("Failed to stop and cleanup all containers in %s", this.composeFile), ex);
        }
        if (CommandStatusCode.FAILURE.equals(result.getStatusCode())) {
            String msg = String.format("Failed to stop and cleanup all containers in [%s]: %s %s", this.composeFile, result.getStatusCode(), result.getStatusMessage());
            error(msg);
            throw new MojoExecutionException(msg);
        }
    }
}
