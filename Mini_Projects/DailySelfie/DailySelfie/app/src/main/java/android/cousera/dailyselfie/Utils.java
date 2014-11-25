package android.cousera.dailyselfie;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {

    public static final SimpleDateFormat SAVE_DATE_FORMATTER =  new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    public static final SimpleDateFormat SHOW_DATE_FORMATTER = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE, d MMM yyyy HH:mm:ss"));

    public static void d(String tag, String strInfo){
        Log.d(tag, strInfo);
    }
}
