package de.tub.insin.ss17.grp1;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.Parameter;

import de.tub.insin.ss17.grp1.training.Trainer;
import de.tub.insin.ss17.grp1.validation.Tester;

public class CliManager {

    private final static String TRAIN = "train";
    private final static String TEST = "test";
    private final static String BOTH = "both";

	@Parameter(description = "command, options: train, test or both")
	private String command;

	@Parameter(names = {"--arffFolder", "-f"}, description = "Path to the arff folder", required = true)
	private String dataFolder;

    // TODO find good way to specify ml parameters, f.x. for NN classifier
	@Parameter
	private List<String> mlParameters = new LinkedList<String>();

	public void run() {
        Trainer trainer = new Trainer();
        Tester tester = new Tester();
        if (command != TEST) {
            trainer.train();
        }

        if (command != TRAIN) {
            tester.test();
        }
	}
}
