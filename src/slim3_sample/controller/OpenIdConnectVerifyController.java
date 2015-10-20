package slim3_sample.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3_sample.constants.Constants;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;

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
        
        // TODO もろもろチェック
        
        // クエリーパラメーター
        String _code = asString("code");
        String _state = asString("state");
        String _error = asString("error");
        
        // state チェック
        if (!sessionScope("state").equals(_state)) {
            return redirect("/loginError");
        }
        
        // error チェック
        if (!StringUtils.isBlank(_error)) {
            return redirect("/loginError");
        }
        
        
        // アクセストークンの取得
        AuthorizationCodeTokenRequest tokenUrl = new AuthorizationCodeTokenRequest(
            HTTP_TRANSPORT, 
            JSON_FACTORY, 
            new GenericUrl("https://accounts.google.com/o/oauth2/token"), 
            _code);
        tokenUrl.setGrantType("authorization_code");
        tokenUrl.setRedirectUri(_continue.replace(Constants.BASE_PATH, Constants.CALLBACK_PATH));
        tokenUrl.set("client_id", Constants.CLIENT_ID);
        tokenUrl.set("client_secret", Constants.CLIENT_SECRET);
        TokenResponse tr = null;
        try {
            tr = tokenUrl.execute();
        } catch (IOException e) {
            throw e;
        }
        
        // id_tokenのパース
        String[] jwt = ((String)tr.get("id_token")).split("\\.");
        byte[] jwtHeader = Base64.decodeBase64(jwt[0]);
        byte[] jwtClaim = Base64.decodeBase64(jwt[1]);
        byte[] jwtSigniture = Base64.decodeBase64(jwt[2]);
        
        // ユーザプロファイル情報の取得
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("accesstoken", tr.getAccessToken());
        attributes.put("refreshtoken",
                 tr.getRefreshToken() == null ? "null" : tr.getRefreshToken());
        attributes.put("tokentype", tr.getTokenType());
        attributes.put("expire", tr.getExpiresInSeconds());
        attributes.put("jwtheader", new String(jwtHeader));
        attributes.put("jwtclaim", new String(jwtClaim));
        attributes.put("jwtsign", new String(jwtSigniture));

        try{
            JsonParser jsonParser
                          = JSON_FACTORY.createJsonParser(new String(jwtClaim));
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String name = jsonParser.getCurrentName();
                if(name != null) {
                    jsonParser.nextToken();
                    
                    if ("iss".equals(name)) {
                        attributes.put("iss", jsonParser.getText());
                    } else if ("sub".equals(name)) {
                        attributes.put("sub", jsonParser.getText());
                    } else if ("azp".equals(name)) {
                        attributes.put("azp", jsonParser.getText());
                    } else if ("email".equals(name)) {
                        attributes.put("email", jsonParser.getText());
                    } else if ("at_hash".equals(name)) {
                        attributes.put("at_hash", jsonParser.getText());
                    } else if ("email_verified".equals(name)) {
                        attributes.put("email_verified", jsonParser.getText());
                    } else if ("aud".equals(name)) {
                        attributes.put("aud", jsonParser.getText());
                    } else if ("iat".equals(name)) {
                        attributes.put("iat", jsonParser.getText());
                    } else if ("exp".equals(name)) {
                        attributes.put("exp", jsonParser.getText());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return redirect("/login?type=openidconnect&continue=" + _continue);
    }
}
