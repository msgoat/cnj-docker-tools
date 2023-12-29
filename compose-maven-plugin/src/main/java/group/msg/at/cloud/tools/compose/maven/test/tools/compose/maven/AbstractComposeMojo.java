/*
 * compose-maven-plugin:AbstractComposeMojo.java
 * (c) Copyright msg systems ag Automotive Technology & Manufacturing 2019 - 2023
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
 * @version 2.0
 */
public abstract class AbstractComposeMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}", readonly = false, required = false)
    protected File target;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject project;

    @Parameter(defaultValue = "${session}", required = true, readonly = true)
    protected MavenSession session;

    @Parameter(property = "compose.file", required = true, readonly = false)
    protected File composeFile;

    @Parameter(property = "compose.debug", defaultValue = "false", readonly = false, required = false)
    protected boolean debug;

    @Parameter(property = "compose.skip", defaultValue = "false", readonly = false, required = false)
    protected boolean skip;

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
