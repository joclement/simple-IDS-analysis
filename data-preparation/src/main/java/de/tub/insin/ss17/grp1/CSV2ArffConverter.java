package de.tub.insin.ss17.grp1;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CSV2ArffConverter {


    private static File combine(List<File> files){
    	return null;
    }

    private static void convert(File mergedSrcFile, File destFile) throws IOException{
    	CSVLoader loader = new CSVLoader();
        loader.setSource(mergedSrcFile);
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(destFile.getAbsolutePath()));
        saver.setDestination(new File(destFile.getAbsolutePath()));
        saver.writeBatch();
    }


    public static void parse(List<File> srcFiles, File destFile) throws IOException{
    	File completeCSVFile = combine(srcFiles);
    	convert(completeCSVFile, destFile);
    }
}
