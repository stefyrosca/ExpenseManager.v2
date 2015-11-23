package ro.ubbcluj.cs.domain;

import data.domain.Validator;
import data.exceptions.MyException;
import ro.ubbcluj.cs.exceptions.ValidatorException;

/**
 * Created by ZUZU on 19.10.2015.
 */
public class DateValidator implements Validator<Date> {
    @Override
    public boolean validate(Date date) throws MyException {
        if (date.getDay() < 0 && date.getDay() > 31) {
            if (date.getMonth() < 0 && date.getMonth() > 13) {
                if (date.getYear() < 1900 && date.getYear() > 2015) {
                    throw new ValidatorException("Date not valid");
                }
            }
        }
        return true;
    }
}