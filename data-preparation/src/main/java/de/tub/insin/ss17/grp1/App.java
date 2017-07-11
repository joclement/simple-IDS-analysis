package de.tub.insin.ss17.grp1;

import java.util.Iterator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;

public class App {
    public static void main(String[] argv) throws Exception {
        DataCliManager cliManager = new DataCliManager();
        JCommander commander = JCommander.newBuilder()
            .addObject(cliManager)
            .build();
        commander.parse(argv);
        Iterator<ParameterDescription> it = commander.getParameters().iterator();
        boolean helpNeeded = false;
        while(it.hasNext()) {
            ParameterDescription parameter = it.next();
            if (parameter.isHelp() && parameter.isAssigned()) {
                helpNeeded = true;
            }
        }
        if (helpNeeded == true) {
            commander.usage();
        } else {
            cliManager.run();
        }
        cliManager.run();
    }
}
