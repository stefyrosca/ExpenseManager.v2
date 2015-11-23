package ro.ubbcluj.cs.utils;

import ro.ubbcluj.cs.domain.Date;

/**
 * Created by ZUZU on 19.10.2015.
 */
public class DateUtils {
    public static boolean isValid(Date date) {
        return date.getDay()>0 && date.getDay()<32 && date.getMonth()>0 && date.getMonth()<13 && date.getYear()>1900 && date.getYear()<2016;
    }
}
