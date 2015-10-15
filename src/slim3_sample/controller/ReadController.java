package slim3_sample.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.ApplicationMessage;

import slim3_sample.model.Head;
import slim3_sample.service.BbsService;

import com.google.appengine.api.datastore.Key;

public class ReadController extends Controller {

    @Override
    public Navigation run() throws Exception {
        BbsService service = new BbsService();
        Head head = null;
        try {
            // 指定されたkeyから記事を取得
            Key key = asKey("key");
            head = service.get(key);
        } catch (Exception e) {
            // keyが不正な場合
        }
        // 記事が取得できなかった場合
        if (head == null) {
            // エラーメッセージをセットしてトップページへフォワード
            errors.put("message", ApplicationMessage.get("error.entry.notfound"));
            return forward(basePath);
        }
        // 記事が存在する場合はリクエストスコープへセット
        requestScope("head", head); // 記事ヘッダ
        requestScope("body", head.getBodyRef().getModel()); // 記事本文
        requestScope("commentList", head.getCommentRef().getModelList()); // コメント一覧
        return forward("read.jsp");
    }
}
