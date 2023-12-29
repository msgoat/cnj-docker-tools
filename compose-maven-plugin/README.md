# compose-maven-plugin

Maven-Plugin for running `docker compose` commands in Maven Builds.

## Plugin Documentation

### System Requirements

| Requirement | Constraint                                    |
|-------------|-----------------------------------------------|
| JDK         | 17                                            |
| Maven       | 3.8+                                          |
| docker      | Must be installed and must be visible in PATH |

### Usage

Add the following snippet to your build/plugin/pluginManagement section:

```
<plugin>
    <groupId>group.msg.at.cloud.tools</groupId>
    <artifactId>compose-maven-plugin</artifactId>
    <version>${maven.compose.plugin.version}</version>
</plugin>
```

Add the following snippet to your build/plugin section:

```
<plugin>
    <groupId>group.msg.at.cloud.tools</groupId>
    <artifactId>compose-maven-plugin</artifactId>
    <executions>
        <execution>
            <id>up-with-logs</id>
            <phase>pre-integration-test</phase>
            <goals>
                <goal>upWithLogs</goal>
            </goals>
            <configuration>
                <composeFile>src/test/docker/${project.artifactId}/docker-compose.yml</composeFile>
            </configuration>
        </execution>
        <execution>
            <id>down</id>
            <phase>post-integration-test</phase>
            <goals>
                <goal>down</goal>
            </goals>
            <configuration>
                <composeFile>src/test/docker/${project.artifactId}/docker-compose.yml</composeFile>
                <removeImagesOfType>all</removeImagesOfType>
                <removeVolumes>true</removeVolumes>
                <removeOrphans>true</removeOrphans>
            </configuration>
        </execution>
    </executions>
</plugin>
```

This snippet demonstrates how to run containers in the __pre-integration-test__ phase with log output and stop them in
the __post_integration_test__ phase after running system tests against them in the __integration-test__ phase using the
maven-failsafe-plugin.

#### Goals

| Goal       | Description                                                                                                                                                                                                                                                        |
|------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| up         | Runs __docker compose up__ with a specified docker-compose file                                                                                                                                                                                                    |
| down       | Runs __docker compose down__ with a specified docker-compose file                                                                                                                                                                                                  |
| upWithLogs | Runs __docker compose up --detach__ in detached mode and automatically attaches to all running containers to follow their log output using __docker-compose logs --follow__. *This is the preferred goal for running containers for system tests in Maven builds.* | 

#### Parameters

| Parameter          | Property                   | Type    | Since | Goal(s)        | Description                                                                                                                                                                                                                                                              |
|--------------------|----------------------------|---------|-------|----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| composeFile        | compose.file               | File    | 0.9   | all            | __(required)__ Path to the docker-compose file which will be used to run docker-compose commands. docker-compose will be executed in the directory containing the docker-compose file. User property: __compose.file__                                                   |
| noColor            | compose.noColor            | boolean  | 0.9   | up, upWithLogs | __(optional)__ Use coloured output, if true. No color codes will be used, if missing. User property: __compose.noColor__                                                                                                                                                 |
| removeImagesOfType | compose.removeImagesOfType | String   | 0.9   | down           | __(optional)__ Remove images. Type must be one of: `all`: Remove all images used by any service, `local`: Remove only images that don't have a custom tag set by the `image` field. No images will be removed, if missing. User property: __compose.removeImagesOfType__ |
| removeOrphans      | compose.removeOrphans      | boolean  | 0.9   | down           | __(optional)__ Remove containers for services not defined in the docker-compose file. No containers will be removed, if missing. User property: __compose.removeOrphans__                                                                                                |
| removeVolumes      | compose.removeVolumes      | boolean  | 0.9   | down           | __(optional)__ Remove named volumes declared in the `volumes` section of the Compose file and anonymous volumes attached to containers. No volumes will be removed, if missing. User property: __compose.removeVolumnes__                                                |
| skip               | compose.skip               | boolean  | 5.1   | all            | __(optional)__ Skips execution of plugin, if set to `true`. Default: `false`.                                                                                                                                                                                            |
