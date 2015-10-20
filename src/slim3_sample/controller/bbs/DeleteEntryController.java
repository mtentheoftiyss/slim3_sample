package slim3_sample.controller.bbs;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.ApplicationMessage;

import slim3_sample.model.bbs.Head;
import slim3_sample.service.bbs.BbsService;

public class DeleteEntryController extends Controller {

    @Override
    public Navigation run() throws Exception {
        if (!isPost()) {
            // POSTではないリクエストはトップページへリダイレクト
            return redirect(basePath);
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
        // 記事削除
        service.delete(head.getKey());
        // トップページへリダイレクト
        return redirect(basePath);
    }
}
