package slim3_sample.controller.bbs;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

import slim3_sample.controller.bbs.UpdateEntryController;

public class UpdateEntryControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/updateEntry");
        UpdateEntryController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }
}
