package de.tub.insin.ss17.grp1;

import com.beust.jcommander.JCommander;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App
{

    private final static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] argv)
    {
        log.info("--- START ---");
        IDSCliManager cliManager = new IDSCliManager();
        JCommander.newBuilder()
            .addObject(cliManager)
            .build()
            .parse(argv);
        cliManager.run();
        log.info("--- FINISHED ---");
    }
}
