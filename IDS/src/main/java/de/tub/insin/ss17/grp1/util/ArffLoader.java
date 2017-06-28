package de.tub.insin.ss17.grp1.util;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.File;


// TODO improve this loading operations
public class ArffLoader {

    private static final String ARFF_FILENAME = "data.arff";

    private static final String TRAIN_FILEPATH = "./training/" + ARFF_FILENAME;

    private static final String TEST_FILEPATH = "./test/" + ARFF_FILENAME;

    private final File arffFolder;

    public ArffLoader(String path) {
        this.arffFolder = new File(path);
    }

    public Instances loadTraining() throws Exception {
        File trainFile = new File(this.arffFolder, TRAIN_FILEPATH);
        return this.load(trainFile);
    }

    public Instances loadTest() throws Exception {
        File testFile = new File(this.arffFolder, TEST_FILEPATH);
        return this.load(testFile);
    }

    private Instances load(File file) throws Exception {
        DataSource source = new DataSource(file.getAbsolutePath());
        Instances data = source.getDataSet();

        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }
}