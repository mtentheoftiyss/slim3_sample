package slim3_sample.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.BeanUtil;

import slim3_sample.model.Body;
import slim3_sample.model.Head;
import slim3_sample.service.BbsService;

public class EditController extends Controller {

    @Override
    public Navigation run() throws Exception {
        if (!isPost() || !validate()) {
            // POST�ł͂Ȃ����N�G�X�g�A�܂��̓o���f�[�V�����G���[�̏ꍇ�͋L���ڍׂ֖߂�
            return forward("read");
        }
        BbsService service = new BbsService();
        Head head = service.get(asKey("key"));
        if (head == null) {
            // �w�肳�ꂽ�L�[�ɊY������L�����Ȃ��ꍇ�̓g�b�v�֖߂�
            errors.put("message", ApplicationMessage.get("error.entry.notfound"));
            return forward(basePath);
        }
        if (!asString("password").equals(head.getPassword())) {
            // �p�X���[�h���s��v�̏ꍇ�͋L���ڍׂ֖߂�
            errors.put("message", ApplicationMessage.get("error.password.invalid"));
            return forward("read");
        }
        // �p�X���[�h���������ꍇ�̓��N�G�X�g�X�R�[�v��Head�ABody�̃v���p�e�B�̒l���Z�b�g����
        Body body = head.getBodyRef().getModel();
        BeanUtil.copy(head, request);
        requestScope("text", body.getText());
        
        return forward("edit.jsp");
    }
    
    private boolean validate() {
        Validators v = new Validators(request);
        v.add("key", v.required());
        v.add("password", v.required());
        return v.validate();
    }
}
