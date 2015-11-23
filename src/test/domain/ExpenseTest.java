package domain;

import ro.ubbcluj.cs.domain.Expense;
import ro.ubbcluj.cs.factory.ExpenseFactory;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by ZUZU on 19.10.2015.
 */
public class ExpenseTest extends TestCase {

    Expense expense, invalidExpense;
    ExpenseFactory factory = new ExpenseFactory();
    ArrayList<Expense> expenses;

    public void setUp() throws Exception {
        super.setUp();
        expense = factory.createExpense();
        invalidExpense = factory.createInvalidExpense();
        expenses = factory.createExpenseArrayList();
    }

    public void testSetExpenseID() throws Exception {
        expense.setExpenseID(5);
        assertEquals(expense.getExpenseID(), 5);

    }

    public void testGetType() throws Exception {
        assertTrue(expense.getType()==factory.TYPE);
    }

    public void testGetDay() throws Exception {
        assertTrue(expense.getDate()==factory.DATE);
    }
}