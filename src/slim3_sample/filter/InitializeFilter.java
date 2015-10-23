package slim3_sample.filter;

import java.io.IOException;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 初期化用のフィルター
 * @author 10257
 *
 */
public class InitializeFilter implements Filter {
    
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        TimeZone.setDefault(TimeZone.getTimeZone("JST"));
        chain.doFilter(req, res);
    }
    
    public void init(FilterConfig conf) throws ServletException {
    }
    
    public void destroy() {
    }

}
