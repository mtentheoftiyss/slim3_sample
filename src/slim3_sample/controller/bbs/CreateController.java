package slim3_sample.controller.bbs;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

/**
 * 記事の新規登録画面のコントローラー
 * @author 10257
 *
 */
public class CreateController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return forward("/bbs/create.jsp");
    }
}
