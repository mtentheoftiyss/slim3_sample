package slim3_sample.controller.bbs;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.BeanUtil;

import slim3_sample.constants.Constants;
import slim3_sample.model.bbs.Comment;
import slim3_sample.model.bbs.Head;
import slim3_sample.service.bbs.BbsService;

import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.mail.MailServiceFactory;

/**
 * コメント登録用のコントローラー
 * @author 10257
 *
 */
public class PostCommentController extends Controller {

    private static final Logger logger = Logger.getLogger(PostCommentController.class.getName());
    
    @Override
    public Navigation run() throws Exception {
        if (!isPost()) {
            // POSTではないリクエストはトップページへリダイレクト
            return redirect(basePath);
        }
        // 入力値のバリデーション
        if (!validate()) {
            // バリデーションエラーの場合は記事詳細ページへ移動
            return forward("/bbs/read.jsp");
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
        // コメントを作成
        Comment comment = new Comment();
        BeanUtil.copy(request, comment);
        comment.setPostDate(new Date());
        // 記事とコメントを登録
        service.insert(head, comment);
        
        // 投稿者にメール送信
        // メール作成
        String sender = sessionScope(Constants.SESSION_AUTH_EMAIL).toString();
        List<String> toList = Arrays.asList(head.getMailAddress());
        List<String> ccList = null;
        List<String> bccList = null;
        String subject = "コメント通知";
        String textBody = "コメント登録しました。";
        Message msg = new Message();
        // From
        msg.setSender(sender);
        // To
        msg.setTo(toList);
        // Cc
        msg.setCc(ccList);
        // Bcc
        msg.setBcc(bccList);
        // 件名
        msg.setSubject(subject);
        // 本文
        msg.setTextBody(textBody);
        String mailSendErr = "";
        try {
            MailServiceFactory.getMailService().send(msg);
        } catch (Exception e) {
            mailSendErr = "&mailSendErr=true";
            logger.info("メール送信エラー");
            logger.info(e.getMessage());
        }
        
        // 記事詳細にリダイレクト(GETパラメータで記事の主キーを指定)
        return redirect(basePath + "read?key=" + asString("key") + mailSendErr);
    }
    
    private boolean validate() {
        Validators v = new Validators(request);
        v.add("userName", v.required(),v.maxlength(50));
        v.add("comment", v.required());
        return v.validate();
    }
}
