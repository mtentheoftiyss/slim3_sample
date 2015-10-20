package slim3_sample.controller;

import java.net.URLEncoder;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class OpenIdLoginController extends Controller {

    @Override
    public Navigation run() throws Exception {
        String openid_identifier = asString("openid_identifier");
        String _continue = sessionScope("continue");
        if (StringUtils.isBlank(_continue)) {
            _continue = "/";
        }
        
        UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            return redirect("/login?type=openid&continue=" + _continue);
        }
        
        // OpenIDîFèÿ
        String loginUrl = "/login?" + 
            URLEncoder.encode(URLEncoder.encode("type=openid&continue=" + 
            _continue, "utf-8"), "utf-8");
        
        String providerLoginUrl = userService.createLoginURL(
            loginUrl,
            request.getServerName(),
            openid_identifier,
            new HashSet<String>());
        
        return redirect(providerLoginUrl);
    }
}
