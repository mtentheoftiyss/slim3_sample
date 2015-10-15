package slim3_sample.controller;

import java.util.List;
import java.util.TimeZone;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3_sample.model.Head;
import slim3_sample.service.BbsService;

public class IndexController extends Controller {

    @Override
    public Navigation run() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("JST"));
        BbsService service = new BbsService();
        List<Head> headList = service.getAll();
        requestScope("headList", headList);
        return forward("index.jsp");
    }
}
