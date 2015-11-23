package data.repository;

import data.exceptions.MyException;
import data.utils.MyLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ZUZU on 13.11.2015.
 */
public abstract class AbstractFileRepository<E> implements CRUDRepository<E> {
    protected String filename;
    protected final Logger log = new MyLogger(this).getLog();
    protected AbstractRepository<E> repository;
 //   protected FileOutputStream fileOut;
 //   protected ObjectOutputStream out;
 //   protected OutputStream buffer;

    @Override
    public void save(E e) throws Exception {
        try {
            log.log(Level.FINEST, "Saving entity {0} in file {1}", new Object[]{e, filename});
            repository.save(e);
        //    out.writeObject(repository.findAll());
            log.log(Level.FINEST, "Entity saved in file " + filename);
        } catch (FileNotFoundException ex) {
            log.log(Level.WARNING, "File not found: " + filename);
            throw ex;
        } catch (IOException ex) {
            log.log(Level.WARNING, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void delete(E e) throws MyException {
        repository.delete(e);
     //   saveAll();
    }

    @Override
    public E findByID(int ID) {
        return repository.findByID(ID);
    }

    @Override
    public int size() {
        return repository.size();
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public Iterable<E> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Iterable<E> findAllFromFile() throws IOException, ClassNotFoundException {
        List<E> entities = null;
        try {
            log.log(Level.FINEST, "Getting entities from file "  + filename);
            FileInputStream fileIn;
            File f = new File(filename);
            boolean created = f.createNewFile();
            if (created==true) {
                log.log(Level.WARNING, "File " + filename + " doesn't exist, creating it now");
                FileOutputStream fileOut = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.close();
                fileOut.close();
            }
            else {
                fileIn = new FileInputStream(filename);
                InputStream buffer = new BufferedInputStream(fileIn);
                ObjectInput in = new ObjectInputStream (buffer);
              //  boolean read = true;
            //    while (read) {
            //        try {
             //           E object = (E) in.readObject();
             //           entities.add(object);
                try {
                    entities = (List<E>) in.readObject();
                }
                catch (EOFException e) {
                    //
                    log.log(Level.WARNING, e.getMessage());
                }
                in.close();
                fileIn.close();
            }
        } catch (IOException ex) {
            log.log(Level.WARNING, ex.getMessage());
            throw ex;
        } catch (ClassNotFoundException ex) {
            log.log(Level.WARNING, ex.getMessage());
            throw ex;
        }
        return entities;
    }

    public void saveAll() throws Exception {
        try {
        List<E> entities = (List< E >)repository.findAll();
        FileOutputStream fileOut = new FileOutputStream(filename, false);
        OutputStream buffer = new BufferedOutputStream(fileOut);
        ObjectOutputStream out = new ObjectOutputStream(buffer);
        out.writeObject(entities);
            out.close();
            buffer.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        saveAll();
        for (MyLogger myLogger : MyLogger.loggers) {
            for (Handler handler : myLogger.getLog().getHandlers()) {
                handler.flush();
                handler.close();
            }
        }
    }
}
