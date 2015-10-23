package slim3_sample.controller.bbs;

import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3_sample.model.bbs.Head;
import slim3_sample.service.bbs.BbsService;

/**
 * 記事の一覧表示画面用のコントローラー
 * @author 10257
 *
 */
public class IndexController extends Controller {

    @Override
    public Navigation run() throws Exception {
        BbsService service = new BbsService();
        List<Head> headList = service.getAll();
        requestScope("headList", headList);
        return forward("/bbs/index.jsp");
    }
}
