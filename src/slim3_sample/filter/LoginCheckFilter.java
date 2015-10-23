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
 * ���O�C���`�F�b�N�p�̃t�B���^�[
 * @author 10257
 *
 */
public class LoginCheckFilter implements Filter {
    
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hreq = (HttpServletRequest) req;
        HttpServletResponse hres = (HttpServletResponse) res;
        
        // ���O�C���`�F�b�N
        if (isLoggedIn(hreq)) {
            chain.doFilter(req, res);
        } else {
            // ���O�C�����Ă��Ȃ������烍�O�C����ʂ֑J�ڂ���
            HttpSession sess = hreq.getSession();
            sess.setAttribute("continue", hreq.getRequestURL().toString());
            hres.sendRedirect("/loginForm");
        }
    }
    
    /**
     * ���O�C���`�F�b�N
     * @param hreq
     * @return ���O�C�����Ă���:true/���O�C�����Ă��Ȃ�:false
     */
    private boolean isLoggedIn(HttpServletRequest hreq) {
        // �Z�b�V������userid���ݒ肳��Ă���ꍇ�A���O�C�����Ă���Ɣ��f����
        HttpSession sess = hreq.getSession();
        return (sess != null && sess.getAttribute(Constants.SESSION_AUTH_USERID) != null);
    }
    
    public void init(FilterConfig conf) throws ServletException {
    }
    
    public void destroy() {
    }

}
