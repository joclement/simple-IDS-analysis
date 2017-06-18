package de.tub.insin.ss17.grp1;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.beust.jcommander.JCommander;

public class App
{
    public static void main(String[] argv) throws Exception 
    {
        CliManager cliManager = new CliManager();
        JCommander.newBuilder()
            .addObject(cliManager)
            .build()
            .parse(argv);
        cliManager.run();
    }
}
