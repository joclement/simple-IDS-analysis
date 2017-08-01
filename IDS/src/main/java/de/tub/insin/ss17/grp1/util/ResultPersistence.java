package de.tub.insin.ss17.grp1.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tub.insin.ss17.grp1.shared.SharedConstants;
import de.tub.insin.ss17.grp1.shared.SharedUtil;


/**
 * Class to handle the saving of the evaluation results.
 *
 * @author Joris Clement
 *
 */
public class ResultPersistence {

    private static final Logger log = LoggerFactory.getLogger(ResultPersistence.class);

    private static final String RESULTS_FOLDER = "./results/";

    private static final String SUMMARY_FILENAME = "summary.txt";

    private final File resultFolder;

    /**
     * Initialize result persistence using the given arff folder and a name for the sub-folder,
     * where the results should be saved in.
     *
     * @param dataFolder the arff folder.
     * @param name the name of the sub-folder.
     */
    public ResultPersistence(String dataFolder, String name) {
        this.resultFolder = new File(dataFolder, RESULTS_FOLDER + name);
        SharedUtil.checkedMkDirs(this.resultFolder);
    }

    public void saveSummary(String summary) {
        File file = new File(resultFolder, SUMMARY_FILENAME);
        try {
            Files.write(file.toPath(), summary.getBytes(SharedConstants.ENCODING));
        } catch (IOException e) {
            log.error("Failed to write the result summary to file,"
                    + " path: " + file.getAbsolutePath());
        }
    }

    public File getFileForSaving(String filename) {
        File file = new File(this.resultFolder, filename);
        return file;
    }
}
