package de.tub.insin.ss17.grp1;

import java.io.PrintWriter;
import java.io.StringWriter;

public class SharedUtil {

    public static String stackTraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();
        return stackTrace;
    }
}
