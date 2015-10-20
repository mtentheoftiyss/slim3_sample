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
            // POST�ł͂Ȃ����N�G�X�g�̓g�b�v�y�[�W�փ��_�C���N�g
            return redirect(basePath);
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
        // �L���폜
        service.delete(head.getKey());
        // �g�b�v�y�[�W�փ��_�C���N�g
        return redirect(basePath);
    }
}
