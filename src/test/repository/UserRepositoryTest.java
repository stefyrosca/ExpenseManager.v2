package test.repository;

import data.exceptions.MyException;
import junit.framework.TestCase;
import ro.ubbcluj.cs.Repository.UserRepository;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.factory.UserFactory;
import ro.ubbcluj.cs.utils.repository.UserRepositoryUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ZUZU on 26.10.2015.
 */
public class UserRepositoryTest extends TestCase {

    User user, invalidUser;
    ArrayList<User> users;
    UserFactory factory = new UserFactory();
    UserRepository repository;
    UserRepositoryUtils repositoryUtils;

    @Override
    public void setUp() throws Exception{
        super.setUp();
        repository = new UserRepository();
        user = factory.createUser();
        invalidUser = factory.createInvalidUser();
        users = factory.createUserList();
        repositoryUtils = new UserRepositoryUtils(repository);
    }

    private void saveUsers() throws MyException{
        for (User user: users) {
            try {
                repository.save(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void testSave() throws Exception {
        assertEquals(repository.size(),0);
        repository.save(user);
        assertEquals(user.getUsername(),UserFactory.USERNAME);
        assertEquals(user.getPassword(),UserFactory.PASSWORD);
        assertEquals(repository.size(),1);
    }

    public void testSaveInvalid(){
        assertEquals(repository.size(),0);
        try {
            repository.save(invalidUser);
        }
        catch (MyException e) {
            assertEquals(repository.size(),0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testFindByUsername() throws Exception {
        saveUsers();
        assertNotNull(repositoryUtils.findByUsername("user4"));
    }

    public void testDelete() throws Exception {
        saveUsers();
        assertEquals(repository.size(),users.size());
        repository.delete(users.get(2));
        assertEquals(repository.size(),users.size()-1);
    }

    public void testDeleteInexistentUser() throws MyException{
        assertEquals(repository.size(),0);
        try {
            repository.delete(user);
            throw new MyException("Test delete failed");
        }
        catch (MyException e) {
            try {
                assertNull(repositoryUtils.findByUsername(user.getUsername()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void testFindByID() throws Exception {
        saveUsers();
        assertNotNull(repository.findByID(users.get(2).getUserID()));
    }

    public void testFindAll() throws Exception {
        saveUsers();
        assertEquals(repository.findAll(),users);
    }

    public void testSortByUsername() throws MyException{
        saveUsers();
        ArrayList<User> sortedUsers = repositoryUtils.sortByUsername();
        assertEquals(sortedUsers.get(0).getUsername(),"abc");
    }
}