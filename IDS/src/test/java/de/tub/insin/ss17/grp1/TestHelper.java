package de.tub.insin.ss17.grp1;

import java.util.Arrays;
import java.util.List;

import de.tub.insin.ss17.grp1.util.ArffLoader;
import weka.classifiers.Classifier;
import weka.classifiers.meta.ClassificationViaRegression;
import weka.core.Instances;

public class TestHelper {

    public static List<String> splitParams(String params) {
        return Arrays.asList(params.split(","));
    }

    public static Instances loadTraining() {
        ArffLoader loader = new ArffLoader(AppTest.ARFF_FOLDER);
        try {
            return loader.loadTraining();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Instances loadTest() {
        ArffLoader loader = new ArffLoader(AppTest.ARFF_FOLDER);
        try {
            return loader.loadTest();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Classifier someClassifier() {
        ClassificationViaRegression cvr = new ClassificationViaRegression();
        try {
            cvr.buildClassifier(loadTraining());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
