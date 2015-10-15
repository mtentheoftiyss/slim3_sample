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
            // �w�肳�ꂽkey����L�����擾
            Key key = asKey("key");
            head = service.get(key);
        } catch (Exception e) {
            // key���s���ȏꍇ
        }
        // �L�����擾�ł��Ȃ������ꍇ
        if (head == null) {
            // �G���[���b�Z�[�W���Z�b�g���ăg�b�v�y�[�W�փt�H���[�h
            errors.put("message", ApplicationMessage.get("error.entry.notfound"));
            return forward(basePath);
        }
        // �L�������݂���ꍇ�̓��N�G�X�g�X�R�[�v�փZ�b�g
        requestScope("head", head); // �L���w�b�_
        requestScope("body", head.getBodyRef().getModel()); // �L���{��
        requestScope("commentList", head.getCommentRef().getModelList()); // �R�����g�ꗗ
        return forward("read.jsp");
    }
}
