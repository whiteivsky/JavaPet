package ru.BlackAndWhite.CuteJavaPet.controllers;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Log4j
@Controller
public class ErrorController extends AbstractErrorController {
    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @Autowired
    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    public ErrorController(ErrorAttributes errorAttributes, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
    }

    @RequestMapping(ERROR_PATH)
    public ModelAndView handleErrors(HttpServletRequest request) {

        Map<String, Object> map = ResponseEntity.status(getStatus(request)).body(getErrorAttributes(request, false)).getBody();

        ModelAndView modelAndView = new ModelAndView("errorPage")
                .addObject("timestamp", map.get("timestamp"))
                .addObject("status", map.get("status"))
                .addObject("error", map.get("error"))
                .addObject("message", map.get("message"))
                .addObject("path", map.get("path"));

        return modelAndView;
    }
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        return handleErrors(request);
    }
}
