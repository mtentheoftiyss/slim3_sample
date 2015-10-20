package slim3_sample.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import slim3_sample.service.bbs.BbsService;

public class BbsServiceTest extends AppEngineTestCase {

    private BbsService service = new BbsService();

    @Test
    public void test() throws Exception {
        assertThat(service, is(notNullValue()));
    }
}
