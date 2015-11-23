package ro.ubbcluj.cs.Repository;


import data.domain.Validator;
import data.exceptions.MyException;
import data.repository.AbstractRepository;
import ro.ubbcluj.cs.domain.Date;
import ro.ubbcluj.cs.domain.Expense;
import ro.ubbcluj.cs.domain.ExpenseValidator;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.utils.repository.ExpenseRepositoryUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class ExpenseRepository extends AbstractRepository<Expense> {

    public ExpenseRepository() {
        entities = new ArrayList<>();
        setValidator(new ExpenseValidator());
        lastID = size();
        entities  = new ArrayList<>();
        try {
            repositoryUtils = new ExpenseRepositoryUtils(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setValidator(Validator v) {
        this.validator = v;
    }

    @Override
    public int size() {
        log.log(Level.FINEST, "Returning number of expenses");
        return entities.size();
    }


    public ArrayList<Expense> findByDate(Date date, ArrayList<Expense> allExpenses) {
        log.log(Level.FINEST, "Searching for expenses on the date {0} ", date);
        ArrayList<Expense> expenses = new ArrayList<>();
        for (Expense expense: allExpenses) {
            if (expense.getDate()==date) {
                expenses.add(expense);
            }
        }
        if (expenses.size()==0) {
            log.log(Level.FINER, "No expenses found");
            return null;
        }
        log.log(Level.FINEST, "Returning {0} expenses", expenses.size());
        return expenses;
    }


}
