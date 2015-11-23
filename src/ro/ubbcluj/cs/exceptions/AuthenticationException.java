package ro.ubbcluj.cs.exceptions;

import data.exceptions.MyException;

/**
 * Created by ZUZU on 04.11.2015.
 */
public class AuthenticationException extends MyException {

    public AuthenticationException(String message) {
        super(message);
    }
}
