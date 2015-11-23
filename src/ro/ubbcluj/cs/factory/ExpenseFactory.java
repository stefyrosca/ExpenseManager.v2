package ro.ubbcluj.cs.factory;

import ro.ubbcluj.cs.domain.Date;
import ro.ubbcluj.cs.domain.Expense;
import ro.ubbcluj.cs.domain.User;

import java.util.ArrayList;

/**
 * Created by ZUZU on 19.10.2015.
 */
public class ExpenseFactory {
    public static final Date DATE = new Date(12,5,2015);
    public static final double PRICE = 17.4;
    public static final Expense.Type TYPE = Expense.Type.Food;
    public static UserFactory factory = new UserFactory();

    public Expense createExpense() {

        User u = factory.createUser();
        u.setUserID(1);
        Expense e = new Expense(DATE, PRICE, TYPE, u, "t16");
        return e;
    }

    public Expense createInvalidExpense() {
        Expense e = new Expense(new Date(0,0,0), -15, TYPE, null, "");
        return e;
    }

    public ArrayList<Expense> createExpenseArrayList() {
        ArrayList<Expense> expenses = new ArrayList<>();
       // expenses.add(createExpense());
        ArrayList<User> users = factory.createUserList();
        expenses.add(new Expense(new Date(12,9,2014), 12.3, TYPE, users.get(0), "t1"));
        expenses.add(new Expense(new Date(11,11,2014), 19.3, TYPE, users.get(1), "t2"));
        expenses.add(new Expense(new Date(7,9,20154), 11.3, TYPE, users.get(2), "t3"));
        expenses.add(new Expense(new Date(29,3,2014), 1.3, TYPE, users.get(3), "t4"));
        return expenses;
    }
}
