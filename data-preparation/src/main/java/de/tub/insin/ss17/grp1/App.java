package de.tub.insin.ss17.grp1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

import de.tub.insin.ss17.grp1.shared.SharedUtil;

public class App {

    private final static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] argv) {
        log.debug("--- START ---");
        DataCliManager cliManager = new DataCliManager();
        JCommander commander = JCommander.newBuilder()
            .addObject(cliManager)
            .build();
        commander.parse(argv);
        if (SharedUtil.helpNeeded(commander)) {
            commander.usage();
        } else {
            cliManager.run();
        }
        log.debug("--- FINISHED ---");
    }
}
