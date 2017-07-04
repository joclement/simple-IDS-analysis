package de.tub.insin.ss17.grp1;

import com.beust.jcommander.JCommander;

public class App {
    public static void main(String[] argv) throws Exception {
        DataCliManager cliManager = new DataCliManager();
        JCommander.newBuilder()
            .addObject(cliManager)
            .build()
            .parse(argv);
        cliManager.run();
    }
}
