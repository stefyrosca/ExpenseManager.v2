package repository;

import data.exceptions.MyException;
import junit.framework.TestCase;
import ro.ubbcluj.cs.domain.Expense;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.factory.ExpenseFactory;
import ro.ubbcluj.cs.Repository.ExpenseRepository;
import ro.ubbcluj.cs.utils.repository.ExpenseRepositoryUtils;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by ZUZU on 19.10.2015.
 */
public class ExpenseRepositoryTest extends TestCase {// AbstractRepositoryTest<Expense> {

    ExpenseFactory factory = new ExpenseFactory();
    Expense expense, invalidExpense;
    ExpenseRepository repository;
    ArrayList<Expense> entities;
    ExpenseRepositoryUtils repositoryUtils;

    @Override
    public void setUp() throws Exception{
        super.setUp();
        repository = new ExpenseRepository();
        expense = createEntity();
        invalidExpense = createInvalidEntity();
        entities = createEntities();
        repositoryUtils = new ExpenseRepositoryUtils(repository);
    }

    public void testSave() throws Exception {
        repository.save(expense);
        assertEquals(repository.size(), 1);
    }

    public void testInvalidSave() throws Exception {
        try {
            repository.save(invalidExpense);
        }
        catch (MyException e) {
            assertEquals(repository.size(), 0);
        }
    }

    public void testSavingEqualExpenses() throws Exception{
        repository.save(expense);
        assertEquals(repository.size(),1);
        try {
            expense.setExpenseID(expense.getExpenseID()+1);
            repository.save(expense);
        }
        catch (MyException e) {
            assertEquals(repository.size(),1);
        }
    }

    public void testFindByID() throws Exception {
        repository.save(expense);
        assertEquals(repositoryUtils.getEntityID(expense),1);
    }

    public void testFindAll() throws Exception {
        saveEntities();
        assertEquals(repository.size(),entities.size());
    }

    public void testSize() throws Exception {
        assertEquals(repository.size(), 0);
        repository.save(expense);
        assertEquals(repository.size(),1);
    }

    protected void saveEntities() {
        for (Expense e: entities) {
            try {
                repository.save(e);
            }
            catch (MyException ex) {
                System.out.println(ex.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void testCheckExistence() throws Exception {
        saveEntities();
        try {
            assertTrue(repositoryUtils.checkExistence(entities.get(3)));
        }
        catch (MyException e) {

        }
    }

    public void testDeleteExpense() throws MyException{
        saveEntities();
        assertEquals(repository.size(),entities.size());
        repository.delete(entities.get(2));
        assertEquals(repository.size(),entities.size()-1);
    }

    public void testDeleteInexistentExpense() throws MyException{
        assertEquals(repository.size(),0);
        try {
            repository.delete(expense);
            throw new MyException("Delete test failed");
        }
        catch (MyException e) {

        }
    }

    public void testFindByUser() {
        saveEntities();
        User u = entities.get(1).getUser();
        ArrayList<Expense> expenses;
        expenses = repositoryUtils.findByUser(u);
        assertNotNull(expenses);
        expenses = repositoryUtils.findByUser(expense.getUser());
        assertNull(expenses);
    }

    public Expense createEntity() {
        return factory.createExpense();
    }

    public Expense createInvalidEntity() {
        return factory.createInvalidExpense();
    }

    public ArrayList<Expense> createEntities() {
        return factory.createExpenseArrayList();
    }
}