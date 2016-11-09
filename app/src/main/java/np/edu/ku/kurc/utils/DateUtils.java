package np.edu.ku.kurc.utils;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    /**
     * Parses date into string.
     *
     * @param value Date Object to format into string.
     * @param sdf   Simple date format into which date is to be formatted.
     * @return      String format of date.
     */
    public static String toString(Date value, SimpleDateFormat sdf) {
        return sdf.format(value);
    }

    /**
     * Parses date into string.
     *
     * @param value Date Object to format into string.
     * @return      String format of date.
     */
    public static String toString(Date value) {
        return toString(value,sdf);
    }

    /**
     * Parses date from string.
     *
     * @param dateString    Date string to be parsed.
     * @param sdf           Simple date format from which the date is to be parsed.
     * @return              Date object from string.
     */
    @Nullable
    public static synchronized Date fromString(String dateString, SimpleDateFormat sdf) {
        Date date = null;

        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * Parses date from string.
     *
     * @param dateString    Date string to be parsed.
     * @return              Date object from string.
     */
    @Nullable
    public static synchronized Date fromString(String dateString) {
        return fromString(dateString,sdf);
    }
}
