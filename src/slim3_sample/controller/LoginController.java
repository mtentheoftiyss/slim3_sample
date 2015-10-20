package slim3_sample.controller;

import org.apache.commons.lang3.StringUtils;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LoginController extends Controller {

    @Override
    public Navigation run() throws Exception {
        String userId = null;
        String type = asString("type");
        
        if ("openid".equals(type)) {
            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();
            if (user == null) {
                return forward("/loginError.jsp");
            }
            userId = user.getUserId();
        } else if ("openidconnect".equals(type)) {
            // TODO もろもろチェック
            userId = "openIdConnect";
        }
        
        sessionScope("userId", userId);
        
        String _continue = sessionScope("continue");
        if (StringUtils.isBlank(_continue)) {
          _continue = "/";
        }
        return redirect(_continue);
    }
}
