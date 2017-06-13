package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class Util {

    public static void saveAsArff(Instances data, File arff) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(arff);
        saver.setDestination(arff);
        saver.writeBatch();
    }

    public static File saveAsArff(Instances data) throws IOException {
        File arffTmp = File.createTempFile("combinedArff", ".arff");
        saveAsArff(data, arffTmp);
        return arffTmp;
    }
}
