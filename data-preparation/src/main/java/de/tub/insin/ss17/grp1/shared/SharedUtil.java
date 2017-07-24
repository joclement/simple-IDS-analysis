package de.tub.insin.ss17.grp1.shared;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;

public class SharedUtil {

    public static String stackTraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();
        return stackTrace;
    }

    public static int checkedConvert(double num) {
        int converted = new Double(num).intValue();
        if (converted != num){
            throw new RuntimeException("Number conversion error.");
        }
        return converted;
    }

    public static boolean helpNeeded(JCommander commander) {
        Iterator<ParameterDescription> it = commander.getParameters().iterator();
        boolean helpNeeded = false;
        while(it.hasNext()) {
            ParameterDescription parameter = it.next();
            if (parameter.isHelp() && parameter.isAssigned()) {
                helpNeeded = true;
            }
        }
        return helpNeeded;
    }
}
