package data.repository;

import data.domain.Validator;
import data.exceptions.MyException;
import data.utils.MyLogger;
import data.utils.repository.RepositoryUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ZUZU on 19.10.2015.
 */
public abstract class AbstractRepository<E> implements CRUDRepository<E>{

    protected Validator<E> validator;
    protected int lastID;
    protected ArrayList<E> entities;
    protected final Logger log = new MyLogger(this).getLog();
    protected RepositoryUtils repositoryUtils;


    @Override
    public void save (E t) throws Exception{
        log.log(Level.FINEST, "Saving entity {0}", t);
        try {
            if (validator.validate(t) && !repositoryUtils.checkExistence(t)) {
                lastID++;
                repositoryUtils.setEntityID(t, lastID);
                entities.add(t);
            }
        }
        catch (MyException e) {
            log.log(Level.FINE, "Saving failed: " + e.getMessage());
            throw new MyException(e.getMessage());
        } catch (ClassNotFoundException e) {
            log.log(Level.WARNING, "Saving failed: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(E t) throws  MyException {
        log.log(Level.FINEST, "Deleting entity {0}", t);
        if (t==null || findByID(repositoryUtils.getEntityID(t))==null) {
            log.log(Level.FINE, "Entity doesn't exist");
            throw new MyException("Entity doesn't exist");
        }
        entities.remove(t);
    }

    @Override
    public E findByID(int id) {
        log.log(Level.FINEST, "Searching for entity with id {0}", id);
        if (entities == null) {
            return null;
        }
        for (E entity: entities) {
            if (repositoryUtils.getEntityID(entity) == id) {
                log.log(Level.FINEST, "Entity found: {0}", entity);
                return entity;
            }
        }
        log.log(Level.FINE, "Entity not found");
        return null;
    }

    @Override
    public Iterable<E> findAll() throws Exception {
        log.log(Level.FINEST, "Returning entities");
        return entities;
    }

    @Override
    public int size() {
        log.log(Level.FINEST, "Returning numbers of entities: {0}", entities.size());
        return entities.size();
    }

    @Override
    public Logger getLog() {
        return log;
    }

    public void setEntities(ArrayList<E> entities) {
        this.entities = entities;
    }

    @Override
    public void close() throws Exception {
        for (MyLogger myLogger : MyLogger.loggers) {
            for (Handler handler : myLogger.getLog().getHandlers()) {
                handler.flush();
                handler.close();
            }
        }
    }
}
