package slim3_sample.controller;

import org.apache.commons.lang3.StringUtils;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3_sample.constants.Constants;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * ログイン用のコントローラー
 * @author 10257
 *
 */
public class LoginController extends Controller {

    @Override
    public Navigation run() throws Exception {
        String userId = null;
        // 認証に成功している場合、クエリーパラメーターにtypeが設定されている
        String type = asString("type");
        
        // OpenID認証の場合
        if ("openid".equals(type)) {
            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();
            if (user == null) {
                return forward("/loginError.jsp");
            }
            userId = user.getUserId();
            sessionScope(Constants.SESSION_AUTH_EMAIL, "dummy");
        // OpenIDConnect認証の場合
        } else if ("openidconnect".equals(type)) {
            userId = "openIdConnect";
        }
        
        // セッションにuseridを格納(これでログインしているかどうかを判定する)
        sessionScope(Constants.SESSION_AUTH_USERID, userId);
        
        String _continue = sessionScope("continue");
        if (StringUtils.isBlank(_continue)) {
          _continue = "/";
        }
        return redirect(_continue);
    }
}
