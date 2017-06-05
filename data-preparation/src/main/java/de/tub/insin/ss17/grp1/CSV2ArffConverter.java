package de.tub.insin.ss17.grp1;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CSV2ArffConverter {

    private final String arffFilename;

    private final String testArffFilename;

    private final boolean seperateTestScenario;

    public CSV2ArffConverter(String arffFilename,
                             String testArffFilename,
                             boolean seperateTestScenario) {
        this.arffFilename = arffFilename;
        this.testArffFilename = testArffFilename;
        this.seperateTestScenario = seperateTestScenario;
    }

    private static File combine(List<File> csvFiles){
        // TODO this function should combine multiple csv files into 1 csv file.
        // It should be taken care, that the first line of the file has to be deleted for
        return null;
    }

    private static void convert(File mergedSrcFile, File destFolder,
                                String arffFile) throws IOException{
        // TODO code is copied from weka website
        CSVLoader loader = new CSVLoader();
        loader.setSource(mergedSrcFile);
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(destFolder.getAbsolutePath(), arffFile));
        saver.setDestination(new File(destFolder.getAbsolutePath(), arffFile));
        saver.writeBatch();
    }


    public void parse(List<File> srcFiles, File destFolder) throws IOException{
        if (seperateTestScenario) {
            File testFile = srcFiles.remove(srcFiles.size() - 1);
            convert(testFile, destFolder, this.testArffFilename);
        }
        File completeCSVFile = combine(srcFiles);
        convert(completeCSVFile, destFolder, this.arffFilename);
    }
}
