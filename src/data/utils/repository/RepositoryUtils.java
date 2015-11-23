package data.utils.repository;

import data.domain.Validator;
import ro.ubbcluj.cs.exceptions.RepositoryException;

import java.io.IOException;

/**
 * Created by ZUZU on 13.11.2015.
 */
public interface RepositoryUtils<E> {
    boolean checkExistence(E e) throws Exception;
    void setEntityID(E entity, int ID);
    int getEntityID(E entity);
}
