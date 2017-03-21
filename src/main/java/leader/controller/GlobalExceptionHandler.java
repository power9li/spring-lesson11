package leader.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by shenli on 2017/2/22.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler(){
        System.out.println("GlobalExceptionHandler.GlobalExceptionHandler");
    }

    public static final String DEFAULT_ERROR_VIEW = "error";
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JSONObject defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        System.out.println("GlobalExceptionHandler.defaultErrorHandler");
        e.printStackTrace();
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("exception", e);
//        mav.addObject("url", req.getRequestURL());
//        mav.addObject("timestamp", System.currentTimeMillis());
//        mav.addObject("status", "-1");
//        mav.addObject("error", e.getMessage());
//        mav.addObject("message", e.getMessage());
//        mav.setViewName("jsonError");
//        return mav;
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("success", false);
        jsonObj.put("reason", e.getMessage());
        return jsonObj;
    }


    @ExceptionHandler(value = MultipartException.class)
    public ModelAndView uploadExceptionHandler(HttpServletRequest req, MultipartException ex) throws IOException {
        System.out.println("GlobalExceptionHandler.uploadExceptionHandler");
        System.out.println("ex.getCause() = " + ex.getCause());
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("timestamp", System.currentTimeMillis());
        mav.addObject("error", ex.getMessage());
        mav.addObject("status", "-1");
        mav.addObject("message", ex.getMessage());
        mav.setViewName("uploadError");
        return mav;
    }




//    @ExceptionHandler
//    public ModelAndView handleException(Exception e, HttpServletRequest req, HttpServletResponse response) throws IOException {
//        System.out.println("GlobalExceptionHandler.handleException");
//        e.printStackTrace();
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("exception", e);
//        mav.addObject("url", req.getRequestURL());
//        mav.setViewName("error");
//        return mav;
//    }

}
