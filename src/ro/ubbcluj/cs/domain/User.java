package ro.ubbcluj.cs.domain;

import java.io.Serializable;

/**
 * Created by ZUZU on 19.10.2015.
 */
public class User implements Comparable, Serializable {
    private String username;
    private String password;
    private int userID;

    public User() {
        username = "newuser";
        password = "1234";
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int id) {
        userID = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getUserID() == user.getUserID() || getUsername().equals(user.getUsername());

    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", userID=" + userID +
                '}';
    }

    @Override
    public int hashCode() {
        return getUserID();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int compareTo(Object o) {
        User u = (User)o;
        if (this.getUsername().compareTo(u.getUsername())==-1) {
            return -1;
        }
        else if (this.getUsername()==u.getUsername())
            return 0;
        return 1;
    }
}