package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;

import com.beust.jcommander.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        log.debug("---- start run ----");
        
        log.debug("-start if: arffFolder = generateDestFolder-");
        if (this.arffFolder == null) {
            this.arffFolder = generateDestFolder();
        }
        log.debug("-finished if: arffFolder = generateDestFolder-");

        List<File> csvs = getScenarios();

        log.debug("-start if: parseSeperateTestScenario-");
        if (this.separateTestScenario) {
            parseSeparateTestScenario(csvs);
        }
        log.debug("-finished if: parseSeperateTestScenario-");

        File arff = parse(csvs);
        log.debug("-start if: move to arff folder-");
        if (this.separateTestScenario) {
            log.debug("-start try: moveToArffFolder-");
            try {
                log.debug("- move to arff folder -");
                moveToArffFolder(arff, TRAINING_ARFF_FILENAME);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                log.error("---- error to move to arff folder ----");
                e.printStackTrace();
            }
        }
        else {
            log.debug("-else: split-");
            split(arff);
        }
        log.debug("-finished if: move to arff folder-");
        
        log.info("---- finished run ----");
        log.debug("---- finished run ----");
    }

    private void split(File arff) {
        log.info("---- start split arff ----");
        log.debug("---- start split arff ----");
        DataSplitter dataSplitter = new DataSplitter(percentageTrain);
        List<File> splitted = null;
        try {
            log.debug("---- try split data ----");
            splitted = dataSplitter.split(arff);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("---- error to splitt data ----");
            e.printStackTrace();
            log.error("---- quit program ----");
            System.exit(-1);
        }
        try {
            log.debug("---- try move to arff folder ----");
            moveToArffFolder(splitted.get(0), TRAINING_ARFF_FILENAME);
            moveToArffFolder(splitted.get(1), TEST_ARFF_FILENAME);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            
            log.error("---- error to move to arff folder ----");
            e.printStackTrace();
            log.error("---- quit program ----");
            System.exit(-1);
        }
        log.info("---- finished split arff ----");
        log.debug("---- finished split arff ----");
    }

    private File parse(List<File> csvs) throws Exception {
        log.info("---- start parse data ----");
        log.debug("---- start parse data ----");
        File arff = null;
        try {
            log.debug("---- try to parse data ----");
            arff = CSV2ArffConverter.parse(csvs);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            
            log.error("---- error to parse data ----");
            e.printStackTrace();
            log.error("---- quit program ----");
            System.exit(-1);
        }
        log.info("---- finished parse data with: {}", arff);
        log.debug("---- finished parse data with: {}", arff);
        return arff;
    }

    private void parseSeparateTestScenario(List<File> csvs) throws Exception {
        log.info("---- start separate test scenario ----");
        log.debug("---- start seperate test scenario ----");
        try {
            log.debug("---- try to extract test scenario ----");
            File arff = this.parse(extractTestScenario(csvs));
            this.moveToArffFolder(arff, TEST_ARFF_FILENAME);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("---- ERROR extracting test scenario ----");
            e.printStackTrace();
        }
        log.info("---- finished seperate test scenario ----");
        log.debug("---- finished seperate test scenario ----");
    }

    public List<File> getScenarios() {
        log.info("---- start get scenarios ----");
        log.debug("---- start get scnearios ----");
        CTUManager ctuManager = new CTUManager(ctuFolder, CSV_FILENAME);
        List<File> csvs = null;
        try {
            log.debug("---- try finding scenarios ----");
            csvs = ctuManager.find(this.scenarios);
        } catch (FileNotFoundException e) {
            
            log.error("---- ERROR file not found ----");
            e.printStackTrace();
            log.error("---- exit program ----");
            System.exit(-1);
        }
        log.info("---- finished get scenarios ----");
        log.debug("---- finished get scenarios ----");
        return csvs;
    }

    private File generateDestFolder() {
        log.info("---- start create destination folder ----");
        log.debug("---- start create destination folder ----");
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
        log.debug("---- finished create destination folder ----");
        return new File(DEFAULT_DEST_PARENT_DIR, name.toString());
    }

    private void moveToArffFolder(File arff, String arffFilename) throws IOException {
        log.info("---- start move to arff folder ----");
        log.debug("---- start move to arff folder ----");
        File testArff = new File(this.arffFolder, arffFilename);
        assert arff.exists();
        assert !testArff.exists();
        testArff.getParentFile().mkdirs();
        Files.move(arff.toPath(), testArff.toPath(), StandardCopyOption.ATOMIC_MOVE);
        log.info("---- finished move to arff folder ----");
        log.debug("---- finished move to arff folder ----");
    }

    private List<File> extractTestScenario(List<File> csvs) {
        File testCsv = csvs.remove(csvs.size() - 1);
        List<File> testCsvList = new LinkedList<File>();
        testCsvList.add(testCsv);

        return testCsvList;
    }
}
