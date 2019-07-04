package group.msg.at.cloud.tools.compose.core.command;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UpCommandTest {

    private boolean composeIsUp;

    @After
    public void onAfter() {
        if (composeIsUp) {
            DownCommand down = new DownCommand();
            down.setComposeFile(Constants.COMPOSE_FILE);
            try {
                down.call();
            } catch (Exception ex) {
                System.err.println(String.format("unable to stop composed containers defined in %s: %s", Constants.COMPOSE_FILE.getAbsolutePath(), ex.getMessage()));
            }
        }
        composeIsUp = false;
    }

    @Test
    public void upCommandWorksOk() throws Exception {
        UpCommand underTest = new UpCommand();
        underTest.setComposeFile(Constants.COMPOSE_FILE);
        UpCommandResult result = underTest.call();
        composeIsUp = true;
        assertNotNull("command must return non-null result", result);
        assertEquals("command status code must be SUCCESS", CommandStatusCode.SUCCESS, result.getStatusCode());
    }
}
