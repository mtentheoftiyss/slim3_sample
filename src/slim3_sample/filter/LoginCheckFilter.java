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

public class LoginCheckFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hreq = (HttpServletRequest) req;
        HttpServletResponse hres = (HttpServletResponse) res;
        
        if (isLoggedIn(hreq)) {
            chain.doFilter(req, res);
        } else {
            HttpSession sess = hreq.getSession();
            sess.setAttribute("continue", hreq.getRequestURL().toString());
            hres.sendRedirect("/loginForm");
        }
    }

    private boolean isLoggedIn(HttpServletRequest hreq) {
        HttpSession sess = hreq.getSession();
        return (sess != null && sess.getAttribute("userId") != null);
    }

    public void init(FilterConfig conf) throws ServletException {
    }

    public void destroy() {
    }

}
