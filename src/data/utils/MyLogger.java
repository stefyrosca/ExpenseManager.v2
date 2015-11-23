package data.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;

/**
 * Created by ZUZU on 12.11.2015.
 */
public class MyLogger {
    private String filename;
    protected Logger log;
    public static ArrayList<MyLogger> loggers = new ArrayList<>();

    public MyLogger(Object o) {
        log = Logger.getLogger(o.getClass().getName());
        this.filename = "./logs/" + o.getClass().getSimpleName() + ".log";
        setLevels();
        loggers.add(this);
    }

    public java.util.logging.Logger getLog() {
        return log;
    }

    private void setLevels() {
        try {
            log.setLevel(Level.FINER);
            FileHandler fh = null;
            ConsoleHandler ch = null;
            for (Handler handler: Logger.getLogger("").getHandlers()) {
                if (handler instanceof ConsoleHandler) {
                    ch = (ConsoleHandler)handler;
                }
                if (handler instanceof FileHandler) {
                    fh = (FileHandler)handler;
                }
            }
            if (ch == null) {
                ch = new ConsoleHandler();
                log.addHandler(ch);
            }
            if (fh == null) {
                fh = new FileHandler(filename, true);
                log.addHandler(fh);
            }
            ch.setLevel(Level.INFO);
            fh.setLevel(Level.FINER);
            fh.setFormatter(new SimpleFormatter());
        }
        catch (IOException ioe) {
            log.warning(ioe.getMessage());
        }
    }

}
