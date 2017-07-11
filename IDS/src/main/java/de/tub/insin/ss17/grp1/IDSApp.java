package de.tub.insin.ss17.grp1;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IDSApp
{

    private final static Logger log = LoggerFactory.getLogger(IDSApp.class);

    public static void main(String[] argv)
    {
        log.debug("--- START ---");
        IDSCliManager cliManager = new IDSCliManager();
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
        log.debug("--- FINISHED ---");
    }
}
