package ro.ubbcluj.cs.UI;

import data.exceptions.MyException;
import data.repository.CRUDRepository;
import data.utils.MyLogger;
import ro.ubbcluj.cs.Repository.*;
import ro.ubbcluj.cs.domain.Date;
import ro.ubbcluj.cs.domain.Expense;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.exceptions.AuthenticationException;
import ro.ubbcluj.cs.service.ExpenseService;
import ro.ubbcluj.cs.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Created by ZUZU on 04.11.2015.
 */

public class ConsoleUI {
    ExpenseService expenseService;
    UserService userService;
    private User currentUser = null;
    private final Logger log = new MyLogger(this).getLog();

    public void setExpenseService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ConsoleUI() {
        try {
            setExpenseService(new ExpenseService());
            setUserService(new UserService());
            CRUDRepository userRepository = new UserXmlRepository("./src/resources/users.xml");
            CRUDRepository expenseRepository = new ExpenseXmlRepository("./src/resources/expenses.xml");
//            CRUDRepository userRepository = new UserFileRepository("./src/resources/users.dat");
//            CRUDRepository expenseRepository = new ExpenseFileRepository("./src/resources/expenses.dat");
//            CRUDRepository<User> userRepository = new UserRepository();
//            CRUDRepository<Expense> expenseRepository = new ExpenseRepository();

            expenseService.setExpenseRepository(expenseRepository);
            userService.setUserRepository(userRepository);
          //  expensesAppService.addUser("root", "rootpass");
        } catch (Exception e) {
              log.log(Level.WARNING, e.getMessage());
              e.printStackTrace();

            // } catch (MyException e) {
     //       e.printStackTrace();
        }
    }

    public void run() {
        if (login()) {
            while (true) {
                printMenu();
                Scanner reader = new Scanner(System.in);
                int command;
                command = reader.nextInt();
                switch (command) {
                    case 0:
                        log.log(Level.FINER,"User {0} has logged out", currentUser.getUsername());
                        currentUser = null;
                        shutdown();
                        System.out.println("Goodbye!");
                        return;
                    case 1:
                        addExpense();
                        break;
                    case 2:
                        deleteExpense();
                        break;
                    case 3:
                        printExpensesByUser();
                        break;
                    case 4:
                        addUser();
                        break;
                    case 5:
                        deleteUser();
                        break;
                    case 6:
                        printUsers();
                        break;
                    default:
                        log.log(Level.INFO,"Invalid command");
                        break;
                }
            }
        }
    }

    private void shutdown() {
        for (MyLogger myLogger : MyLogger.loggers) {
            for (Handler handler : myLogger.getLog().getHandlers()) {
                handler.flush();
                handler.close();
            }
        }
        try {
            expenseService.close();
            userService.close();
        } catch (Exception e) {
            log.log(Level.WARNING, e.getMessage());
        }
    }

    private User authenticate() throws AuthenticationException, Exception {
        Scanner reader = new Scanner(System.in);
        System.out.println("Username:");
        String username = reader.nextLine();
        System.out.println("Password:");
        String password = reader.nextLine();
        return userService.authenticate(username, password);
    }

    private void addExpense() {
        log.log(Level.FINEST, "Adding expense ..");
        String title, type;
        int day, month, year;
        double price;
        Date date;
        try {
            Scanner reader = new Scanner(System.in);
            System.out.println("Title:");
            title = reader.nextLine();
            System.out.println("Type: [Food, Clothes, Housekeeping, Others]");
            type = reader.nextLine();
            System.out.println("Date:(dd mm yyyy)");
            day = reader.nextInt();
            month = reader.nextInt();
            year = reader.nextInt();
            System.out.println("Price:");
            price = reader.nextDouble();
            date = new Date(day, month, year);
            expenseService.addExpense(date, price, type, currentUser, title);
            log.log(Level.FINEST, "Expense added");
        }
        catch (MyException e) {
            log.log(Level.INFO, e.getMessage());
        }
        catch (IllegalArgumentException e) {
            log.log(Level.WARNING, "Illegal argument");
        }
        catch (InputMismatchException e) {
            log.log(Level.WARNING, "Wrong input");
        }
        catch (NullPointerException e) {
            log.log(Level.WARNING, "Null pointer exception occured");
            e.printStackTrace();
        }
        catch (Exception e) {
            log.log(Level.WARNING, e.getMessage());
        }

    }

    private void deleteExpense() {
        log.log(Level.FINEST, "Deleting expense ..");
        Scanner reader = new Scanner(System.in);
        System.out.println("Title:");
        String title = reader.nextLine();
        try {
             expenseService.deleteExpense(currentUser, title);
             log.log(Level.FINEST, "Expense deleted");
             System.out.println("Expense deleted");
        }
        catch (MyException e) {
            log.log(Level.INFO, e.getMessage());
        }
    }

    private void printExpensesByUser() {
        log.log(Level.FINEST, "Printing expenses ..");
        Iterable<Expense> expenses = expenseService.getAllExpensesByUser(currentUser);
        if (expenses==null) {
            System.out.println("It looks like you don't have any expenses..");
            log.log(Level.FINE, "No expenses found for current user");
            return;
        }
        System.out.println("Your expenses:");
        Iterator<Expense> iterator = expenses.iterator();
        while (iterator.hasNext())
            System.out.println(iterator.next());
        }

    private void addUser() {
        log.log(Level.FINEST, "Adding user ..");
        if (checkPermission()) {
            Scanner reader = new Scanner(System.in);
            System.out.println("Username:");
            String username = reader.nextLine();
            System.out.println("Password:");
            String password = reader.nextLine();
            try {
                userService.addUser(username, password);
                System.out.println("User added");
                log.log(Level.FINEST, "User added");
            }
            catch (MyException e) {
                System.out.println(e.getMessage());
                log.log(Level.FINE, e.getMessage());
            } catch (Exception e) {
                log.log(Level.WARNING, e.getMessage());
            }
        }
        else {
            log.log(Level.INFO, "Permission denied for user: username: {0}",currentUser.getUsername());
        }
    }

    private void printUsers() {
        log.log(Level.FINEST, "Printing users ..");
        if (checkPermission()) {
            ArrayList<User> users = null;
            try {
                users = (ArrayList<User>) userService.getUsers();
                if (users.size() == 0) {
                    System.out.println("There are no users");
                    log.log(Level.FINER, "No users found in memory");
                }
                Iterator<User> iterator = users.iterator();
                while (iterator.hasNext()) {
                    System.out.println(iterator.next());
                }
            } catch (Exception e) {
                log.log(Level.WARNING, e.getMessage());
            }
        }
        else {
            log.log(Level.INFO, "Permission denied for user: username: {0}",currentUser.getUsername());
        }
    }

    private void deleteUser() {
        log.log(Level.FINEST, "Deleting user ..");
        if (checkPermission()) {
            Scanner reader = new Scanner(System.in);
            System.out.println("Username:");
            String username = reader.nextLine();
            if (canBeDeleted(username)) {
                try {
                    userService.deleteUser(username);
                    expenseService.deleteExpensesForUser(currentUser);
                    System.out.println("User deleted");
                    log.log(Level.FINEST, "User deleted");
                } catch (MyException e) {
                    System.out.println(e.getMessage());
                    log.log(Level.FINE,e.getMessage());
                } catch (Exception e) {
                    log.log(Level.WARNING,e.getMessage());
                }
            }
            else {
                log.log(Level.INFO, "Attempt to delete the root user");
            }
        }
        else {
            log.log(Level.INFO, "Permission denied for user: username: {0}",currentUser.getUsername());
        }
    }

    private boolean login() {
        log.log(Level.FINEST, "Login ..");
        try {
           User user = authenticate();
           if (user != null) {
               currentUser = user;
               System.out.println("Hello " + currentUser.getUsername() + "!");
               log.log(Level.FINEST, "Login succeeded, user: {0}", user);
               return true;
           }
       }
       catch (AuthenticationException e) {
           log.log(Level.INFO, e.getMessage());
       } catch (ClassNotFoundException e) {
            log.log(Level.WARNING, e.getMessage());
        } catch (Exception e) {
            log.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }
        log.log(Level.FINER, "Login failed");
        return false;
    }

    private void printMenu() {
        System.out.println("\nPossible actions:");
        System.out.println("1. Add expense");
        System.out.println("2. Delete expense");
        System.out.println("3. Print expenses");
        if (currentUser.getUsername().equals("root")) {
            System.out.println("4. Add user");
            System.out.println("5. Delete user");
            System.out.println("6. Print users");
        }
        System.out.println("0. Logout");
        System.out.println("Command?");
    }

    private boolean checkPermission() {
        log.log(Level.FINEST, "Checking permission for user " + currentUser);
        if (currentUser.getUsername().equals("root")) {
            log.log(Level.FINER, "Permission granted");
            return true;
        }
        log.log(Level.FINER, "Permission denied");
        return false;
    }

    private boolean canBeDeleted(String username) { return !username.equals("root");}
}
