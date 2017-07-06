package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class CTUManager {

    private final File datasetDir;

    private final String filename;

    public CTUManager(String datasetDir, String filename){
        this.datasetDir = new File(datasetDir);
        this.filename = filename;
    }

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
