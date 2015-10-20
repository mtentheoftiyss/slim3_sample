package slim3_sample.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3_sample.constants.Constants;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;

public class OpenIdConnectLoginController extends Controller {

    @Override
    public Navigation run() throws Exception {
        String openid_identifier = asString("openid_identifier");
        String _continue = sessionScope("continue");
        if (StringUtils.isBlank(_continue)) {
          _continue = "/";
        }
        
        String state = new BigInteger(130, new SecureRandom()).toString(32);
        sessionScope("state", state);
        String redirectUrl = _continue.replace(Constants.BASE_PATH, Constants.CALLBACK_PATH);
        
        AuthorizationCodeRequestUrl codeUrl = new AuthorizationCodeRequestUrl(openid_identifier, Constants.CLIENT_ID);
        codeUrl.setScopes(Arrays.asList("openid email profile"));
        codeUrl.setResponseTypes(Arrays.asList("code"));
        codeUrl.setRedirectUri(redirectUrl);
        codeUrl.setState(state);
        codeUrl.set("continue", _continue);
        codeUrl.set("type", "openidconnect");
        String loginUrl = codeUrl.build();
        return redirect(loginUrl);
    }
}
