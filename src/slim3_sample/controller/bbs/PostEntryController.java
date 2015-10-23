package slim3_sample.controller.bbs;

import java.util.Date;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import slim3_sample.constants.Constants;
import slim3_sample.model.bbs.Body;
import slim3_sample.model.bbs.Head;
import slim3_sample.service.bbs.BbsService;

/**
 * 記事の登録用のコントローラー
 * @author 10257
 *
 */
public class PostEntryController extends Controller {

    @Override
    public Navigation run() throws Exception {
        if (!isPost() || !validate()) {
            return forward("/bbs/create.jsp");
        }
        Head head = new Head();
        Body body = new Body();
        
        BeanUtil.copy(request, head);
        BeanUtil.copy(request, body);
        head.setPostDate(new Date());
        head.setMailAddress(sessionScope(Constants.SESSION_AUTH_EMAIL).toString());
        
        BbsService service = new BbsService();
        service.insert(head, body);
        
        return redirect(basePath);
    }
    
    private boolean validate() {
        Validators v = new Validators(request);
        v.add("subject", v.required(), v.maxlength(50));
        v.add("userName", v.required(), v.maxlength(50));
        v.add("text", v.required());
        v.add("password", v.required(), v.minlength(6), v.maxlength(20));
        return v.validate();
    }
}
