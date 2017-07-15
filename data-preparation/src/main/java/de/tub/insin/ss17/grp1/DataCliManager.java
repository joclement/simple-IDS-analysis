package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO should we add printStackTrace on debug level?

public class DataCliManager {

    private final String CSV_FILENAME = "netflow.csv";

    private final String DEFAULT_DEST_PARENT_DIR = "./";

    private final String DEFAULT_CTU_DIR = "./src/main/resources/CTU13/";

    private final String ARFF_FILENAME = "data.arff";

    private final String TRAINING_ARFF_FILENAME = "./training/" + ARFF_FILENAME;

    private final String TEST_ARFF_FILENAME = "./test/" + ARFF_FILENAME;

    private static final Logger log = LoggerFactory.getLogger(DataCliManager.class);


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

    @Parameter(names = { "--separateTestScenario", "-t" },
               description = "Use the last number from the option --scenarios as the test scenario")
    private boolean separateTestScenario = false;

    @Parameter(names = { "--destFolder", "-d" },
               description = "Path to the destination folder,"
                           + " by default the result folder will be placed in the current folder"
                           + " with a name describing its contents.")
    private File arffFolder = null;

    @Parameter(names = { "--removeBackground", "-r" },
               description = "If true, remove all Background Instances")
    private boolean removeBackground = false;

    @Parameter(names = {"--help", "-h"}, help = true)
    private boolean help;

    // TODO add better error reporting
    public void run() throws Exception {
        log.debug("start: run");

        if (this.arffFolder == null) {
            this.arffFolder = generateDestFolder();
        }

        List<File> csvs = getScenarios();

        if (this.separateTestScenario) {
            parseSeparateTestScenario(csvs);
        }

        File arff = parse(csvs);
        if (this.separateTestScenario) {
            moveToArffFolder(arff, TRAINING_ARFF_FILENAME);
        }
        else {
            split(arff);
        }

        log.info("Arff files moved to: {}", arffFolder);
        log.debug("finished: run");
    }

    private void split(File arff) {
        log.debug("split into training and test");
        DataSplitter dataSplitter = new DataSplitter(percentageTrain);
        List<File> splitted = null;
        try {
            splitted = dataSplitter.split(arff);
        } catch (Exception e) {
            shutdown("failed to split data into training and test instances");
        }

        log.debug("move training arff file to destination");
        moveToArffFolder(splitted.get(0), TRAINING_ARFF_FILENAME);
        log.debug("move test arff file to destination");
        moveToArffFolder(splitted.get(1), TEST_ARFF_FILENAME);
    }

    private File parse(List<File> csvs) throws Exception {
        log.debug("convert from csv to arff");
        File arff = null;
        try {
            arff = CSV2ArffConverter.parse(csvs, this.removeBackground);
        } catch (IOException e) {
            shutdown("failed to convert data from csv to arff" + e.getMessage());
        }
        return arff;
    }

    private void parseSeparateTestScenario(List<File> csvs) throws Exception {
        log.debug("convert seperate test scenario");
        File arff = null;
        try {
            File testScenario = extractTestScenario(csvs);
            List<File> testScenarioList = new LinkedList<>();
            testScenarioList.add(testScenario);
            arff = this.parse(testScenarioList);
        } catch (IOException e) {
            shutdown("failed to extract test scenario");
        }
        this.moveToArffFolder(arff, TEST_ARFF_FILENAME);
    }

    public List<File> getScenarios() {
        log.debug("get scenarios");

        CTUManager ctuManager = new CTUManager(ctuFolder, CSV_FILENAME);
        List<File> csvs = null;
        try {
            csvs = ctuManager.find(this.scenarios);
        } catch (IOException e) {
            shutdown(e.getMessage());
        }
        log.debug("list of scenarios {}", csvs);
        return csvs;
    }

    private File generateDestFolder() {
        StringBuilder name = new StringBuilder();
        name.append("scenarios=");
        Iterator<Integer> it = this.scenarios.iterator();
        name.append(it.next());
        while(it.hasNext()) {
            name.append(",");
            name.append(it.next());
        }

        name.append("_");

        name.append("percentageTrain=");
        name.append(this.percentageTrain);

        name.append("_");

        name.append("separateTestScenario=");
        name.append(this.separateTestScenario);

        return new File(DEFAULT_DEST_PARENT_DIR, name.toString());
    }

    private void moveToArffFolder(File arff, String arffFilename) {
        File destinationArff = new File(this.arffFolder, arffFilename);
        destinationArff.getParentFile().mkdirs();
        try {
            Files.move(arff.toPath(), destinationArff.toPath(), StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            String description =
                    "Failed to move resulting training arff to destination folder, "
                    + "from: " + arff.toPath()
                    + " , to: " + destinationArff.toPath();
            shutdown(description);
        }
    }

    private File extractTestScenario(List<File> csvs) {
        File testCsv = csvs.remove(csvs.size() - 1);

        log.debug("extracted test scenario: {}", testCsv);
        return testCsv;
    }

    private static void shutdown(String description) {
        if (!description.isEmpty()) {
            log.error(description);
        }
        log.error("Shutdown programm");
        System.exit(1);
    }
}
