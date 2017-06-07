package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.Parameter;

public class CliManager {

    // TODO make this a parameter as well, but of course just optional
    private final String csvFilename = "netflow.csv";

    // TODO make this a parameter as well, but of course just optional
    // TODO correct the default location
    private final String datasetDir = "./data/sets/CTU-13/";

    private final String arffFilename = "normal.arff";

    private final String testArffFilename = "test.arff";

    @Parameter
    private List<String> parameters = new LinkedList<String>();

    @Parameter(names = { "--scenarios", "-s" }, description = "The number for the scenarios in the ctu dataset")
    private List<Integer> scenarios;

    @Parameter(names = { "--percentageTrain", "-p" }, description = "Percentage of the data for the training set")
    private Integer percentageTrain = 80;

    @Parameter(names = { "--numOfFolds", "-f" }, description = "number of folds for cross-validation")
    private Integer numOfFolds = 5;

    @Parameter(names = { "--seperateTestScenario",
            "-t" }, description = "Use the last number from the option --scenarios as the test scenario")
    private boolean seperateTestScenario = false;

    public void run() {
        CTUManager ctuManager = new CTUManager(this.datasetDir, this.csvFilename);
        CSV2ArffConverter converter = new CSV2ArffConverter(this.arffFilename, this.testArffFilename,
                seperateTestScenario);
        DataSplitter dataSplitter = new DataSplitter(seperateTestScenario, arffFilename, testArffFilename,
                percentageTrain);

        try {
            List<File> files = ctuManager.find(this.scenarios);
            File arffFolder = generateDestFolder();
            converter.parse(files, arffFolder);
            dataSplitter.split(arffFolder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File generateDestFolder() {
        // TODO the files should be placed in the resources folder of the other
        // subproject
        // TODO find a good name for the folder in the resources folder in the
        // IDS subproject,
        // where the arff destination will be placed in.
        return null;
    }
}
