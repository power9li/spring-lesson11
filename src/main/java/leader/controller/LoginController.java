package leader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by shenli on 2017/3/17.
 */
@Controller
public class LoginController {

//    @ResponseBody
    @RequestMapping("/mylogin")
    public String mylogin(){
        return "loginform";
    }

    @RequestMapping("/accessDenied")
    @ResponseBody
    public String accessDenied(HttpServletRequest request){
        String requestType = request.getHeader("X-Requested-With");
        if(requestType != null && requestType.equals("XMLHttpRequest")) {
            String requestPath = request.getRequestURI();
            return "{\"success\":false, \"reason\":\"禁止访问,请联系管理员\", \"redirectUrl\":\""+requestPath+"\"}";
        }
        else{
            return "<h2>Access Denied </h2><br>禁止访问,请联系管理员. 或 <a href='/mylogin'>重新登录</a>";
        }
    }

    @RequestMapping("/welcome")
    @ResponseBody
    public String welcome(HttpServletRequest request){
        String requestType = request.getHeader("X-Requested-With");
        if(requestType != null && requestType.equals("XMLHttpRequest")) {
            return "{\"success\":true, \"redirectUrl\":\"/welcome\"}";
        }
        else{
            return "<html><head><script src='/js/jquery.js'></script></head><body><h2>Welcome!</h2><form action='/logout' method='post'><button>logout</button></body></html>";
        }
    }
}
