package slim3_sample.controller;

import java.util.Date;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.BeanUtil;

import slim3_sample.model.Comment;
import slim3_sample.model.Head;
import slim3_sample.service.BbsService;

public class PostCommentController extends Controller {

    @Override
    public Navigation run() throws Exception {
        if (!isPost()) {
            // POST�ł͂Ȃ����N�G�X�g�̓g�b�v�y�[�W�փ��_�C���N�g
            return redirect(basePath);
        }
        // ���͒l�̃o���f�[�V����
        if (!validate()) {
            // �o���f�[�V�����G���[�̏ꍇ�͋L���ڍ׃y�[�W�ֈړ�
            return forward("read");
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
        // �L���ڍׂɃ��_�C���N�g(GET�p�����[�^�ŋL���̎�L�[���w��)
        return redirect(basePath + "read?key="+asString("key"));
    }
    
    private boolean validate() {
        Validators v = new Validators(request);
        v.add("userName", v.required(),v.maxlength(50));
        v.add("comment", v.required());
        return v.validate();
    }
}
