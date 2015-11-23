package ro.ubbcluj.cs.domain;

import data.domain.Validator;
import data.exceptions.MyException;
import ro.ubbcluj.cs.exceptions.ValidatorException;
import ro.ubbcluj.cs.utils.DateUtils;

/**
 * Created by ZUZU on 19.10.2015.
 */
public class ExpenseValidator implements Validator<Expense> {

    @Override
    public boolean validate(Expense expense) throws MyException{
        DateValidator dateValidator = new DateValidator();
        if (!dateValidator.validate(expense.getDate()) || expense.getPrice()<0) {
            throw new ValidatorException("Invalid expense");
        }
        return true;
    }
}
