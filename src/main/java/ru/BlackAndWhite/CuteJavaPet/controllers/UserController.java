package ru.BlackAndWhite.CuteJavaPet.controllers;


import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.BlackAndWhite.CuteJavaPet.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(path = "admin")
    public String admin(Model model) {
        model.addAttribute("isActive", "class=\"active\"");
        return "admin";
    }

    @GetMapping(path = "registration")
    public Model registration(Model model) {
        return model;
    }

    @PostMapping(path = "registration")
    public ModelAndView registration(@RequestParam("userName") String userName,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     Model model) {
        return new ModelAndView("download")
                .addAllObjects(model.asMap())
                .addObject("registrationStatus",
                        userService.userRegistration(userName, email, password, confirmPassword));
    }

    @GetMapping(path = "login")
    public String login(@RequestParam(value = "WEB-INF/error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("WEB-INF/error", "Username or password is incorrect.");
        }
        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");
        }
        return "login";
    }

    @GetMapping(path = "logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }

    @GetMapping(path = {"/", "welcome"})
    public String welcome() {
        if (userService.getCurrentLoggedUser() != null) {
            return "redirect:/download";
        } else {
            return "welcome";
        }
    }
}
