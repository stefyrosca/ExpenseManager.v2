package ro.ubbcluj.cs.domain;

import java.io.Serializable;

/**
 * Created by ZUZU on 19.10.2015.
 */
public class Date implements Serializable {
    int day;
    int month;
    int year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Date date = (Date) o;

        if (getDay() != date.getDay()) return false;
        if (getMonth() != date.getMonth()) return false;
        return getYear() == date.getYear();

    }

    @Override
    public String toString() {
        return "day=" + day +
                ", month=" + month +
                ", year=" + year + ' ';
    }

    @Override
    public int hashCode() {
        int result = getDay();
        result = 31 * result + getMonth();
        result = 31 * result + getYear();
        return result;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date(int d, int m, int y) {

        day = d;
        month = m;
        year = y;
    }
}
