package com.dparadig.auth_server.settings.validators;

import com.dparadig.auth_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator of unique username
 */
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        // If the repository is null then return null
        if(userService == null){
            return true;
        }
        // Check if the username is unique
        return userService.getUserByEmail(username) == null;
    }
}
