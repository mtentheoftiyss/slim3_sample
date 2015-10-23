package slim3_sample.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3_sample.constants.Constants;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;


/**
 * OpenIDConnect�F�،�̃��_�C���N�g��̃R���g���[���[
 * @author 10257
 *
 */
public class OpenIdConnectVerifyController extends Controller {

    private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
    private static GoogleAuthorizationCodeFlow AUTH_FLOW = null;
    private static final GoogleIdTokenVerifier ID_TOKEN_VERIFIER = new GoogleIdTokenVerifier(HTTP_TRANSPORT, JSON_FACTORY);
    
    @Override
    public Navigation run() throws Exception {
        String _continue = sessionScope("continue");
        if (StringUtils.isBlank(_continue)) {
          _continue = "/";
        }
        
        // �N�G���[�p�����[�^�[�̎擾
        // code �A�N�Z�X�g�[�N�����擾���邽�߂̈ꎞ�I�ȃg�[�N��
        // state ���_�C���N�g�̍ۂ�state�p�����[�^�Ɠ���������
        // error �G���[���b�Z�[�W
        String _code = asString("code");
        String _state = asString("state");
        String _error = asString("error");
        
        // state���F�؎��ɐ����������̂ƈقȂ�ꍇ�A�G���[
        if (!sessionScope("state").equals(_state)) {
            return redirect("/loginError");
        }
        
        // �F�؉�ʂŕs���Ƃ����ꍇ�A�G���[
        if (!StringUtils.isBlank(_error)) {
            return redirect("/loginError");
        }
        
        try {
            // �N���C�A���g�V�[�N���b�g�̎擾(src�z����json�t�@�C����z�u���Ă���)
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, 
                new InputStreamReader(OpenIdConnectVerifyController.class.getResourceAsStream("/client_secrets.json")));
            
            AUTH_FLOW = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, 
                Arrays.asList(Constants.SCOPE)).build();
        } catch (IOException e) {
            throw e;
        }
        
        // �A�N�Z�X�g�[�N���̎擾
        String redirectUrl = _continue.replace(Constants.BASE_PATH, Constants.CALLBACK_PATH);
        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse = AUTH_FLOW.newTokenRequest(_code).setRedirectUri(redirectUrl).execute();
        } catch (TokenResponseException e) {
            throw e;
        }
        
        // ID�g�[�N���̌���
        GoogleIdToken token = GoogleIdToken.parse(JSON_FACTORY, tokenResponse.getIdToken());
        try {
            if(!ID_TOKEN_VERIFIER.verify(token)) {
                throw new GeneralSecurityException();
            }
        } catch (GeneralSecurityException e) {
            throw e;
        }
        
//        String[] jwt = ((String)tr.get("id_token")).split("\\.");
//        byte[] jwtHeader = Base64.decodeBase64(jwt[0]);
//        byte[] jwtClaim = Base64.decodeBase64(jwt[1]);
//        byte[] jwtSigniture = Base64.decodeBase64(jwt[2]);
        
        // payload����e������擾
        Payload payload = token.getPayload();
        String email = payload.getEmail();
        // �L���ɃR�����g���e���Ƀ��[�����M����̂Ń��[���A�h���X���Z�b�V�����ɕۑ�����
        sessionScope(Constants.SESSION_AUTH_EMAIL, email);
        
        // ���[�U�̃v���t�@�C�������擾
        URL url = new URL("https://www.googleapis.com/plus/v1/people/me/openIdConnect");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Host", "www.googleapis.com");
        connection.setRequestProperty("Authorization", String.format("OAuth %s", tokenResponse.getAccessToken()));
        connection.connect();
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String httpSource = new String();
        String str;
        while ( null != ( str = bufferReader.readLine() ) ) {
            httpSource = httpSource + str;
        }
        bufferReader.close();
        connection.disconnect();
        
        return redirect("/login?type=openidconnect&continue=" + _continue);
    }
}
