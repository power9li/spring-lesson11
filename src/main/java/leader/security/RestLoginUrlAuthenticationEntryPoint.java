package leader.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by shenli on 2017/3/19.
 */
public class RestLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public RestLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("header -- name=" + headerName +",value="+request.getHeader(headerName));
        }

        HttpSession session = request.getSession();
        if (session != null) {
            System.out.println("session = " + session);
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            String errorMsg = ex != null ? ex.getMessage() : "none";
            System.out.println("errorMsg = " + errorMsg);
        }
        else{
            System.out.println("session is null.");
        }
        String loginUrl = buildHttpsRedirectUrlForRequest(request);
        System.out.println("loginUrl = " + loginUrl);
        String redirectUrl = buildRedirectUrlToLoginPage(request, response, authException);
        System.out.println("redirectUrl = " + redirectUrl);

        String requestType = request.getHeader("X-Requested-With");
        if(requestType != null && requestType.equals("XMLHttpRequest")) {
            System.out.println("is AJAX");
            String requestPath = request.getRequestURI();
            response.setContentType("application/json;charset=UTF-8");
            String respContent = "{\"success\":false, \"reason\":\"" + authException.getMessage() + "\", \"requestURI\":\""+requestPath+"\"}";
            response.setContentLength(respContent.length());
            response.getWriter().write(respContent);
        }
        else{
            System.out.println("not AJAX");
            super.commence(request,response,authException);
        }


//        super.commence(request, response, authException);
    }
}
