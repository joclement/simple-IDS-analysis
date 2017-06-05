package de.tub.insin.ss17.grp1;

import java.io.File;

public class DataSplitter {

    private final boolean seperateTestScenario;

    private final String arffFilename;

    private final String testArffFilename;

    private Integer percentageTrain;

    private final String FINAL_ARFF_FILENAME = "data.arff";

    public DataSplitter(boolean seperateTestScenario,
                        String arffFilename, String testArffFilename,
                        Integer percentageTrain) {
        this.seperateTestScenario = seperateTestScenario;
        this.arffFilename = arffFilename;
        this.testArffFilename = testArffFilename;
        this.percentageTrain = percentageTrain;
    }

    public void split(File arffFolder) {
        // TODO split the data by the given percentage
        // and put the result for the training in a training folder
        // and for the test in a test folder.
        // TODO Give both files the same name, FINAL_ARFF_FILENAME.

    }

}
