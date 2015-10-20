package slim3_sample.controller.bbs;

import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3_sample.model.bbs.Head;
import slim3_sample.service.bbs.BbsService;

public class IndexController extends Controller {

    private static final Logger logger = Logger.getLogger(IndexController.class.getName());
    @Override
    public Navigation run() throws Exception {
        logger.info("index‚Å‚·");
        TimeZone.setDefault(TimeZone.getTimeZone("JST"));
        BbsService service = new BbsService();
        List<Head> headList = service.getAll();
        requestScope("headList", headList);
        return forward("/bbs/index.jsp");
    }
}
