package slim3_sample.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class BodyTest extends AppEngineTestCase {

    private Body model = new Body();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
