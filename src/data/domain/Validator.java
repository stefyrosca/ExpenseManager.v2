package data.domain;

import data.exceptions.MyException;

/**
 * Created by ZUZU on 19.10.2015.
 */
public interface Validator<T> {
    boolean validate(T t) throws MyException;
}
