package de.tub.insin.ss17.grp1;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import de.tub.insin.ss17.grp1.util.ArffLoader;
import weka.classifiers.Classifier;
import weka.classifiers.meta.ClassificationViaRegression;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class TestHelper {

    protected final static String ARFF_FOLDER = "./src/main/resources/test/";

    protected static final String NO_ARFF_FOLDER_TXT;

    static {
        NO_ARFF_FOLDER_TXT = "The tests in data-preparation need to be executed "
                           + "first to have an existing arff folder";
    }

    public static List<String> splitParams(String params) {
        return Arrays.asList(params.split(","));
    }

    public static Instances loadTraining() {
        assertArff();
        ArffLoader loader = new ArffLoader(ARFF_FOLDER);
        try {
            return loader.loadTraining();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Instances loadTest() {
        assertArff();
        ArffLoader loader = new ArffLoader(ARFF_FOLDER);
        try {
            return loader.loadTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Classifier slowClassifier() {
        ClassificationViaRegression cvr = new ClassificationViaRegression();
        try {
            cvr.buildClassifier(loadTraining());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Classifier quickClassifier() {
        J48 cvr = new J48();
        try {
            cvr.buildClassifier(loadTraining());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean hasArff() {
        File arffFolder = new File(ARFF_FOLDER);
        return arffFolder.exists();
    }

    public static void assertArff() {
        assert hasArff(): NO_ARFF_FOLDER_TXT;
    }

    public static List<String> prepareParams(String params) {
        List<String> preparedParams =
                IDSCliManager.prepareParams(Arrays.asList(params.split(",")));
        return preparedParams;
    }
}
