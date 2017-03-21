package leader.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by shenli on 2017/3/18.
 */
//@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    public AuthSuccessHandler(){
        System.out.println("AuthSuccessHandler.AuthSuccessHandler");
        setDefaultTargetUrl("/welcome");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        System.out.println("AuthSuccessHandler.onAuthenticationSuccess");
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        System.out.println("savedRequest = " + savedRequest);

        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParameter != null && StringUtils.hasText(request
                .getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        clearAuthenticationAttributes(request);

        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);

        response.setStatus(HttpServletResponse.SC_OK);
        String requestType = request.getHeader("X-Requested-With");
        if(requestType != null && requestType.equals("XMLHttpRequest")) {
            System.out.println("ash AJAX");
            response.getWriter().print("{\"success\": true, \"redirectUrl\": \"" + targetUrl + "\"}");
            response.getWriter().flush();
        }
        else{
            System.out.println("ash NO AJAX");
            response.sendRedirect(targetUrl);
        }
    }

    @Override
    public void setRequestCache(RequestCache requestCache) {
        System.out.println("AuthSuccessHandler.setRequestCache");
        this.requestCache = requestCache;
    }
}
