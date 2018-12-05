package ru.BlackAndWhite.CuteJavaPet.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.services.UserService;


@PropertySource("classpath:/validation.properties")
@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;
    @Autowired
    private Environment env;


    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
//        RegUser user = (RegUser) o;
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "Required");
//
//        if (user.getUserName().length() < 4 || user.getUserName().length() > 32) {
//            errors.rejectValue("userName", "Size.userForm.userName", env.getProperty("Size.userForm.userName"));
//            return;
//        }
//        if (userService.findByUserName(user.getUserName()) != null) {
//            errors.rejectValue("userName", "Duplicate.userForm.userName", env.getProperty("Duplicate.userForm.userName"));
//            return;
//        }
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Required");
//        if (userService.findByUserEmail(user.getEmail()) != null) {
//            errors.rejectValue("email", "Duplicate.userForm.email", env.getProperty("Duplicate.userForm.email"));
//            return;
//        }
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Required");
//        if (user.getPassword().length() < 4 || user.getPassword().length() > 32) {
//            errors.rejectValue("password", "Size.userForm.password", env.getProperty("Size.userForm.password"));
//            return;
//        }
//        if (!user.getEncryptedPassword().equals(user.getPassword())) {
//            errors.rejectValue("confirmPassword", "Different.userForm.password", env.getProperty("Different.userForm.password"));
//            return;
//        }
    }

    public String validateUser(User user, String confirmPassword) {

        if (user.getUserName().length() < 4 || user.getUserName().length() > 32) {
            return env.getProperty("Size.userForm.userName");
        }
        if (userService.findByUserName(user.getUserName()) != null) {
            return env.getProperty("Duplicate.userForm.userName");
        }
        if (userService.findByUserEmail(user.getEmail()) != null) {
            return env.getProperty("Duplicate.userForm.email");
        }
        if (user.getPassword().length() < 4 || user.getPassword().length() > 32) {
            return env.getProperty("Size.userForm.password");
        }
        if (!confirmPassword.equals(user.getPassword())) {
            return env.getProperty("Different.userForm.password");
        }
        return null;
    }
}
