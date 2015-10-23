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
 * OpenIDConnect�F�ؗp�̃R���g���[���[
 * @author 10257
 *
 */
public class OpenIdConnectLoginController extends Controller {

    // �萔��`
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
            // �N���C�A���g�V�[�N���b�g�̎擾(src�z����json�t�@�C����z�u���Ă���)
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, 
                new InputStreamReader(OpenIdConnectLoginController.class.getResourceAsStream("/client_secrets.json")));
            
            AUTH_FLOW = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, 
                Arrays.asList(Constants.SCOPE)).build();
        } catch (IOException e) {
            throw e;
        }
        
        // state�̐���(�����_��)
        String state = new BigInteger(130, new SecureRandom()).toString(32);
        sessionScope("state", state);
        // �F�،�̃��_�C���N�gURL(Google Developers Console�Őݒ肵���l)
        String redirectUrl = _continue.replace(Constants.BASE_PATH, Constants.CALLBACK_PATH);
        
        // �F��URL�̐���
        GoogleAuthorizationCodeRequestUrl authorizationCodeRequestUrl = AUTH_FLOW.newAuthorizationUrl();
        authorizationCodeRequestUrl.setRedirectUri(redirectUrl);
        authorizationCodeRequestUrl.setState(state);
        authorizationCodeRequestUrl.set("openid.realm", _continue.replace(Constants.BASE_PATH, ""));
        return redirect(authorizationCodeRequestUrl.build());
    }
}
