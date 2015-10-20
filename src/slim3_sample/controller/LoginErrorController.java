package slim3_sample.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class LoginErrorController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return forward("/loginError.jsp");
    }
}
