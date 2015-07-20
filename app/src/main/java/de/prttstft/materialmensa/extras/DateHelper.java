package de.prttstft.materialmensa.extras;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateHelper {

    public String getDate() {
        String date;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        date = df.format(c.getTime());

        return date;
    }

    public String getDate(int n) {
        String date;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        c.add(Calendar.DAY_OF_YEAR, n);
        date = df.format(c.getTime());
        return date;
    }

    public String getDay() {
        String day;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEEE");
        day = df.format(c.getTime());

        return day;
    }

    public String getDay(int n) {
        String day;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEEE");
        c.add(Calendar.DAY_OF_YEAR, n);
        day = df.format(c.getTime());

        return day;
    }
}
