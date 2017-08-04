package de.tub.insin.ss17.grp1.util;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.File;

import de.tub.insin.ss17.grp1.shared.RuntimeWekaException;


// TODO improve this loading operations
// TODO add check for arff file validity here
/**
 * Class to load arff data.
 *
 * @author Joris Clement
 * @author Philip Wilson
 *
 */
public class ArffLoader {

    private static final String ARFF_FILENAME = "data.arff";

    private static final String TRAIN_FILEPATH = "./training/" + ARFF_FILENAME;

    private static final String TEST_FILEPATH = "./test/" + ARFF_FILENAME;

    private final File training;
    private final File test;

    public ArffLoader(String arffFolderPath) {
        File arffFolder = new File(arffFolderPath);
        this.training = new File(arffFolder, TRAIN_FILEPATH);
        this.test = new File(arffFolder, TEST_FILEPATH);

        if(!this.training.isFile() || !this.test.isFile()) {
            throw new IllegalArgumentException("Arff folder has wrong format, "
                                             + "does not contain arff data correctly");
        }
    }

    public Instances loadTraining() {
        return this.load(this.training);
    }

    public Instances loadTest() {
        return this.load(this.test);
    }

    private Instances load(File file) {
        Instances data = null;
        try {
            DataSource source = new DataSource(file.getAbsolutePath());
            data = source.getDataSet();
        } catch (Exception e) {
            throw new RuntimeWekaException("Failed to load arff data.");
        }

        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }
}
