package slim3_sample.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import slim3_sample.model.bbs.Head;

public class HeadTest extends AppEngineTestCase {

    private Head model = new Head();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
