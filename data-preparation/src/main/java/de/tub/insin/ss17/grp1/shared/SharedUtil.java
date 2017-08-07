package de.tub.insin.ss17.grp1.shared;

import java.io.File;
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
        if (converted != num) {
            throw new NumberFormatException("Number conversion error.");
        }
        return converted;
    }

    public static boolean helpNeeded(JCommander commander) {
        Iterator<ParameterDescription> it = commander.getParameters().iterator();
        boolean helpNeeded = false;
        while (it.hasNext()) {
            ParameterDescription parameter = it.next();
            if (parameter.isHelp() && parameter.isAssigned()) {
                helpNeeded = true;
            }
        }
        return helpNeeded;
    }

    public static void checkedMkDirs(File dir) {
        if (!dir.isDirectory() && !dir.mkdirs()) {
            throw new RuntimeException("Failed to create folder(including parents) " +
                                       dir.toPath());
        }
    }

    public static void checkedMkDir(File dir) {
        if (!dir.isDirectory() && !dir.mkdir()) {
            throw new RuntimeException("Failed to create folder" + dir.toPath());
        }
    }
}
