package ro.ubbcluj.cs.utils.repository;

import data.exceptions.MyException;
import data.repository.CRUDRepository;
import data.utils.repository.RepositoryUtils;
import ro.ubbcluj.cs.Repository.ExpenseRepository;
import ro.ubbcluj.cs.domain.Expense;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.exceptions.RepositoryException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ZUZU on 13.11.2015.
 */
public class ExpenseRepositoryUtils implements RepositoryUtils {
    CRUDRepository<Expense> expenseRepository;
    Logger log;
    ArrayList<Expense> entities;

    public ExpenseRepositoryUtils(CRUDRepository<Expense> expenseRepository) throws Exception {
        this.expenseRepository = expenseRepository;
        log = expenseRepository.getLog();
        entities = (ArrayList<Expense>)expenseRepository.findAll();
    }

    @Override
    public boolean checkExistence(Object o) throws RepositoryException {
        Expense expense = (Expense) o;
        log.log(Level.FINEST, "Checking existence in memory for expense {0}", expense);
        if (findByUserAndTitle(expense.getUser(),expense.getTitle())!=null) {
            log.log(Level.FINE, "Expense already exists");
            throw new RepositoryException("An expense with that title already exists");
        }
        log.log(Level.FINER, "Expense doesn't exist");
        return false;
    }

    public Expense findByUserAndTitle(User user, String title) {
        log.log(Level.FINEST,"Searching for expense with title {0} " + title + " from user {0} " + user);
        for (Expense e: entities) {
            if (e.getUser().equals(user) && e.getTitle().equals(title)) {
                log.log(Level.FINER,"Expense found");
                return e;
            }
        }
        log.log(Level.FINER, "Expense not found");
        return null;
    }

    @Override
    public void setEntityID(Object entity, int ID) {
        Expense expense = (Expense) entity;
        expense.setExpenseID(ID);
    }

    @Override
    public int getEntityID(Object entity) {
        Expense expense = (Expense) entity;
        return expense.getExpenseID();
    }

    public ArrayList<Expense> findByUser(User user) {
        log.log(Level.FINEST, "Searching expenses by user {0}", user);
        ArrayList<Expense> expenses = new ArrayList<>();
        for (Expense expense: entities) {
            if (expense.getUser().equals(user)) {
                expenses.add(expense);
            }
        }
        if (expenses.size()==0) {
            log.log(Level.FINE, "No expenses found");
            return null;
        }
        log.log(Level.FINEST, "Returning {0} expenses", expenses.size());
        return expenses;
    }

    public void deleteExpensesForUser(User user) throws MyException {
        log.log(Level.FINEST, "Deleting expenses for user {0}", user);
        ArrayList<Expense> expenses = findByUser(user);
        if (expenses==null) {
            log.log(Level.FINER, "No expenses found");
            return;
        }
        for (Expense e: expenses) expenseRepository.delete(e);
        log.log(Level.FINEST, "Expenses deleted");
    }

}
