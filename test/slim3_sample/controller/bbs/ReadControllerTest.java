package slim3_sample.controller.bbs;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

import slim3_sample.controller.bbs.ReadController;

public class ReadControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/read");
        ReadController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/read.jsp"));
    }
}
