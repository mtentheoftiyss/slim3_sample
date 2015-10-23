package slim3_sample.constants;

/**
 * 定数定義クラス
 * @author 10257
 *
 */
public class Constants {
    private Constants() {}
    
    // ベースパス
    public static final String BASE_PATH = "/bbs";
    // OpenIDConnect認証後のリダイレクト先パス
    public static final String CALLBACK_PATH = "/openIdConnectVerify";
    // 認証時に要求する権限
    public static final String SCOPE = "openid email profile";
    
    // セッション格納用変数名
    // ユーザーID
    public static final String SESSION_AUTH_USERID = "auth_userid";
    // メールアドレス
    public static final String SESSION_AUTH_EMAIL = "auth_email";
    
}
