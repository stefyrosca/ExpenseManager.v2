package ro.ubbcluj.cs.utils.repository;

import data.repository.CRUDRepository;
import data.utils.repository.RepositoryUtils;
import ro.ubbcluj.cs.Repository.UserRepository;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.exceptions.RepositoryException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ZUZU on 13.11.2015.
 */
public class UserRepositoryUtils implements RepositoryUtils<User> {
    CRUDRepository<User> userRepository;
    Logger log;
    ArrayList<User> entities;

    public UserRepositoryUtils(CRUDRepository<User> userRepository) throws Exception {
        this.userRepository = userRepository;
        log = userRepository.getLog();
        entities  = (ArrayList<User>)userRepository.findAll();
    }

    @Override
    public boolean checkExistence(User t) throws  Exception {
        log.log(Level.FINER, "Checking existence in memory for user {0}", t);
        if (findByUsername(t.getUsername())!=null || userRepository.findByID(t.getUserID())!=null) {
            log.log(Level.FINE, "User already exists");
            throw new RepositoryException("A user with that username already exists");
        }
        log.log(Level.FINER, "User doesn't exist");
        return false;
    }

    @Override
    public void setEntityID(User entity, int ID) {
        entity.setUserID(ID);
    }

    @Override
    public int getEntityID(User entity) {
        return entity.getUserID();
    }

    public ArrayList<User> sortByUsername() {
        log.log(Level.FINER, "Sorting users by username");
        entities.sort((u1, u2) -> u1.getUsername().compareTo(u2.getUsername()));
        log.log(Level.FINER, "Users sorted");
        return entities;
    }


    public User findByUsername(String username) throws Exception {
        log.log(Level.FINER,"Searching for user " + username);
        Iterable<User> users = userRepository.findAll();
        if (users == null)
            return null;
        for (User u: users) {
            if (u.getUsername().equals(username)){
                log.log(Level.FINE,"User found");
                return u;
            }
        }
        log.log(Level.FINER, "User not found");
        return null;
    }
}
