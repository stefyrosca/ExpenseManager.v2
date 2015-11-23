package ro.ubbcluj.cs.factory;

import ro.ubbcluj.cs.domain.User;

import java.util.ArrayList;

/**
 * Created by ZUZU on 19.10.2015.
 */
public class UserFactory {
    public static final String USERNAME = "poweruser";
    public static final String PASSWORD = "userpass";

    public User createUser() {
        User u = new User(USERNAME, PASSWORD);
        return u;
    }

    public User createInvalidUser() {
        User u = new User("","");
        return u;
    }

    public ArrayList<User> createUserList() {
        ArrayList<User> users = new ArrayList<>();
        User u = new User("ww2","abc12");
        u.setUserID(1);
        users.add(u);
        u = new User("abc","abc12");
        u.setUserID(2);
        users.add(u);
        u = new User("user4","abc12");
        u.setUserID(3);
        users.add(u);
        u = new User("mmn","abc12");
        u.setUserID(4);
        users.add(u);
        return users;
    }
}
