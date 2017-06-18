package de.tub.insin.ss17.grp1;


import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.tub.insin.ss17.grp1.util.ArffLoader;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.ClassificationViaRegression;
import weka.core.Instances;


/**
 * Unit test for IDS.
 */
public class AppTest 
{

    private final static String ARFF_FOLDER = "./src/main/resources/testArff/";

    private final static String NN_PARAMS = "k=5,dist=20.0";

    @Before
    public void beforeEachTest() {
        System.out.println("This is executed before each Test");
    }

    @After
    public void afterEachTest() {
        System.out.println("This is executed after each Test");
    }
    
    @Test
    public void testArffLoaderWithRandomAlgorithm() throws Exception {
        String[] argv = {"train",
                         "-f", ARFF_FOLDER,
                         "-p", NN_PARAMS};
        ArffLoader loader = new ArffLoader(ARFF_FOLDER);
        Instances testData = loader.loadTest();
        ClassificationViaRegression cvr = new ClassificationViaRegression();
        testData.setClass(testData.attribute("label"));
        cvr.buildClassifier(testData);
        Evaluation eval = new Evaluation(testData);
        eval.crossValidateModel(cvr, testData, 15, new Random(1));
        System.out.println(eval.toSummaryString());
        
    }

    // TODO add checks
    @Ignore
    @Test
    public void testTrainCommand() {
        String[] argv = {"train",
                         "-f", ARFF_FOLDER,
                         "-p", NN_PARAMS};
        App.main(argv);
    }

    // TODO add checks
    @Ignore
    @Test
    public void testTestCommand() {
        String[] argv = {"train,test",
                         "-f", ARFF_FOLDER,
                         "-p", NN_PARAMS};
        App.main(argv);
    }

    // TODO add test for invalid args
    @Ignore
    @Test
    public void testWrongInput() {
    }

}
