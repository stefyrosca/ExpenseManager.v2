package ro.ubbcluj.cs.Repository;

import data.domain.Validator;
import data.exceptions.MyException;
import data.repository.AbstractFileRepository;
import ro.ubbcluj.cs.domain.User;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by ZUZU on 13.11.2015.
 */
public class UserFileRepository extends AbstractFileRepository<User> {


    public UserFileRepository(String filename) throws IOException, ClassNotFoundException {
        this.filename = filename;
        repository = new UserRepository();
      //  fileOut = new FileOutputStream(filename, false);
     //   buffer = new BufferedOutputStream(fileOut);
     //   out = new ObjectOutputStream(buffer);
        ArrayList<User> users = (ArrayList<User>) this.findAllFromFile();
        if (users == null) {
            repository.setEntities(new ArrayList<User>());
        }
        else {
            repository.setEntities(users);
        }
    }
//
//    @Override
//    public void save(User u) throws IOException, MyException, ClassNotFoundException {
//        super.save(u);
//        userRepository.save(u);
//    }
}
//
//    @Override
//    public Iterable<User> findAll() throws IOException, ClassNotFoundException {
//        if (userRepository.size()==0) {
//            return super.findAll();
//        }
//        return userRepository.findAll();
//    }
//
//    @Override
//    public int size() { return userRepository.size(); }
//
//    @Override
//    public User findByID(int ID) {
//        return userRepository.findByID(ID);
//    }
//
//    @Override
//    public void delete(User user) throws MyException {
//        userRepository.delete(user);
//        saveAll();
//    }
//
//    public void saveAll() {
//        FileOutputStream fileOut = null;
//        ArrayList<User> users = (ArrayList < User >)userRepository.findAll();
//        try {
//            fileOut = new FileOutputStream(filename, false);
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            for (User user: users) {
//                out.writeObject(user);
//            }
//            out.close();
//            fileOut.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
