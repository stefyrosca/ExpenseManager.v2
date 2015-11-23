package ro.ubbcluj.cs.domain;

import java.io.Serializable;

/**
 * Created by ZUZU on 19.10.2015.
 */
public class Expense implements Serializable {


    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public enum Type {
        Food, Clothes, Housekeeping, Others
    }



    private String title;
    private Date date;
    private double price;
    private Type type;
    private User user;
    private int expenseID = 0;

    public Expense(Date date, double price, Type type, User user, String title) {
        this.date = date;
        this.price = price;
        this.type = type;
        this.user = user;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expense expense = (Expense) o;

        return expenseID == expense.getExpenseID() || (title.equals( expense.getTitle()) && user.equals( expense.getUser()));

    }

    @Override
    public int hashCode() {
        return getExpenseID();
    }

    public User getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getExpenseID() {
        return expenseID;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "title=" + title +
                ", date=" + date +
                ", price=" + price +
                ", type=" + type +
                ", user=" + user +
                ", expenseID=" + expenseID +
                '}';
    }
}

