package slim3_sample.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import slim3_sample.model.bbs.Comment;

public class CommentTest extends AppEngineTestCase {

    private Comment model = new Comment();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
