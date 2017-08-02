package de.tub.insin.ss17.grp1.dataprep;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Manager for access to the files in the ctu dataset.
 *
 * @author Joris Clement
 * @author Philip Wilson
 *
 */
public class CTUManager {

    private final File datasetDir;

    private final String filename;

    /**
     * Constructs the manager for the ctu dataset.
     *
     * @param datasetDir the path to the dataset.
     * @param filename the name of the scenario csv files,
     *        the filenames in all scenarios should be the same.
     */
    public CTUManager(String datasetDir, String filename){
        this.datasetDir = new File(datasetDir);
        this.filename = filename;
    }

    /**
     * Gets a list of files to the scenarios.
     *
     * @param scenarios the number(identifiers) for the scenarios, which should be used.
     * @return the list of scenario files.
     * @throws IOException if there is a problem accessing the files.
     */
    public List<File> find(List<Integer> scenarios) throws IOException {

        List<File> files = new LinkedList<File>();
        for(Integer scenario : scenarios){
            files.add(find(scenario));
        }

        return files;
    }

    private File find(Integer scenario) throws IOException {
        String pathInDataset = scenario.toString() + File.separator + this.filename;
        File file = new File(datasetDir, pathInDataset);
        if(!file.canRead()){
            throw new IOException("can not read scenario file, path: " + file.toPath());
        }
        return file;
    }

}
