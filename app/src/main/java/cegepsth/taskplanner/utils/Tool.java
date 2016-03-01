package cegepsth.taskplanner.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vincent on 2016-02-17.
 */
public class Tool {

    public static String DateToPrettyString(Date date) {
        return date.toGMTString().replaceAll(" GMT","");
    }

    public static String DateToRealString(Date date){
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dt.format(date);
    }
}
