package ro.ubbcluj.cs.Repository;

import data.domain.Validator;
import data.repository.AbstractRepository;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.domain.UserValidator;
import ro.ubbcluj.cs.exceptions.RepositoryException;
import ro.ubbcluj.cs.utils.repository.UserRepositoryUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;


public class UserRepository extends AbstractRepository<User> {

    public UserRepository() {
        setValidator(new UserValidator());
        entities = new ArrayList<>();
        lastID = size();
        try {
            repositoryUtils = new UserRepositoryUtils(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void setValidator(Validator v) {
        this.validator = v;
    }

    @Override
    public int size() {
        log.log(Level.FINER, "Returning number of users");
        return entities.size();
    }






}
