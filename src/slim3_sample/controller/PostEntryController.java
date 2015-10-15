package slim3_sample.controller;

import java.util.Date;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import slim3_sample.model.Body;
import slim3_sample.model.Head;
import slim3_sample.service.BbsService;

public class PostEntryController extends Controller {

    @Override
    public Navigation run() throws Exception {
        if (!isPost() || !validate()) {
            return forward("create");
        }
        Head head = new Head();
        Body body = new Body();
        
        BeanUtil.copy(request, head);
        BeanUtil.copy(request, body);
        head.setPostDate(new Date());
        
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
