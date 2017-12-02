package com.shit.beerburn.utils;

// import org.joda.time.DateTime;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Created by ahmeshaf on 2/7/2016.
 */
public class DateTimeHelper {
    public static String getLocalFormattedDate(String isoLastDate) {
        String LOCAL_FORMAT = "MMM dd yyyy  HH:mm a";
        DateTime myDateTime = DateTime.parse(isoLastDate, ISODateTimeFormat.dateTimeNoMillis()).withZone(DateTimeZone.UTC);
        DateTimeFormatter dtf = DateTimeFormat.forPattern(LOCAL_FORMAT);
        return dtf.print(myDateTime);
    }


    public static Long getEpochSecondsOf(String isoDate) {
        DateTime myDateTime = DateTime.parse(isoDate, ISODateTimeFormat.dateTimeNoMillis()).withZone(DateTimeZone.UTC);
        return myDateTime.getMillis()/1000;
    }

    public static boolean isDayOfDateToday(String isoDate) {
        LocalDateTime localDate = new LocalDateTime();
        DateTime lastActivtyDate = DateTime.parse(isoDate, ISODateTimeFormat.dateTimeNoMillis()).withZone(DateTimeZone.UTC);
        int dayOfYearLocal = localDate.getDayOfYear();
        int dayofYearActivity = lastActivtyDate.getDayOfYear();
        if (dayOfYearLocal == dayofYearActivity) {
            return true;
        }
        else {
            return false;
        }
    }
}
