package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class CTUManager {

    private final File datasetDir;

    private final String filename;

    private static final Logger log = LoggerFactory.getLogger(CTUManager.class);

    public CTUManager(String datasetDir, String filename){
        log.debug("-start: CTUManager-");
        this.datasetDir = new File(datasetDir);
        this.filename = filename;
        log.debug("-finished:CTUManager-");
    }

    public List<File> find(List<Integer> scenarios) throws FileNotFoundException {
        
        List<File> files = new LinkedList<File>();
        for(Integer scenario : scenarios){
            files.add(find(scenario));
        }

        return files;
    }

    private File find(Integer scenario) throws FileNotFoundException{
        log.debug("-start: File find-");
        String pathInDataset = scenario.toString() + File.separator + this.filename;
        File file = new File(datasetDir, pathInDataset);
        if(!file.exists()){
            log.error("ERROR: file not found");
            throw new FileNotFoundException();
        }
        log.debug("-finished: File find");
        return file;
    }

}
