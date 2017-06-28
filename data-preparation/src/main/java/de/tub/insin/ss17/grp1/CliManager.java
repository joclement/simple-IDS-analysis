package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.Level;

public class CliManager {

    private final String CSV_FILENAME = "netflow.csv";

    private final String DEFAULT_DEST_PARENT_DIR = "../IDS/src/main/resources/";

    private final String DEFAULT_CTU_DIR = "./src/main/resources/CTU13/";

    private final String ARFF_FILENAME = "data.arff";

    private final String TRAINING_ARFF_FILENAME = "./training/" + ARFF_FILENAME;

    private final String TEST_ARFF_FILENAME = "./test/" + ARFF_FILENAME;

    private static final Logger log = LoggerFactory.getLogger(CliManager.class);

    
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

    @Parameter(names = { "--separateTestScenario", "-t" },
               description = "Use the last number from the option --scenarios as the test scenario")
    private boolean separateTestScenario = false;

    @Parameter(names = { "--destFolder", "-d" },
               description = "Path to the destination folder")
    private File arffFolder = null;

    // TODO add better error reporting
    public void run() throws Exception {
        log.info("---- Start run ----");
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
            try {
                moveToArffFolder(arff, TRAINING_ARFF_FILENAME);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                log.error("---- error to move to arff folder ----");
                e.printStackTrace();
            }
        }
        else {
            split(arff);
        }
        
        log.info("---- finished run ----");
        log.debug("finished: run");
    }

    private void split(File arff) {
        log.info("---- start split arff ----");
        log.debug("start: split");
        DataSplitter dataSplitter = new DataSplitter(percentageTrain);
        List<File> splitted = null;
        try {
            splitted = dataSplitter.split(arff);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("ERROR: failed to split data");
            e.printStackTrace();
            log.error("quit system");
            System.exit(-1);
        }
        try {
            moveToArffFolder(splitted.get(0), TRAINING_ARFF_FILENAME);
            moveToArffFolder(splitted.get(1), TEST_ARFF_FILENAME);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            
            log.error("ERROR: failed to move to Arff-Folder");
            e.printStackTrace();
            log.error("quit system");
            System.exit(-1);
        }
        log.info("---- finished split arff ----");
        log.debug("finished: split");
    }

    private File parse(List<File> csvs) throws Exception {
        log.info("---- start parse data ----");
        log.debug("start: parse");
        File arff = null;
        try {
            arff = CSV2ArffConverter.parse(csvs);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            
            log.error("ERROR: failed to parse data");
            e.printStackTrace();
            log.error("quit system");
            System.exit(-1);
        }
        log.info("---- finished parse data with: {}", arff);
        log.debug("finished: parse");
        return arff;
    }

    private void parseSeparateTestScenario(List<File> csvs) throws Exception {
        log.info("---- start separate test scenario ----");
        log.debug("start: parseSeparateTestScenario");
        try {
            File arff = this.parse(extractTestScenario(csvs));
            this.moveToArffFolder(arff, TEST_ARFF_FILENAME);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("ERROR: failed extracting test scenario");
            e.printStackTrace();
        }
        log.info("---- finished seperate test scenario ----");
        log.debug("finished: parseSeparateTestScenario");
    }

    public List<File> getScenarios() {
        log.info("---- start get scenarios ----");
        log.debug("start: getScenarios");
        CTUManager ctuManager = new CTUManager(ctuFolder, CSV_FILENAME);
        List<File> csvs = null;
        try {
            csvs = ctuManager.find(this.scenarios);
        } catch (FileNotFoundException e) {
            
            log.error("ERROR: file not found");
            e.printStackTrace();
            log.error("quit system");
            System.exit(-1);
        }
        log.info("---- finished get scenarios ----");
        log.debug("finished: getScenarios");
        return csvs;
    }

    private File generateDestFolder() {
        log.info("---- start create destination folder ----");
        log.debug("start: generateDestFolder");
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

        log.info("---- finished create destination folder ----");
        log.debug("finished: generateDestFolder");
        return new File(DEFAULT_DEST_PARENT_DIR, name.toString());
    }

    private void moveToArffFolder(File arff, String arffFilename) throws IOException {
        log.info("---- start move to arff folder ----");
        log.debug("start: moveToArffFolder");
        File testArff = new File(this.arffFolder, arffFilename);
        assert arff.exists();
        assert !testArff.exists();
        testArff.getParentFile().mkdirs();
        Files.move(arff.toPath(), testArff.toPath(), StandardCopyOption.ATOMIC_MOVE);
        log.info("---- finished move to arff folder ----");
        log.debug("finished: moveToArffFolder");
    }

    private List<File> extractTestScenario(List<File> csvs) {
        log.info("---- start extracting test scenario ----");
        log.debug("start: extractTestScenario");
        File testCsv = csvs.remove(csvs.size() - 1);
        List<File> testCsvList = new LinkedList<File>();
        testCsvList.add(testCsv);

        log.info("---- finished extracting test scenario ----");
        log.debug("finished: extractTestScenario");
        return testCsvList;
    }
}
