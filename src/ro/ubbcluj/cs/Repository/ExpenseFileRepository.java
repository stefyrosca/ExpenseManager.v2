package ro.ubbcluj.cs.Repository;

import data.repository.AbstractFileRepository;
import ro.ubbcluj.cs.domain.Expense;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by ZUZU on 04.11.2015.
 */
public class ExpenseFileRepository extends AbstractFileRepository<Expense> {

    public ExpenseFileRepository(String filename) throws IOException, ClassNotFoundException {
        this.filename = filename;
        //   fileOut = new FileOutputStream(filename, false);
        //   buffer = new BufferedOutputStream(fileOut);
        //   out = new ObjectOutputStream(buffer);
        repository = new ExpenseRepository();
        ArrayList<Expense> expenses = (ArrayList<Expense>) this.findAllFromFile();
        if (expenses == null) {
            repository.setEntities(new ArrayList<Expense>());
        } else {
            repository.setEntities(expenses);
        }
    }

//    @Override
//    public void save(Expense e) throws IOException, MyException, ClassNotFoundException {
//        super.save(e);
//        expenseRepository.save(e);
//    }




}
