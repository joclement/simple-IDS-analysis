package de.tub.insin.ss17.grp1.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultPersistence {

    private static final Logger log = LoggerFactory.getLogger(ResultPersistence.class);

    private static final String RESULTS_FOLDER = "./results/";

    private static final String SUMMARY_FILENAME = "summary.txt";

    private final File resultFolder;

    public ResultPersistence(String dataFolder, String name) {
        // TODO Auto-generated constructor stub
        this.resultFolder = new File(dataFolder, RESULTS_FOLDER + name);
        this.resultFolder.mkdirs();
    }

    public void saveSummary(String summary) {
        File file = new File(resultFolder, SUMMARY_FILENAME);
        try {
            Files.write(file.toPath(), summary.getBytes());
        } catch (IOException e) {
            log.error("Failed to write the result summary to file,"
                    + " path: " + file.getAbsolutePath());
        }
    }

}
