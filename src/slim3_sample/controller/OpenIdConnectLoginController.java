package slim3_sample.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3_sample.constants.Constants;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 * OpenIDConnect認証用のコントローラー
 * @author 10257
 *
 */
public class OpenIdConnectLoginController extends Controller {

    // 定数定義
    private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
    private static GoogleAuthorizationCodeFlow AUTH_FLOW = null;

    @Override
    public Navigation run() throws Exception {
        String _continue = sessionScope("continue");
        if (StringUtils.isBlank(_continue)) {
          _continue = "/";
        }
        
        try {
            // クライアントシークレットの取得(src配下にjsonファイルを配置しておく)
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, 
                new InputStreamReader(OpenIdConnectLoginController.class.getResourceAsStream("/client_secrets.json")));
            
            AUTH_FLOW = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, 
                Arrays.asList(Constants.SCOPE)).build();
        } catch (IOException e) {
            throw e;
        }
        
        // stateの生成(ランダム)
        String state = new BigInteger(130, new SecureRandom()).toString(32);
        sessionScope("state", state);
        // 認証後のリダイレクトURL(Google Developers Consoleで設定した値)
        String redirectUrl = _continue.replace(Constants.BASE_PATH, Constants.CALLBACK_PATH);
        
        // 認証URLの生成
        GoogleAuthorizationCodeRequestUrl authorizationCodeRequestUrl = AUTH_FLOW.newAuthorizationUrl();
        authorizationCodeRequestUrl.setRedirectUri(redirectUrl);
        authorizationCodeRequestUrl.setState(state);
        authorizationCodeRequestUrl.set("openid.realm", _continue.replace(Constants.BASE_PATH, ""));
        return redirect(authorizationCodeRequestUrl.build());
    }
}
