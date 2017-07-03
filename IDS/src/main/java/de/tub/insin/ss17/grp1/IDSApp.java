package de.tub.insin.ss17.grp1;

import com.beust.jcommander.JCommander;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IDSApp
{

    private final static Logger log = LoggerFactory.getLogger(IDSApp.class);

    public static void main(String[] argv)
    {
        log.debug("--- START ---");
        IDSCliManager cliManager = new IDSCliManager();
        JCommander.newBuilder()
            .addObject(cliManager)
            .build()
            .parse(argv);
        cliManager.run();
        log.debug("--- FINISHED ---");
    }
}
