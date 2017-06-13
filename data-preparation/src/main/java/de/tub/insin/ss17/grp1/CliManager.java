package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.beust.jcommander.Parameter;

public class CliManager {

    private final String CSV_FILENAME = "netflow.csv";

    private final String DEFAULT_DEST_PARENT_DIR = "../IDS/src/main/resources/";

    private final String DEFAULT_CTU_DIR = "./src/main/resources/CTU13/";

    private final String ARFF_FILENAME = "data.arff";

    private final String TRAINING_ARFF_FILENAME = "./training/" + ARFF_FILENAME;

    private final String TEST_ARFF_FILENAME = "./test/" + ARFF_FILENAME;

	@Parameter(names = {"--ctu", "-c"},
               description = "Path to the ctu13 folder")
	private String ctuFolder = DEFAULT_CTU_DIR;

    @Parameter(names = { "--scenarios", "-s" },
               description = "The number for the scenarios in the ctu dataset",
               required = true)
    private List<Integer> scenarios;

    @Parameter(names = { "--percentageTrain", "-p" },
               description = "Percentage of the data for the training set")
    private Integer percentageTrain = 80;

    @Parameter(names = { "--numOfFolds", "-f" },
               description = "number of folds for cross-validation")
    private Integer numOfFolds = 5;

    @Parameter(names = { "--seperateTestScenario", "-t" },
               description = "Use the last number from the option --scenarios as the test scenario")
    private boolean seperateTestScenario = false;

    @Parameter(names = { "--destFolder", "-d" },
               description = "Path to the destination folder")
    private File arffFolder = null;

    // TODO add better error reporting
    public void run() {
        if (this.arffFolder == null) {
            this.arffFolder = generateDestFolder();
        }

        List<File> csvs = getScenarios();

        if (this.seperateTestScenario) {
            parseSeperateTestScenario(csvs);
        }

        File arff = parse(csvs);

        if (this.seperateTestScenario) {
            try {
                moveToArffFolder(arff, TRAINING_ARFF_FILENAME);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            split(arff);
        }

    }

    private void split(File arff) {
        DataSplitter dataSplitter = new DataSplitter(percentageTrain);
        List<File> splitted = null;
        try {
            splitted = dataSplitter.split(arff);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(-1);
        }
        try {
            moveToArffFolder(splitted.get(0), TRAINING_ARFF_FILENAME);
            moveToArffFolder(splitted.get(1), TEST_ARFF_FILENAME);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private File parse(List<File> csvs) {
        File arff = null;
        try {
            arff = CSV2ArffConverter.parse(extractTestScenario(csvs));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(-1);
        }

        return arff;
    }

    private void parseSeperateTestScenario(List<File> csvs) {
        try {
            File arff = this.parse(extractTestScenario(csvs));
            this.moveToArffFolder(arff, TEST_ARFF_FILENAME);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private List<File> getScenarios() {

        CTUManager ctuManager = new CTUManager(ctuFolder, CSV_FILENAME);
        List<File> csvs = null;
        try {
            csvs = ctuManager.find(this.scenarios);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return csvs;
    }

    private File generateDestFolder() {
        StringBuilder name = new StringBuilder();
        name.append("scenarios");
        for (Integer scenario : this.scenarios) {
            name.append(",");
            name.append(scenario);
        }

        name.append("_");

        name.append("percentageTrain=");
        name.append(this.percentageTrain);

        name.append("_");

        name.append("seperateTestScenario=");
        name.append(this.seperateTestScenario);

        return new File(this.ctuFolder, name.toString());
    }

    private void moveToArffFolder(File arff, String arffFilename) throws IOException {
        File testArff = new File(this.arffFolder, arffFilename);
        assert arff.exists();
        assert !testArff.exists();
        testArff.getParentFile().mkdirs();
        Files.move(arff.toPath(), testArff.toPath(), StandardCopyOption.ATOMIC_MOVE);
    }

    private List<File> extractTestScenario(List<File> csvs) {
        File testCsv = csvs.remove(csvs.size() - 1);
        List<File> testCsvList = new LinkedList<File>();
        testCsvList.add(testCsv);

        return testCsvList;
    }
}
