package de.tub.insin.ss17.grp1.util;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.File;

import de.tub.insin.ss17.grp1.shared.RuntimeWekaException;


// TODO improve this loading operations
// TODO add check for arff file validity here
public class ArffLoader {

    private static final String ARFF_FILENAME = "data.arff";

    private static final String TRAIN_FILEPATH = "./training/" + ARFF_FILENAME;

    private static final String TEST_FILEPATH = "./test/" + ARFF_FILENAME;

    private final File arffFolder;

    public ArffLoader(String arffFolder) {
        this.arffFolder = new File(arffFolder);
    }

    public Instances loadTraining() {
        File trainFile = new File(this.arffFolder, TRAIN_FILEPATH);
        return this.load(trainFile);
    }

    public Instances loadTest() {
        File testFile = new File(this.arffFolder, TEST_FILEPATH);
        return this.load(testFile);
    }

    private Instances load(File file) {
        Instances data = null;
        try {
            DataSource source = new DataSource(file.getAbsolutePath());
            data = source.getDataSet();
        } catch (Exception e) {
            throw new RuntimeWekaException("Failed load arff data.");
        }

        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }
}
