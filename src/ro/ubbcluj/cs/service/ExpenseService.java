package ro.ubbcluj.cs.service;

import data.exceptions.MyException;
import data.repository.CRUDRepository;
import data.utils.MyLogger;
import ro.ubbcluj.cs.domain.Date;
import ro.ubbcluj.cs.domain.Expense;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.exceptions.RepositoryException;
import ro.ubbcluj.cs.utils.repository.ExpenseRepositoryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpenseService {
    private CRUDRepository<Expense> expenseRepository;
    private ExpenseRepositoryUtils expenseRepositoryUtils;
    private final Logger log = new MyLogger(this).getLog();
    private Map<Integer, Expense> expenses = new HashMap<>();

    public void setExpenseRepository(CRUDRepository<Expense> expenseRepository) throws Exception {
        this.expenseRepository = expenseRepository;
        expenseRepositoryUtils = new ExpenseRepositoryUtils(expenseRepository);
    }

    public void addExpense(Date date, double price, String stype, User user, String title) throws Exception {
        log.log(Level.FINEST, "Adding expense: {0}, {1}, {2}, {3}, {4}", new Object[]{date, price, stype, user, title});
        Expense.Type type;
        try {
            type = Expense.Type.valueOf(stype);
        }
        catch (IllegalArgumentException e) {
            type = Expense.Type.Others;
            log.log(Level.INFO, "Illegal argument for type, using Others");
        }
        Expense expense = new Expense(date, price, type, user, title);
        for (Expense e: expenses.values()){
            if (e==expense) {
                log.log(Level.FINER, "Expense already exists");
                throw new RepositoryException("Expense already exists");
            }
        }
        expenseRepository.save(expense);
        expenses.put(expense.getExpenseID(), expense);
        log.log(Level.FINEST, "Expense added");
    }

    public Iterable<Expense> getAllExpensesByUser(User user) {
        log.log(Level.FINEST, "Getting expenses for user {0}", user);
        ArrayList<Expense> expensesByUser = expenseRepositoryUtils.findByUser(user);
        for (Expense e: expensesByUser) {
            if (!expenses.containsKey(e.getExpenseID())) {
                expenses.put(e.getExpenseID(),e);
            }
        }
        return expensesByUser;
    }

    public void deleteExpense(User user, String title) throws MyException {
        log.log(Level.FINEST, "Deleting expense with title {0} from user {1}", new Object[] {title, user});
        Expense e = null;
        for (Expense ex: expenses.values()) {
            if (ex.getUser().equals(user) && ex.getTitle().equals(title)) {
                log.log(Level.FINER, "Expense found");
                expenses.remove(ex.getExpenseID());
                e = ex;
                break;
            }
        }
        if (e == null)
            e = expenseRepositoryUtils.findByUserAndTitle(user,title);
        expenseRepository.delete(e);
        log.log(Level.FINEST, "Expense deleted");
    }

    public void close() throws Exception {
        expenseRepository.close();
    }

    public void deleteExpensesForUser(User user) throws MyException {
        expenseRepositoryUtils.deleteExpensesForUser(user);
    }

}
