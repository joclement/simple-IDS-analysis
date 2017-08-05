package de.tub.insin.ss17.grp1.util;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

import java.io.File;
import java.util.Arrays;

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

    private NumericToNominal numToNominalConverter;

    public ArffLoader(String arffFolderPath) {
        this(arffFolderPath, null);
    }
    public ArffLoader(String arffFolderPath, String numToNominalCols) {
        File arffFolder = new File(arffFolderPath);
        this.training = new File(arffFolder, TRAIN_FILEPATH);
        this.test = new File(arffFolder, TEST_FILEPATH);

        if(!this.training.isFile() || !this.test.isFile()) {
            throw new IllegalArgumentException("Arff folder has wrong format, "
                                             + "does not contain arff data correctly");
        }

        this.setUpNumToNominalConverter(numToNominalCols);
    }

    private void setUpNumToNominalConverter(String numToNominalCols) {
        if (numToNominalCols == null) {
            this.numToNominalConverter = null;
        } else {

            this.numToNominalConverter = new NumericToNominal();
            this.numToNominalConverter.setAttributeIndicesArray(stringToInts(numToNominalCols));
            try {
                this.numToNominalConverter.setInputFormat(this.load(this.training));
            } catch (Exception e) {
                throw new RuntimeWekaException("Failed to set up numeric to nominal converter. "
                                             + e.getMessage());
            }
        }
    }

    public Instances loadTraining() {
        Instances training = this.load(this.training);
        if (this.hasConverterBeenSetUp()) {
            training = this.convertNumToNominal(training);
        }
        return training;
    }

    public Instances loadTest() {
        Instances test = this.load(this.test);
        if (this.hasConverterBeenSetUp()) {
            test = this.convertNumToNominal(test);
        }
        return test;
    }

    private boolean hasConverterBeenSetUp() {
        return this.numToNominalConverter != null;
    }

    private Instances load(File file) {
        Instances data = null;
        try {
            DataSource source = new DataSource(file.getAbsolutePath());
            data = source.getDataSet();
        } catch (Exception e) {
            throw new RuntimeWekaException("Failed to load arff data. " + e.getMessage());
        }

        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

    private Instances convertNumToNominal(Instances originalData) {
        Instances convertedData;
        try {
            convertedData = Filter.useFilter(originalData, this.numToNominalConverter);
        } catch (Exception e) {
            throw new RuntimeWekaException("Failed to convert numeric attributes to nominal. "
                                         + e.getMessage());
        }
        return convertedData;
    }

    private static int[] stringToInts(String numbersCommaSeperated) {
       return Arrays.stream(numbersCommaSeperated.split(",")).mapToInt(Integer::parseInt).toArray();
    }
}
