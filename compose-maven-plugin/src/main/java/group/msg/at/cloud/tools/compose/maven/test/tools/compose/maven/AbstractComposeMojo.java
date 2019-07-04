/*
 * at41-tools-kubectl-maven-plugin:AbstractComposeMojo.java
 * (c) Copyright msg systems ag Automotive Technology 2019
 */
package group.msg.at.cloud.tools.compose.maven.test.tools.compose.maven;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * Common base implementation of all Mojos running Docker Compose.
 *
 * @author theism
 * @version 1.0
 */
public abstract class AbstractComposeMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = false)
    protected File target;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject project;

    @Parameter(defaultValue = "${session}", required = true, readonly = true)
    protected MavenSession session;

    @Parameter(property = "compose.file", required = true, readonly = true)
    protected File composeFile;

    @Parameter(property = "compose.debug", defaultValue = "false", readonly = true, required = false)
    protected boolean debug;

    protected void info(String msg) {
        getLog().info(msg);
    }

    protected void warn(String msg) {
        getLog().warn(msg);
    }

    protected void error(String msg) {
        getLog().error(msg);
    }

    protected void error(String msg, Throwable e) {
        getLog().error(msg, e);
    }
}
