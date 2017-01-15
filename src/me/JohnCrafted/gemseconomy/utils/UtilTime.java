package me.JohnCrafted.gemseconomy.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by John on 26.03.2016.
 */
public class UtilTime {

    public static final String DATE_FORMAT_NOW = "MM-dd-yyyy HH:mm:ss";
    public static final String DATE_FORMAT_DAY = "MM-dd-yyyy";

    public static String now() {
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public static String when(final long time) {
        final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return sdf.format(time);
    }

    public static String date() {
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        return sdf.format(cal.getTime());
    }

    public static String since(final long epoch) {
        return "Took " + convertString(System.currentTimeMillis() - epoch, 1, TimeUnit.FIT) + ".";
    }

    public static double convert(final long time, final int trim, TimeUnit type) {
        if (type == TimeUnit.FIT) {
            if (time < 60000L) {
                type = TimeUnit.SECONDS;
            }
            else if (time < 3600000L) {
                type = TimeUnit.MINUTES;
            }
            else if (time < 86400000L) {
                type = TimeUnit.HOURS;
            }
            else {
                type = TimeUnit.DAYS;
            }
        }
        if (type == TimeUnit.DAYS) {
            return UtilMath.trim(trim, time / 8.64E7);
        }
        if (type == TimeUnit.HOURS) {
            return UtilMath.trim(trim, time / 3600000.0);
        }
        if (type == TimeUnit.MINUTES) {
            return UtilMath.trim(trim, time / 60000.0);
        }
        if (type == TimeUnit.SECONDS) {
            return UtilMath.trim(trim, time / 1000.0);
        }
        return UtilMath.trim(trim, time);
    }

    public static String MakeStr(final long time) {
        return convertString(time, 1, TimeUnit.FIT);
    }

    public static String MakeStr(final long time, final int trim) {
        return convertString(Math.max(0L, time), trim, TimeUnit.FIT);
    }

    public static String convertString(final long time, final int trim, TimeUnit type) {
        if (time == -1L) {
            return "Permanent";
        }
        if (type == TimeUnit.FIT) {
            if (time < 60000L) {
                type = TimeUnit.SECONDS;
            }
            else if (time < 3600000L) {
                type = TimeUnit.MINUTES;
            }
            else if (time < 86400000L) {
                type = TimeUnit.HOURS;
            }
            else {
                type = TimeUnit.DAYS;
            }
        }
        double num;
        String text;
        if (trim == 0) {
            if (type == TimeUnit.DAYS) {
                text = (num = UtilMath.trim(trim, time / 8.64E7)) + " Day";
            }
            else if (type == TimeUnit.HOURS) {
                text = (num = UtilMath.trim(trim, time / 3600000.0)) + " Hour";
            }
            else if (type == TimeUnit.MINUTES) {
                text = (num = UtilMath.trim(trim, time / 60000.0)) + " Minute";
            }
            else if (type == TimeUnit.SECONDS) {
                text = (int)(num = (int)UtilMath.trim(trim, time / 1000.0)) + " Second";
            }
            else {
                text = (int)(num = (int)UtilMath.trim(trim, time)) + " Millisecond";
            }
        }
        else if (type == TimeUnit.DAYS) {
            text = (num = UtilMath.trim(trim, time / 8.64E7)) + " Day";
        }
        else if (type == TimeUnit.HOURS) {
            text = (num = UtilMath.trim(trim, time / 3600000.0)) + " Hour";
        }
        else if (type == TimeUnit.MINUTES) {
            text = (num = UtilMath.trim(trim, time / 60000.0)) + " Minute";
        }
        else if (type == TimeUnit.SECONDS) {
            text = (num = UtilMath.trim(trim, time / 1000.0)) + " Second";
        }
        else {
            text = (int)(num = (int)UtilMath.trim(0, time)) + " Millisecond";
        }
        if (num != 1.0) {
            text += "s";
        }
        return text;
    }

    public static boolean elapsed(final long from, final long required) {
        return System.currentTimeMillis() - from > required;
    }

    public enum TimeUnit
    {
        FIT,
        DAYS,
        HOURS,
        MINUTES,
        SECONDS,
        MILLISECONDS;
    }
}
