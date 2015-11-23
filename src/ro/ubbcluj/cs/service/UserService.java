package ro.ubbcluj.cs.service;

import data.repository.CRUDRepository;
import data.utils.MyLogger;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.exceptions.AuthenticationException;
import ro.ubbcluj.cs.exceptions.RepositoryException;
import ro.ubbcluj.cs.utils.repository.UserRepositoryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ZUZU on 20.11.2015.
 */
public class UserService {
    private CRUDRepository<User> userRepository;
    private UserRepositoryUtils userRepositoryUtils;
    private final Logger log = new MyLogger(this).getLog();
    private Map<String, User> users = new HashMap<>();

    public void setUserRepository(CRUDRepository<User> userRepository) throws Exception {
        this.userRepository = userRepository;
        userRepositoryUtils = new UserRepositoryUtils(userRepository);
    }

    public User authenticate(String username, String password) throws AuthenticationException, Exception {
        log.log(Level.FINEST, "Authenticating user {0}", username);
        User user;
        if (users.containsKey(username)) {
            user = users.get(username);
        }
        else
            user = userRepositoryUtils.findByUsername(username);
        if (user == null) {
            log.log(Level.FINE, "Authentication failed");
            throw new AuthenticationException("User doesn't exist");
        }
        if (user.getPassword().equals(password)) {
            log.log(Level.FINEST, "Authentication succeeded");
            users.put(username, user);
            return user;
        }
        log.log(Level.FINE, "Authentication failed");
        throw new AuthenticationException("User and password don't match");
    }

    public void addUser(String username, String password) throws Exception {
        log.log(Level.FINEST, "Adding user with username {0}, password {1}", new Object[] {username, password});
        User user = new User(username, password);
        if (users.containsKey(username)) {
            log.log(Level.FINE, "User " + username + "already exists");
            throw new RepositoryException("User already exists");
        }
        userRepository.save(user);
        users.put(username, user);
        log.log(Level.FINEST, "User saved");
    }

    public Iterable<User> getUsers() throws Exception {
        log.log(Level.FINEST, "Getting all users");
        ArrayList<User> allUsers = (ArrayList<User>) userRepository.findAll();
        for (User user: allUsers) {
            if (!users.containsKey(user.getUsername())) {
                users.put(user.getUsername(), user);
            }
        }
        return allUsers;
    }

    public void deleteUser(String username) throws Exception {
        log.log(Level.FINEST, "Deleting user {0}", username);
        User user;
        if (users.containsKey(username)) {
            user = users.get(username);
        }
        else
            user = userRepositoryUtils.findByUsername(username);
        userRepository.delete(user);
        if (users.containsKey(username)) {
            users.remove(username);
        }
        log.log(Level.FINEST, "User deleted");
    }

    public void close() throws Exception {
        userRepository.close();
    }

}
