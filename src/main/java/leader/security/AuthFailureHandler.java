package leader.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by shenli on 2017/3/18.
 */
//@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

//    @Autowired
//    private UserMapper userMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        System.out.println("AuthFailureHandler.onAuthenticationFailure");
        saveException(request, exception);
        HttpSession session = request.getSession(false);
        System.out.println("session = " + session);
        if (session != null) {
            Object attribute = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            System.out.println("session.attribute = " + attribute);
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print("{\"success\": false, \"reason\": \""+exception.getMessage()+"\"}");
        response.getWriter().flush();
    }
}
