package slim3_sample.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFormServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("continue", req.getParameter("continue"));
        req.getRequestDispatcher("/loginForm.jsp").forward(req, res);
//        HttpServletRequest hreq = (HttpServletRequest) req;
//        HttpServletResponse hres = (HttpServletResponse) res;
//        String _continue = hreq.getRequestURL().toString();
//        
//        if (isLoggedIn(hreq)) {
//            hres.sendRedirect(_continue);
//        } else {
//            hres.sendRedirect("/loginForm?continue=" + _continue);
//        }
    }

//    private boolean isLoggedIn(HttpServletRequest hreq) {
//        HttpSession sess = hreq.getSession();
//        return (sess != null && sess.getAttribute("userId") != null);
//    }
}
