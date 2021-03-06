package slim3_sample.controller.bbs;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.BeanUtil;

import slim3_sample.model.bbs.Body;
import slim3_sample.model.bbs.Head;
import slim3_sample.service.bbs.BbsService;

/**
 * 記事の編集画面用のコントローラー
 * @author 10257
 *
 */
public class EditController extends Controller {

    @Override
    public Navigation run() throws Exception {
        if (!isPost() || !validate()) {
            // POSTではないリクエスト、またはバリデーションエラーの場合は記事詳細へ戻る
            return forward("/bbs/read.jsp");
        }
        BbsService service = new BbsService();
        Head head = service.get(asKey("key"));
        if (head == null) {
            // 指定されたキーに該当する記事がない場合はトップへ戻る
            errors.put("message", ApplicationMessage.get("error.entry.notfound"));
            return forward(basePath);
        }
        if (!asString("password").equals(head.getPassword())) {
            // パスワードが不一致の場合は記事詳細へ戻る
            errors.put("message", ApplicationMessage.get("error.password.invalid"));
            return forward("/bbs/read.jsp");
        }
        // パスワードが正しい場合はリクエストスコープにHead、Bodyのプロパティの値をセットする
        Body body = head.getBodyRef().getModel();
        BeanUtil.copy(head, request);
        requestScope("text", body.getText());
        
        return forward("/bbs/edit.jsp");
    }
    
    private boolean validate() {
        Validators v = new Validators(request);
        v.add("key", v.required());
        v.add("password", v.required());
        return v.validate();
    }
}
