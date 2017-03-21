package leader.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shenli on 2017/3/16.
 */
@RestController
@RequestMapping(value = "/manager")
public class ManagerController {

    @RequestMapping("findUser")
    @ResponseBody
    public String findUser(){
        return "findUser";
    }

    @RequestMapping("createuser")
    public String createUser(){
        return "createuser";
    }

    @RequestMapping("deleteuser")
    public String deleteUser(){
        return "deleteuser";
    }

    @RequestMapping("createRole")
    public String createRole(){
        return "createrole";
    }

    @RequestMapping("delRole")
    public String delRole(){
        return "delRole";
    }
}
