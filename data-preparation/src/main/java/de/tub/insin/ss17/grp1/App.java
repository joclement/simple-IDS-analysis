package de.tub.insin.ss17.grp1;

import com.beust.jcommander.JCommander;

import de.tub.insin.ss17.grp1.shared.SharedUtil;

public class App {
    public static void main(String[] argv) throws Exception {
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
    }
}
