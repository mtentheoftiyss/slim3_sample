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
            // POST�ł͂Ȃ����N�G�X�g�̓g�b�v�y�[�W�փ��_�C���N�g
            return redirect(basePath);
        }
        // ���͒l�̃o���f�[�V����
        if (!validate()) {
            // �o���f�[�V�����G���[�̏ꍇ�͋L���ڍ׃y�[�W�ֈړ�
            return forward("/bbs/edit.jsp");
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
        if (!asString("password").equals(head.getPassword())) {
            // �p�X���[�h���s��v�̏ꍇ�͋L���ڍׂ֖߂�
            errors.put("message", ApplicationMessage.get("error.password.invalid"));
            return forward("/bbs/read.jsp");
        }
        // ���N�G�X�g�̒l��Head�ABody�̃v���p�e�B�ɃZ�b�g����
        Body body = head.getBodyRef().getModel();
        
        BeanUtil.copy(request, head);
        body.setText(asString("text"));
        // �㏑���X�V
        service.update(head, body);
        // �L���ڍׂɃ��_�C���N�g(GET�p�����[�^�ŋL���̎�L�[���w��)
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
