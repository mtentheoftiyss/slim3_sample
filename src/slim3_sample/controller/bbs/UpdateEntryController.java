package slim3_sample.controller.bbs;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.BeanUtil;

import slim3_sample.model.bbs.Body;
import slim3_sample.model.bbs.Head;
import slim3_sample.service.bbs.BbsService;

public class UpdateEntryController extends Controller {

    @Override
    public Navigation run() throws Exception {
        if (!isPost()) {
            // POSTではないリクエストはトップページへリダイレクト
            return redirect(basePath);
        }
        // 入力値のバリデーション
        if (!validate()) {
            // バリデーションエラーの場合は記事詳細ページへ移動
            return forward("/bbs/edit.jsp");
        }
        BbsService service = new BbsService();
        Head head = null;
        try {
            // 指定されたkeyから記事を取得
            head = service.get(asKey("key"));
        } catch (Exception e) {
            // keyが不正な場合
        }
        // 記事が取得できなかった場合
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
        // リクエストの値をHead、Bodyのプロパティにセットする
        Body body = head.getBodyRef().getModel();
        
        BeanUtil.copy(request, head);
        body.setText(asString("text"));
        // 上書き更新
        service.update(head, body);
        // 記事詳細にリダイレクト(GETパラメータで記事の主キーを指定)
        return redirect(basePath + "read?key="+asString("key"));
    }
    
    private boolean validate() {
        Validators v = new Validators(request);
        v.add("subject", v.required(),v.maxlength(50));
        v.add("userName", v.required(),v.maxlength(50));
        v.add("text", v.required());
        return v.validate();
    }
    
}
