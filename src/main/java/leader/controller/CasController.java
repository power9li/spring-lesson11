package leader.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shenli on 2017/3/20.
 */
@Controller
@RequestMapping("/cas")
public class CasController {

    @RequestMapping("/hello")
    public String hello() {
        return "不验证哦";
    }

    @PreAuthorize("hasAuthority('op_createuser')")
    @RequestMapping("/security")
    public String security() {
        return "hello world security";
    }

    @PreAuthorize("hasAuthority('admin')")//必须要有ADMIN权限的才能访问
    @RequestMapping("/authorize")
    public String authorize() {
        return "有权限访问";
    }
}
