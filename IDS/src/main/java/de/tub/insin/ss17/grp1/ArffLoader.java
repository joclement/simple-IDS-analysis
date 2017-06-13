package de.tub.insin.ss17.grp1;

import weka.core.converters.ArffLoader.ArffReader;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.ClassificationViaRegression;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;


// TODO improve this loading operations
public class ArffLoader{

    private static final String arffFilename = "data.arff";

    private static final String trainFilepath = "./training/" + arffFilename;

    private static final String testFilepath = "./test/" + arffFilename;

    private final File arffFolder;

    public ArffLoader(String path) {
        this.arffFolder = new File(path);
    }

    public Instances loadTraining() throws Exception {
        File trainFile = new File(this.arffFolder, trainFilepath);
        return load(trainFile);
    }

    public Instances loadTest() throws Exception {
        File testFile = new File(this.arffFolder, testFilepath);
        return load(testFile);
    }

    private Instances load(File file) throws Exception {
        DataSource source = new DataSource(file.getAbsolutePath());
        Instances data = source.getDataSet();

        return data;
    }
}
