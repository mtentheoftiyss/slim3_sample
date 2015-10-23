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
 * �R�����g�o�^�p�̃R���g���[���[
 * @author 10257
 *
 */
public class PostCommentController extends Controller {

    private static final Logger logger = Logger.getLogger(PostCommentController.class.getName());
    
    @Override
    public Navigation run() throws Exception {
        if (!isPost()) {
            // POST�ł͂Ȃ����N�G�X�g�̓g�b�v�y�[�W�փ��_�C���N�g
            return redirect(basePath);
        }
        // ���͒l�̃o���f�[�V����
        if (!validate()) {
            // �o���f�[�V�����G���[�̏ꍇ�͋L���ڍ׃y�[�W�ֈړ�
            return forward("/bbs/read.jsp");
        }
        BbsService service = new BbsService();
        Head head = null;
        try {
            // �w�肳�ꂽkey����L�����擾
            head = service.get(asKey("key"));
        } catch (Exception e) {
            // key���s���ȏꍇ
        }
        // �L�����擾�ł��Ȃ������ꍇ
        if (head == null) {
            // �w�肳�ꂽ�L�[�ɊY������L�����Ȃ��ꍇ�̓g�b�v�֖߂�
            errors.put("message", ApplicationMessage.get("error.entry.notfound"));
            return forward(basePath);
        }
        // �R�����g���쐬
        Comment comment = new Comment();
        BeanUtil.copy(request, comment);
        comment.setPostDate(new Date());
        // �L���ƃR�����g��o�^
        service.insert(head, comment);
        
        // ���e�҂Ƀ��[�����M
        // ���[���쐬
        String sender = sessionScope(Constants.SESSION_AUTH_EMAIL).toString();
        List<String> toList = Arrays.asList(head.getMailAddress());
        List<String> ccList = null;
        List<String> bccList = null;
        String subject = "�R�����g�ʒm";
        String textBody = "�R�����g�o�^���܂����B";
        Message msg = new Message();
        // From
        msg.setSender(sender);
        // To
        msg.setTo(toList);
        // Cc
        msg.setCc(ccList);
        // Bcc
        msg.setBcc(bccList);
        // ����
        msg.setSubject(subject);
        // �{��
        msg.setTextBody(textBody);
        String mailSendErr = "";
        try {
            MailServiceFactory.getMailService().send(msg);
        } catch (Exception e) {
            mailSendErr = "&mailSendErr=true";
            logger.info("���[�����M�G���[");
            logger.info(e.getMessage());
        }
        
        // �L���ڍׂɃ��_�C���N�g(GET�p�����[�^�ŋL���̎�L�[���w��)
        return redirect(basePath + "read?key=" + asString("key") + mailSendErr);
    }
    
    private boolean validate() {
        Validators v = new Validators(request);
        v.add("userName", v.required(),v.maxlength(50));
        v.add("comment", v.required());
        return v.validate();
    }
}
