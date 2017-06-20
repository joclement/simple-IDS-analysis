package de.tub.insin.ss17.grp1;

import java.util.Random;

import org.junit.Test;

import de.tub.insin.ss17.grp1.util.ArffLoader;

import weka.classifiers.Evaluation;
import weka.classifiers.meta.ClassificationViaRegression;
import weka.core.Instances;


public class ArffLoaderTest {

    @Test
    public void testArffLoaderWithRandomAlgorithm() throws Exception {
        ArffLoader loader = new ArffLoader(AppTest.ARFF_FOLDER);
        Instances testData = loader.loadTest();
        ClassificationViaRegression cvr = new ClassificationViaRegression();
        cvr.buildClassifier(testData);
        Evaluation eval = new Evaluation(testData);
        eval.crossValidateModel(cvr, testData, 15, new Random(1));
        System.out.println(eval.toSummaryString());
    }

}
