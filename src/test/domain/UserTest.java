package domain;

import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.factory.UserFactory;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by ZUZU on 19.10.2015.
 */
public class UserTest extends TestCase {

    User user, invalidUser;
    ArrayList<User> users;
    UserFactory factory = new UserFactory();

    public void setUp() throws Exception {
        super.setUp();
        user = factory.createUser();
        users = factory.createUserList();
        invalidUser = factory.createInvalidUser();
    }

    public void testGetUsername() throws Exception {
        assertEquals(user.getUsername(),factory.USERNAME);
    }

    public void testSetUsername() throws Exception {
        user.setUsername("abc");
        assertEquals(user.getUsername(),"abc");
    }

    public void testGetPassword() throws Exception {
        assertEquals(user.getPassword(),factory.PASSWORD);

    }

    public void testSetPassword() throws Exception {
        user.setPassword("pass");
        assertEquals("pass",user.getPassword());
    }
}