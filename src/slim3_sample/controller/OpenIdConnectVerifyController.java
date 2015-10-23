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
 * OpenIDConnect認証後のリダイレクト先のコントローラー
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
        
        // クエリーパラメーターの取得
        // code アクセストークンを取得するための一時的なトークン
        // state リダイレクトの際のstateパラメータと同じ文字列
        // error エラーメッセージ
        String _code = asString("code");
        String _state = asString("state");
        String _error = asString("error");
        
        // stateが認証時に生成したものと異なる場合、エラー
        if (!sessionScope("state").equals(_state)) {
            return redirect("/loginError");
        }
        
        // 認証画面で不許可とした場合、エラー
        if (!StringUtils.isBlank(_error)) {
            return redirect("/loginError");
        }
        
        try {
            // クライアントシークレットの取得(src配下にjsonファイルを配置しておく)
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, 
                new InputStreamReader(OpenIdConnectVerifyController.class.getResourceAsStream("/client_secrets.json")));
            
            AUTH_FLOW = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, 
                Arrays.asList(Constants.SCOPE)).build();
        } catch (IOException e) {
            throw e;
        }
        
        // アクセストークンの取得
        String redirectUrl = _continue.replace(Constants.BASE_PATH, Constants.CALLBACK_PATH);
        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse = AUTH_FLOW.newTokenRequest(_code).setRedirectUri(redirectUrl).execute();
        } catch (TokenResponseException e) {
            throw e;
        }
        
        // IDトークンの検証
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
        
        // payloadから各種情報を取得
        Payload payload = token.getPayload();
        String email = payload.getEmail();
        // 記事にコメント投稿時にメール送信するのでメールアドレスをセッションに保存する
        sessionScope(Constants.SESSION_AUTH_EMAIL, email);
        
        // ユーザのプロファイル情報を取得
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
