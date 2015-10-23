package slim3_sample.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import slim3_sample.constants.Constants;

/**
 * ログインチェック用のフィルター
 * @author 10257
 *
 */
public class LoginCheckFilter implements Filter {
    
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hreq = (HttpServletRequest) req;
        HttpServletResponse hres = (HttpServletResponse) res;
        
        // ログインチェック
        if (isLoggedIn(hreq)) {
            chain.doFilter(req, res);
        } else {
            // ログインしていなかったらログイン画面へ遷移する
            HttpSession sess = hreq.getSession();
            sess.setAttribute("continue", hreq.getRequestURL().toString());
            hres.sendRedirect("/loginForm");
        }
    }
    
    /**
     * ログインチェック
     * @param hreq
     * @return ログインしている:true/ログインしていない:false
     */
    private boolean isLoggedIn(HttpServletRequest hreq) {
        // セッションにuseridが設定されている場合、ログインしていると判断する
        HttpSession sess = hreq.getSession();
        return (sess != null && sess.getAttribute(Constants.SESSION_AUTH_USERID) != null);
    }
    
    public void init(FilterConfig conf) throws ServletException {
    }
    
    public void destroy() {
    }

}
