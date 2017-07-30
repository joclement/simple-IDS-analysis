package de.tub.insin.ss17.grp1;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.tub.insin.ss17.grp1.util.ArffLoader;

import weka.classifiers.Evaluation;
import weka.classifiers.meta.ClassificationViaRegression;
import weka.core.Instances;


public class ArffLoaderTest {

    @Before
    public void beforeEachTest() {
        TestHelper.assertArff();
    }

    @Ignore
    @Test
    public void testArffLoaderWithRandomAlgorithm() throws Exception {
        ArffLoader loader = new ArffLoader(TestHelper.ARFF_FOLDER);
        Instances testData = loader.loadTest();
        ClassificationViaRegression cvr = new ClassificationViaRegression();
        cvr.buildClassifier(testData);
        Evaluation eval = new Evaluation(testData);
        eval.crossValidateModel(cvr, testData, 15, new Random(1));
        System.out.println(eval.toSummaryString());
    }

    @Test
    public void testBasic() {
        ArffLoader loader = new ArffLoader(TestHelper.ARFF_FOLDER);
        try {
            Instances testData = loader.loadTest();
            assertTrue(testData.classIndex() >= 0);
            assertTrue(testData.numClasses() >= 0);
            Instances trainingData = loader.loadTraining();
            assertTrue(trainingData.classIndex() >= 0);
            assertTrue(trainingData.numClasses() >= 0);
        } catch (Exception e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }
}
