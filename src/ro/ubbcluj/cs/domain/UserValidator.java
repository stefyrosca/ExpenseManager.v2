package ro.ubbcluj.cs.domain;

import data.domain.Validator;
import data.exceptions.MyException;
import ro.ubbcluj.cs.exceptions.ValidatorException;
import ro.ubbcluj.cs.utils.StringUtils;

/**
 * Created by ZUZU on 19.10.2015.
 */
public class UserValidator implements Validator<User> {

    @Override
    public boolean validate(User user) throws MyException{
        if (!StringUtils.isValid(user.getUsername()) && StringUtils.isValid(user.getPassword())) {
            throw new ValidatorException("Invalid user");
        }
        return true;
    }
}
