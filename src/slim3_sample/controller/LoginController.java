package slim3_sample.controller;

import org.apache.commons.lang3.StringUtils;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3_sample.constants.Constants;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * ���O�C���p�̃R���g���[���[
 * @author 10257
 *
 */
public class LoginController extends Controller {

    @Override
    public Navigation run() throws Exception {
        String userId = null;
        // �F�؂ɐ������Ă���ꍇ�A�N�G���[�p�����[�^�[��type���ݒ肳��Ă���
        String type = asString("type");
        
        // OpenID�F�؂̏ꍇ
        if ("openid".equals(type)) {
            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();
            if (user == null) {
                return forward("/loginError.jsp");
            }
            userId = user.getUserId();
            sessionScope(Constants.SESSION_AUTH_EMAIL, "dummy");
        // OpenIDConnect�F�؂̏ꍇ
        } else if ("openidconnect".equals(type)) {
            userId = "openIdConnect";
        }
        
        // �Z�b�V������userid���i�[(����Ń��O�C�����Ă��邩�ǂ����𔻒肷��)
        sessionScope(Constants.SESSION_AUTH_USERID, userId);
        
        String _continue = sessionScope("continue");
        if (StringUtils.isBlank(_continue)) {
          _continue = "/";
        }
        return redirect(_continue);
    }
}
