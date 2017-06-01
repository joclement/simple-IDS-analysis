package de.tub.insin.ss17.grp1;

import com.beust.jcommander.JCommander;

public class App
{
    public static void main(String[] argv)
    {
        CliManager cliManager = new CliManager();
        JCommander.newBuilder()
        .addObject(cliManager)
        .build()
        .parse(argv);
        cliManager.run();
    }
}
