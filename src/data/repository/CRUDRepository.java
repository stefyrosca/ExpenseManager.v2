package data.repository;

import data.exceptions.MyException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by ZUZU on 19.10.2015.
 */
public interface CRUDRepository<E> extends Repository<E> {
    void save(E e) throws Exception;
    Iterable<E> findAll() throws Exception;
    int size();
    E findByID(int ID);
    void delete(E e) throws MyException;
    Logger getLog();
    void close() throws Exception;
}
